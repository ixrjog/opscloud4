package com.sdg.cmdb.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.oss.model.OSSObjectSummary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.offbytwo.jenkins.helper.JenkinsVersion;

import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.ci.*;
import com.sdg.cmdb.domain.ci.ciStatus.CiDeployVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiStatusVO;
import com.sdg.cmdb.domain.ci.jenkins.Notify;

import com.sdg.cmdb.domain.dingtalk.DingtalkDO;


import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.configurationProcessor.AnsibleFileProcessorService;

import com.sdg.cmdb.util.*;

import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liangjian on 2017/2/17.
 */
@Service
public class CiServiceImpl implements CiService {

    private static final Logger logger = LoggerFactory.getLogger(CiServiceImpl.class);

    @Resource
    private AuthService authService;

    @Resource
    private GitlabService gitlabService;

    @Resource
    private UserDao userDao;

    @Resource
    private JenkinsService jenkinsService;

    @Resource
    private CiDao ciDao;

    @Resource
    private DingtalkDao dingtalkDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private AnsibleFileProcessorService ansibleFileProcessorService;

    @Resource
    private DingtalkService dingtalkService;

    @Resource
    private SchedulerManager schedulerManager;

    @Autowired
    private AnsibleFileProcessorService ansibleService;

    @Resource
    private AliyunOssService aliyunOssService;

    static public final String PARAM_BRANCH = "branch";

    static public final String PARAM_HOSTPATTERN = "hostPattern";

    static public final String PARAM_SSHURL = "sshUrl";

    static public final String PARAM_ARTIFACT_PATH = "artifactPath";

    // TODO Deploy
    static public final String PARAM_OSS_PATH = "ossPath";

    static public final String TEMPLATE = "template_";

    static public final String BUILD_CONSOLE = "console";

    static public final String BUILD_PLAIN_LINK = "badge/icon";

    static public final String VERSION_NAME = "release-";

    // TODO buildType
    static public final int TYPE_BUILD = 0;
    static public final int TYPE_DEPLOY = 1;

    @Override
    public BusinessWrapper<Boolean> buildNotify(Notify notify) {
        // JSONObject notifyJson

        // Notify notify
        // TODO 打印日志用于调试
        // logger.info("Jenkins Notifications: {}", notifyJson);
        // Gson gson = new GsonBuilder().create();
        //  Notify notify = gson.fromJson(notifyJson.toJSONString(), Notify.class);

        // TODO 异步处理
        schedulerManager.registerJob(() -> {
            CiBuildDO ciBuildDO = ciDao.getBuildByUnique(notify.getName(), notify.getBuild().getNumber());
            if (ciBuildDO == null) {
                logger.info("未找到JobBuild(可能是任务队列) jobName = {} , buildNumber = {}", notify.getName(), notify.getBuild().getNumber());
                ciBuildDO = ciDao.getBuildByQueue(notify.getName());
                if (ciBuildDO == null) {
                    logger.info("未找到JobBuild(包括任务队列)，此通知可能存在配置错误 jobName = {} , buildNumber = {}", notify.getName(), notify.getBuild().getNumber());
                    return;
                } else {
                    ciBuildDO.setBuildNumber(notify.getBuild().getNumber());
                }
            }
            CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
            boolean isDeploy = isDeploy(ciJobDO, notify);
            ciBuildDO = updateBuild(ciBuildDO, notify, isDeploy);
            // TODO 更新 buildPhase/buildStatus 如果是队列则更新buildNumber
            //if (ciBuildDO == null) return;
            BuildNotifyDO buildNotifyDO = new BuildNotifyDO(ciBuildDO.getId(), notify);
            try {
                BuildNotifyDO checkNotify = ciDao.getBuildNotifyByUnique(buildNotifyDO);
                if (checkNotify != null)
                    ciDao.delBuildNotify(checkNotify.getId());
                ciDao.addBuildNotify(buildNotifyDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // TODO 记录Artifacts
            if (!isDeploy && buildNotifyDO.getBuildPhase().equals("FINALIZED"))
                saveBuildArtifacts(notify, ciBuildDO);
            // TODO 更新DeployHistory
            updateDeployHistory(ciBuildDO, buildNotifyDO);
            // TODO 触发通知
            notify(ciBuildDO, buildNotifyDO, isDeploy);
        });
        return new BusinessWrapper<>(true);
    }

    private boolean isDeploy(CiJobDO ciJobDO, Notify notify) {
        if (notify.getName().equals(ciJobDO.getJobName()))
            return false;
        return true;
    }

    /**
     * TODO Check build type(Build / Deploy)
     * 通知需要判断类型
     *
     * @param ciBuildDO
     * @param buildNotifyDO
     */
    private void notify(CiBuildDO ciBuildDO, BuildNotifyDO buildNotifyDO, boolean isDeploy) {
        if (isDeploy) {
            dingtalkService.notifyDeploy(ciBuildDO, buildNotifyDO);
        } else {
            dingtalkService.notifyCi(ciBuildDO, buildNotifyDO);
        }
    }


    /**
     * 更新build状态
     *
     * @param notify
     * @return
     */
    private CiBuildDO updateBuild(CiBuildDO ciBuildDO, Notify notify, boolean isDeploy) {
        try {
            ciBuildDO.setBuildPhase(notify.getBuild().getPhase());
            ciBuildDO.setBuildStatus(notify.getBuild().getStatus());
            // TODO 队列任务插入默认版本号(Deploy不插入)
            if (!isDeploy && StringUtils.isEmpty(ciBuildDO.getVersionName()))
                ciBuildDO.setVersionName(VERSION_NAME + ciBuildDO.getBuildNumber());
            ciDao.updateBuild(ciBuildDO);
            return ciBuildDO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TODO 记录 Artifacts
     * cmdb-web/build/libs/opscloud-1.0.0-SNAPSHOT.war
     *
     * @param notify
     * @param ciBuildDO
     */
    private void saveBuildArtifacts(Notify notify, CiBuildDO ciBuildDO) {
        // logger.info("Save build artifacts Notify : {}", notify.toString());
        // logger.info("Save build artifacts CiBuildDO : {}", ciBuildDO.toString());

        if (ciBuildDO == null) return;
        Map<String, HashMap<String, String>> artifacts = notify.getBuild().getArtifacts();
        for (String artifactsName : artifacts.keySet()) {
            HashMap<String, String> artifact = artifacts.get(artifactsName);
            if (artifact.containsKey("archive")) {
                String archiveUrl = artifact.get("archive");
                if (StringUtils.isEmpty(archiveUrl)) return;
                BuildArtifactDO buildArtifactDO = new BuildArtifactDO(ciBuildDO.getId(), artifactsName, archiveUrl);
                // logger.info("Save build artifacts BuildArtifactDO : {}", buildArtifactDO.toString());
                //String ossPath = getOssPath(ciBuildDO, buildArtifactDO);
                OSSObjectSummary ossObject = getOssObject(ciBuildDO, buildArtifactDO);
                if (ossObject != null) {
                    buildArtifactDO.setOssPath(ossObject.getKey());
                    buildArtifactDO.setArtifactSize((int) ossObject.getSize());
                }
                try {
                    logger.info("checkBuildArtifactByUnique buildId = {} , check = {}", ciBuildDO.getId(), ciDao.checkBuildArtifactByUnique(buildArtifactDO));
                    if (ciDao.checkBuildArtifactByUnique(buildArtifactDO) == 0)
                        ciDao.addBuildArtifact(buildArtifactDO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 转换 ArtifactPath 2 OssPath
     * cmdb-web/build/libs/opscloud-1.0.0-SNAPSHOT.war
     * ci/${jobName}/${buildNumber}/opscloud-1.0.0-SNAPSHOT.war
     *
     * @return
     */
    private OSSObjectSummary getOssObject(CiBuildDO ciBuildDO, BuildArtifactDO buildArtifactDO) {
        try {
            String artifactPath = getBuildParamValue(ciBuildDO, PARAM_ARTIFACT_PATH);
            String artifactName = buildArtifactDO.getArtifactName().replace(artifactPath, "");
            String ossPath = "ci/" + ciBuildDO.getJobName() + "/" + ciBuildDO.getBuildNumber() + "/" + artifactName;
            List<OSSObjectSummary> list = aliyunOssService.listObject(ossPath);
            if (list != null && list.size() == 1)
                for (OSSObjectSummary object : list) {
                    return object;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CiAppVO getApp(long id) {
        CiAppDO ciAppDO = ciDao.getCiApp(id);
        return getCiAppVO(ciAppDO);
    }

    @Override
    public List<CiAppVO> queryMyApp(String projectName) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        // 管理员可查看所有App
        if (authService.isRole(username, RoleDO.roleAdmin))
            userDO.setId(0);
        List<CiAppDO> list = ciDao.getCiAppByAuthUserId(userDO.getId(), projectName);
        List<CiAppVO> voList = new ArrayList<>();
        for (CiAppDO ciAppDO : list)
            voList.add(getCiAppVO(ciAppDO));
        return voList;
    }


    @Override
    public BusinessWrapper<Boolean> saveApp(CiAppVO ciAppVO) {
        CiAppDO ciAppDO = new CiAppDO(ciAppVO);
        if (ciAppDO.getUserId() == 0) {
            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            ciAppDO.setUserId(userDO.getId());
        }
        try {
            if (ciAppDO.getId() == 0) {
                ciDao.addCiApp(ciAppDO);
            } else {
                ciDao.updateCiApp(ciAppDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    private CiAppVO getCiAppVO(CiAppDO ciAppDO) {
        List<GitlabBranch> branchList = gitlabService.getProjectBranchs(ciAppDO.getProjectId());
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ciAppDO.getServerGroupId());
        CiAppVO ciAppVO = new CiAppVO(ciAppDO, branchList, serverGroupDO);
        DingtalkDO dingtalkDO = dingtalkDao.getDingtalk(ciAppDO.getDingtalkId());
        List<DingtalkDO> dingtalkList = dingtalkDao.queryDingtalk();
        ciAppVO.setDingtalkDO(dingtalkDO);
        ciAppVO.setDingtalkList(dingtalkList);
        return ciAppVO;
    }

    @Override
    public BusinessWrapper<Boolean> saveJob(CiJobVO ciJobVO) {
        try {
            if (ciJobVO.getId() == 0) {
                ciDao.addCiJob(ciJobVO);
            } else {
                ciDao.updateCiJob(ciJobVO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public BusinessWrapper<Boolean> createJob(long jobId) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        if (!StringUtils.isEmpty(ciJobDO.getJobTemplate())) {
            Job job = jenkinsService.getJob(ciJobDO.getJobName());
            if (job == null)
                if (jenkinsService.createJobByTemplate(ciJobDO.getJobName(), ciJobDO.getJobTemplate()))
                    return new BusinessWrapper<Boolean>(false);
        }
        if (!StringUtils.isEmpty(ciJobDO.getDeployJobTemplate())) {
            Job job = jenkinsService.getJob(ciJobDO.getDeployJobName());
            if (job == null)
                if (jenkinsService.createJobByTemplate(ciJobDO.getDeployJobName(), ciJobDO.getDeployJobTemplate()))
                    return new BusinessWrapper<Boolean>(false);
        }
        return new BusinessWrapper<Boolean>(true);
    }


    @Override
    public List<CiJobVO> queryJob(long appId) {
        List<CiJobDO> list = ciDao.getCiJobByAppId(appId);
        List<CiJobVO> voList = new ArrayList<>();
        for (CiJobDO ciJobDO : list) {
            voList.add(getCiJobVO(ciJobDO));
        }
        return voList;
    }

    @Override
    public CiJobVO getJob(long id) {
        CiJobDO ciJobDO = ciDao.getCiJob(id);
        return getCiJobVO(ciJobDO);
    }

    @Override
    public BusinessWrapper<Boolean> buildJob(long id) {
        CiJobDO ciJobDO = ciDao.getCiJob(id);
        CiJobVO ciJobVO = getCiJobVO(ciJobDO);
        return buildJob(ciJobVO);
    }


    /**
     * 新增部署记录
     */
    private void addDeployHistory(CiJobVO ciJobVO, CiBuildDO ciBuildDO) {
        if (ciJobVO.getCiType() == 0) return;
        CiBuildVO ciBuildVO = getBuildVO(ciBuildDO, TYPE_BUILD);
        List<ServerDO> servers = getHostPattern(ciBuildDO, ciJobVO.getCiAppVO().getServerGroupId());
        for (ServerDO serverDO : servers) {
            CiDeployHistoryDO ciDeployHistoryDO = new CiDeployHistoryDO(serverDO, ciJobVO, ciBuildVO);
            ciDao.addDeployHistory(ciDeployHistoryDO);
            // TODO 插入Deploy
            CiDeployDO ciDeployDO = new CiDeployDO(serverDO.getId(), ciDeployHistoryDO.getId());
            saveCiDeploy(ciDeployDO);
        }
    }


    @Override
    public List<ServerDO> getHostPattern(CiBuildDO ciBuildDO, long serverGroupId) {
        String hostPattern = getBuildParamValue(ciBuildDO, PARAM_HOSTPATTERN);
        if (StringUtils.isEmpty(hostPattern)) return new ArrayList<>();
        Map<String, List<ServerDO>> hostPatternMap = serverGroupService.getHostPatternMap(serverGroupId);
        return hostPatternMap.get(hostPattern);
    }


    /**
     * 查询某次构建的参数
     *
     * @param ciBuildDO
     * @param paramName
     * @return
     */
    @Override
    public String getBuildParamValue(CiBuildDO ciBuildDO, String paramName) {
        List<JobParam> parsmList = getBuildParams(ciBuildDO).getParams();
        String paramValue = "";
        for (JobParam jobParam : parsmList) {
            if (jobParam.getName().equals(paramName)) {
                paramValue = jobParam.getValue();
                break;
            }
        }
        // TODO 如果为空则取默认参数
        if (StringUtils.isEmpty(paramValue)) {
            List<CiJobParamDO> jobParamList = ciDao.queryJobParamByJobId(ciBuildDO.getJobId());
            for (CiJobParamDO ciJobParamDO : jobParamList) {
                if (ciJobParamDO.getParamName().equals(paramName)) {
                    paramValue = ciJobParamDO.getParamValue();
                    break;
                }
            }
        }
        return paramValue;
    }

    /**
     * 更新部署记录
     *
     * @param ciBuildDO
     * @param buildNotifyDO
     */
    private void updateDeployHistory(CiBuildDO ciBuildDO, BuildNotifyDO buildNotifyDO) {
        // TODO 完成才更新状态
        if (!buildNotifyDO.getBuildPhase().equals("FINALIZED")) return;

        CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        List<ServerDO> servers = getHostPattern(ciBuildDO, ciAppDO.getServerGroupId());
        for (ServerDO serverDO : servers) {
            CiDeployHistoryDO ciDeployHistoryDO = ciDao.getDeployHistoryByUnique(serverDO.getId(), ciBuildDO.getId());
            if (ciDeployHistoryDO == null)
                continue;
            // TODO 更新状态，包括队列的构建编号
            ciDeployHistoryDO.setBuildNumber(buildNotifyDO.getBuildNumber());
            ciDeployHistoryDO.setBuildPhase(buildNotifyDO.getBuildPhase());
            ciDeployHistoryDO.setBuildStatus(buildNotifyDO.getBuildStatus());
            ciDeployHistoryDO.setVersionName(ciBuildDO.getVersionName());
            ciDeployHistoryDO.setVersionDesc(ciBuildDO.getVersionDesc());
            ciDao.updateDeployHistory(ciDeployHistoryDO);
        }
    }

    private void saveCiDeploy(CiDeployDO ciDeployDO) {
        try {
            CiDeployDO deploy = ciDao.getDeployByServerId(ciDeployDO.getServerId());
            if (deploy == null) {
                ciDao.addDeploy(ciDeployDO);
            } else {
                deploy.setCiHistoryId(ciDeployDO.getCiHistoryId());
                ciDao.updateDeploy(deploy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 部分参数可选构建
     *
     * @param ciJobVO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> buildJob(CiJobVO ciJobVO) {
        // TODO 校验不通过
        if (!checkBuildJob(ciJobVO))
            return new BusinessWrapper<Boolean>(false);
        HashMap<String, String> params = getParams(ciJobVO);
        // TODO 检查Job是否存在，不存在按模版创建
        Job job = checkJob(ciJobVO.getJobName(), ciJobVO.getJobTemplate());
        if (job != null) {
            JobWithDetails jobWithDetails = jenkinsService.build(job, params);
            if (jobWithDetails != null) {
                CiBuildDO ciBuildDO = saveBuild(ciJobVO, params, jobWithDetails);
                // TODO 流水线插入Deploy记录
                addDeployHistory(ciJobVO, ciBuildDO);
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }

    /**
     * 发布任务
     *
     * @param ciJobVO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> deployJob(CiJobVO ciJobVO) {
        // TODO 校验不通过
        if (!checkBuildJob(ciJobVO))
            return new BusinessWrapper<Boolean>(false);
        HashMap<String, String> params = getParams(ciJobVO);
        BuildArtifactDO buildArtifactDO = ciDao.getBuildArtifact(ciJobVO.getDeployArtifactId());
        // TODO 插入 ossPath
        invokeDeployParams(buildArtifactDO, params);
        // TODO 检查Job是否存在，不存在按模版创建
        Job job = checkJob(ciJobVO.getDeployJobName(), ciJobVO.getDeployJobTemplate());
        if (job != null) {
            JobWithDetails jobWithDetails = jenkinsService.build(job, params);
            if (jobWithDetails != null) {

                CiBuildDO ciBuildDO = saveBuildByDeploy(ciJobVO, params, jobWithDetails, buildArtifactDO);
                // TODO 流水线插入Deploy记录
                addDeployHistory(ciJobVO, ciBuildDO);
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }

    private Job checkJob(String jobName, String templateName) {
        if (StringUtils.isEmpty(jobName)) return null;
        Job job = jenkinsService.getJob(jobName);
        if (job != null) return job;
        if (jenkinsService.createJobByTemplate(jobName, templateName))
            return jenkinsService.getJob(jobName);
        return null;
    }

    /**
     * 校验job合法性
     * 1.校验用户
     * 2.校验服务器分组信息
     *
     * @param checkJobVO
     */
    private boolean checkBuildJob(CiJobVO checkJobVO) {
        CiJobDO ciJobDO = ciDao.getCiJob(checkJobVO.getId());
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        // TODO 校验用户通过
        if (ciDao.checkCiAppAuth(userDO.getId(), ciJobDO.getAppId()) == 0) {
            if (!authService.isRole(username, RoleDO.roleAdmin))
                return false;
        }
        // TODO 校验服务器组
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        Map<String, List<ServerDO>> map = serverGroupService.getHostPatternMap(ciAppDO.getServerGroupId());
        // TODO 当前分组不存在
        if (!map.containsKey(checkJobVO.getHostPattern()))
            return false;

        CiJobVO ciJobVO = getCiJobVO(ciJobDO);
        ciJobVO.setBranch(checkJobVO.getBranch());
        ciJobVO.setHostPattern(checkJobVO.getHostPattern());
        ciJobVO.setVersionName(checkJobVO.getVersionName());
        ciJobVO.setVersionDesc(checkJobVO.getVersionDesc());
        checkJobVO = ciJobVO;
        return true;
    }

    private CiBuildDO saveBuild(CiJobVO ciJobVO, HashMap<String, String> params, JobWithDetails jobWithDetails) {
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        CiBuildDO ciBuildDO = new CiBuildDO(ciJobVO, userDO, getParams(ciJobVO, params).toString(), false);
        if (jobWithDetails.isInQueue()) {
            // TODO 设置为队列
            ciBuildDO.setBuildNumber(jobWithDetails.getNextBuildNumber());
        } else {
            ciBuildDO.setBuildNumber(jobWithDetails.getLastBuild().getNumber());
            // TODO 设置版本名称
            if (StringUtils.isEmpty(ciJobVO.getVersionName()))
                ciBuildDO.setVersionName(VERSION_NAME + ciBuildDO.getBuildNumber());
        }
        String branch = getBuildParamValue(ciBuildDO, this.PARAM_BRANCH);
        GitlabBranch gitlabBranch = gitlabService.getProjectBranch(ciJobVO.getCiAppVO().getProjectId(), branch);
        if (gitlabBranch == null) return ciBuildDO;

        String commit = gitlabBranch.getCommit().getId();
        ciBuildDO.setCommit(commit);
        ciDao.addBuild(ciBuildDO);
        saveBuildCommit(ciBuildDO, ciJobVO.getId(), ciJobVO.getJobName(), branch);
        return ciBuildDO;
    }

    /**
     * 记录Deploy构建任务
     *
     * @param ciJobVO
     * @param params
     * @param jobWithDetails
     * @return
     */
    private CiBuildDO saveBuildByDeploy(CiJobVO ciJobVO, HashMap<String, String> params, JobWithDetails jobWithDetails, BuildArtifactDO buildArtifactDO) {
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        // TODO 取构件的versionName
        CiBuildDO buildDO = ciDao.getBuild(buildArtifactDO.getBuildId());


        CiBuildDO ciBuildDO = new CiBuildDO(ciJobVO, userDO, getParams(ciJobVO, params).toString(), true);
        ciBuildDO.setVersionName(buildDO.getVersionName());
        ciBuildDO.setVersionDesc(buildDO.getVersionDesc());
        //
        if (jobWithDetails.isInQueue()) {
            // TODO 设置为队列
            ciBuildDO.setBuildNumber(jobWithDetails.getNextBuildNumber());
        } else {
            ciBuildDO.setBuildNumber(jobWithDetails.getLastBuild().getNumber());
            // TODO 设置版本名称
            if (StringUtils.isEmpty(ciJobVO.getVersionName()))
                ciBuildDO.setVersionName(VERSION_NAME + ciBuildDO.getBuildNumber());
        }
        String branch = getBuildParamValue(ciBuildDO, this.PARAM_BRANCH);
        GitlabBranch gitlabBranch = gitlabService.getProjectBranch(ciJobVO.getCiAppVO().getProjectId(), branch);
        if (gitlabBranch == null) return ciBuildDO;

        String commit = gitlabBranch.getCommit().getId();
        ciBuildDO.setCommit(commit);
        ciDao.addBuild(ciBuildDO);
        saveBuildCommit(ciBuildDO, ciJobVO.getId(), ciJobVO.getDeployJobName(), branch);
        return ciBuildDO;
    }

    // List<GitlabCommit>  getChanges(long jobId, String jobName,String branch);
    private boolean saveBuildCommit(CiBuildDO ciBuildDO, long jobId, String jobName, String branch) {
        try {
            List<GitlabCommit> commits = gitlabService.getChanges(jobId, jobName, branch);
            for (GitlabCommit gitlabCommit : commits) {
                CiBuildCommitDO ciBuildCommitDO = new CiBuildCommitDO(ciBuildDO.getId(), gitlabCommit);
                ciDao.addCiBuildCommit(ciBuildCommitDO);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private JobParams getParams(CiJobVO ciJobVO, HashMap<String, String> params) {
        for (CiJobParamDO ciJobParamDO : ciJobVO.getParamList()) {
            if (params.containsKey(ciJobParamDO.getParamName())) {
                if (params.get(ciJobParamDO.getParamName()).equals(ciJobParamDO.getParamValue()))
                    params.remove(ciJobParamDO.getParamName());
            }
        }
        List<JobParam> list = new ArrayList<>();
        for (String key : params.keySet()) {
            list.add(new JobParam(key, params.get(key)));
        }
        return new JobParams(list);
    }

    /**
     * 升级job(按模版版本)
     *
     * @param ciJobVO
     * @return
     */
    private boolean updateJob(CiJobVO ciJobVO) {
        // TODO 判断版本是否一致
        if (ciJobVO.getCiTemplateDO().getVersion() != ciJobVO.getJobVersion()) {
            boolean isUpdate = jenkinsService.updateJob(ciJobVO.getJobName(), ciJobVO.getJobTemplate());
            if (isUpdate) {
                ciJobVO.setJobVersion(ciJobVO.getCiTemplateDO().getVersion());
                ciDao.updateCiJob(ciJobVO);
            }
        }
        return true;
    }

    private HashMap<String, String> getParams(CiJobVO ciJobVO) {
        List<CiJobParamDO> paramList = ciJobVO.getParamList();
        HashMap<String, String> params = new HashMap<>();
        for (CiJobParamDO param : paramList) {
            params.put(param.getParamName(), param.getParamValue());
        }
        // TODO 如果没有branch则生成
        if (!params.containsKey(PARAM_BRANCH)) {
            if (!StringUtils.isEmpty(ciJobVO.getBranch()))
                params.put(PARAM_BRANCH, ciJobVO.getBranch());
        }
        if (!StringUtils.isEmpty(ciJobVO.getHostPattern())) {
            params.put(PARAM_HOSTPATTERN, ciJobVO.getHostPattern());
        }

        // TODO 生成sshUrl
        params.put(PARAM_SSHURL, ciJobVO.getCiAppVO().getSshUrl());
        return params;
    }

    /**
     * @param buildArtifactDO
     * @param paramMap
     */
    private void invokeDeployParams(BuildArtifactDO buildArtifactDO, HashMap<String, String> paramMap) {
        if (!StringUtils.isEmpty(buildArtifactDO.getOssPath()))
            paramMap.put(PARAM_OSS_PATH, buildArtifactDO.getOssPath());
    }

    private CiJobVO getCiJobVO(CiJobDO ciJobDO) {
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        // TODO gitlab分支
        List<GitlabBranch> branchList = gitlabService.getProjectBranchs(ciAppDO.getProjectId());
        // TODO 参数
        List<CiJobParamDO> paramList = ciDao.queryJobParamByJobId(ciJobDO.getId());
        CiJobVO ciJobVO = new CiJobVO(ciJobDO, branchList, paramList, getCiAppVO(ciAppDO));
        if (!StringUtils.isEmpty(ciJobDO.getJobTemplate())) {
            CiTemplateDO ciTemplateDO = ciDao.getTemplateByName(ciJobDO.getJobTemplate());
            ciJobVO.setCiTemplateDO(ciTemplateDO);
            ciJobVO.setJobTemplateMd5(jenkinsService.getJobXmlMd5(ciJobDO.getJobName()));
        }

        if (!StringUtils.isEmpty(ciJobDO.getDeployJobTemplate())) {
            CiTemplateDO ciTemplateDO = ciDao.getTemplateByName(ciJobDO.getDeployJobTemplate());
            ciJobVO.setCiDeployTemplateDO(ciTemplateDO);
            ciJobVO.setDeployJobTemplateMd5(jenkinsService.getJobXmlMd5(ciJobDO.getDeployJobName()));
        }
        return ciJobVO;
    }

    @Override
    public BusinessWrapper<CiJobVO> saveParam(CiJobParamDO ciJobParamDO) {
        try {
            CiJobDO ciJobDO = ciDao.getCiJob(ciJobParamDO.getJobId());
            if (ciJobParamDO.getId() == 0) {
                ciDao.addJobParam(ciJobParamDO);
            } else {
                ciDao.updateJobParam(ciJobParamDO);
            }
            return new BusinessWrapper<>(getCiJobVO(ciJobDO));
        } catch (Exception e) {
            e.printStackTrace();
            BusinessWrapper<CiJobVO> wrapper = new BusinessWrapper<CiJobVO>(new CiJobVO());
            wrapper.setSuccess(false);
            return wrapper;
        }
    }

    @Override
    public BusinessWrapper<CiJobVO> delParam(long id) {
        try {
            CiJobParamDO ciJobParamDO = ciDao.getJobParam(id);
            CiJobDO ciJobDO = ciDao.getCiJob(ciJobParamDO.getJobId());
            ciDao.delJobParam(id);
            return new BusinessWrapper<>(getCiJobVO(ciJobDO));
        } catch (Exception e) {
            e.printStackTrace();
            BusinessWrapper<CiJobVO> wrapper = new BusinessWrapper<CiJobVO>(new CiJobVO());
            wrapper.setSuccess(false);
            return wrapper;
        }
    }

    @Override
    public TableVO<List<CiTemplateVO>> getTemplatePage(String name, int appType, int ciType, int page, int length) {
        long size = ciDao.getTemplateSize(name, appType, ciType);
        List<CiTemplateDO> list = ciDao.getTemplatePage(name, appType, ciType, page * length, length);
        List<CiTemplateVO> voList = new ArrayList<>();
        for (CiTemplateDO ciTemplateDO : list) {
            // TODO 更新自己，转换字符
            jenkinsService.updateJob(ciTemplateDO.getName(), ciTemplateDO.getName());
            // TODO Check MD5
            String md5 = jenkinsService.getJobXmlMd5(ciTemplateDO.getName());
            if (!ciTemplateDO.getMd5().equals(md5)) {
                ciTemplateDO.setMd5(md5);
                ciTemplateDO.setVersion(ciTemplateDO.getVersion() + 1);
                ciDao.updateTemplate(ciTemplateDO);
            }
            voList.add(getCiTemplateVO(ciTemplateDO));
        }

        return new TableVO<>(size, voList);
    }

    private CiTemplateVO getCiTemplateVO(CiTemplateDO ciTemplateDO) {
        CiTemplateVO ciTemplateVO = BeanCopierUtils.copyProperties(ciTemplateDO, CiTemplateVO.class);
        //CiTemplateVO ciTemplateVO = new CiTemplateVO(ciTemplateDO);
        List<CiJobDO> list = ciDao.queryCiJobByJobTemplate(ciTemplateDO.getName());
        List<CiJobVO> voList = new ArrayList<>();
        for (CiJobDO ciJobDO : list)
            voList.add(getCiJobVO(ciJobDO));
        ciTemplateVO.setJobList(voList);

        List<CiJobDO> deployList = ciDao.queryCiJobByDeployJobTemplate(ciTemplateDO.getName());
        List<CiJobVO> voDeployList = new ArrayList<>();
        for (CiJobDO ciJobDO : deployList)
            voDeployList.add(getCiJobVO(ciJobDO));
        ciTemplateVO.setDeployJobList(voDeployList);

        return ciTemplateVO;
    }


    @Override
    public TableVO<List<CiBuildVO>> getBuildPage(long jobId, int page, int length) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        long size = ciDao.getBuildSize(jobId, ciJobDO.getJobName());

        List<CiBuildDO> list = ciDao.getBuildPage(jobId, ciJobDO.getJobName(), page * length, length);
        List<CiBuildVO> voList = new ArrayList<>();
        for (CiBuildDO ciBuildDO : list)
            voList.add(getBuildVO(ciBuildDO, TYPE_BUILD));
        return new TableVO<>(size, voList);
    }

    @Override
    public TableVO<List<CiBuildVO>> getDeployPage(long jobId, int page, int length) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        long size = ciDao.getBuildSize(jobId, ciJobDO.getDeployJobName());

        List<CiBuildDO> list = ciDao.getBuildPage(jobId, ciJobDO.getDeployJobName(), page * length, length);
        List<CiBuildVO> voList = new ArrayList<>();
        for (CiBuildDO ciBuildDO : list)
            voList.add(getBuildVO(ciBuildDO, TYPE_DEPLOY));
        return new TableVO<>(size, voList);
    }

    @Override
    public List<CiBuildVO> getArtifact(long jobId, int buildNumber) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        List<BuildArtifactDO> list = ciDao.queryBuildArtifactByJobName(ciJobDO.getJobName(), buildNumber);
        List<CiBuildVO> voList = new ArrayList<>();
        for (BuildArtifactDO buildArtifactDO : list) {
            CiBuildDO ciBuildDO = ciDao.getBuild(buildArtifactDO.getBuildId());
            voList.add(getBuildVO(ciBuildDO, TYPE_BUILD));
        }
        return voList;
    }

    @Override
    public List<GitlabCommit> getBuildCommit(long jobId, String jobName, String branch) {
        return gitlabService.getChanges(jobId, jobName, branch);
    }


    /**
     * @param ciBuildDO
     * @param buildType
     * @return
     */
    private CiBuildVO getBuildVO(CiBuildDO ciBuildDO, int buildType) {
        CiBuildVO ciBuildVO = new CiBuildVO(ciBuildDO);
        //JSONObject jsonObject = JSON.parseObject(ciBuildDO.getParameters());
        ciBuildVO.setJobParams(getBuildParams(ciBuildDO));
        // TODO notifyList
        List<BuildNotifyDO> notifyList = ciDao.queryBuildNotifyByBuildId(ciBuildDO.getId());
        ciBuildVO.setNotifyList(notifyList);
        // TODO console http://ci.domain.cn/job/java_opscloud_prod/112/console
        for (BuildNotifyDO buildNotifyDO : notifyList)
            if (!StringUtils.isEmpty(buildNotifyDO.getBuildFullUrl())) {
                ciBuildVO.setConsole(buildNotifyDO.getBuildFullUrl() + BUILD_CONSOLE);
                break;
            }
        // TODO artifactList
        ciBuildVO.setArtifactList(getArtifactList(ciBuildDO.getId(), buildType));

        // TODO commitList
        List<CiBuildCommitDO> commitList = ciDao.queryCiBuildCommitByBuildId(ciBuildDO.getId());
        ciBuildVO.setCommitList(commitList);
        // TODO badge icon
        String badgeIcon = "";
        for (BuildNotifyDO buildNotifyDO : notifyList) {
            if (!StringUtils.isEmpty(buildNotifyDO.getBuildFullUrl())) {
                badgeIcon = buildNotifyDO.getBuildFullUrl() + BUILD_PLAIN_LINK;
                break;
            }
        }
        if (StringUtils.isEmpty(badgeIcon)) {
            ciBuildVO.setBadgeIcon(badgeIcon);
        } else {
            ciBuildVO.setBadgeIcon(badgeIcon);
        }
        // TODO deployHistory
        List<CiDeployHistoryDO> serverList = ciDao.queryDeployHistoryByBuildId(ciBuildDO.getId());
        ciBuildVO.setServerList(serverList);
        return ciBuildVO;
    }

    private List<BuildArtifactVO> getArtifactList(long buildId, int buildType) {
        List<BuildArtifactDO> artifactList = new ArrayList<>();
        List<BuildArtifactVO> list = new ArrayList<>();
        if (buildType == TYPE_BUILD) {
            artifactList = ciDao.queryBuildArtifactByBuildId(buildId);
        } else {
            CiBuildDO ciBuildDO = ciDao.getBuild(buildId);
            String ossPath = getBuildParamValue(ciBuildDO, PARAM_OSS_PATH);
            if (!StringUtils.isEmpty(ossPath)) {
                artifactList = ciDao.queryBuildArtifactByOssPath(ossPath);
            } else {
                return list;
            }
        }
        for (BuildArtifactDO buildArtifactDO : artifactList) {
            BuildArtifactVO vo = BeanCopierUtils.copyProperties(buildArtifactDO, BuildArtifactVO.class);
            vo.setSize(ByteUtils.getSize(buildArtifactDO.getArtifactSize()));
            list.add(vo);
        }
        return list;
    }

    private JobParams getBuildParams(CiBuildDO ciBuildDO) {
        Gson gson = new GsonBuilder().create();
        // JSON.toJSONString(ciBuildDO.getParameters())
        JobParams jobParams = gson.fromJson(ciBuildDO.getParameters(), JobParams.class);
        return jobParams;
    }


    @Override
    public BusinessWrapper<CiTemplateDO> saveTemplate(CiTemplateDO ciTemplateDO) {
        try {
            String jobXml = jenkinsService.getJobXml(ciTemplateDO.getName());
            String templateMd5 = EncryptionUtil.md5(jobXml);
            if (ciTemplateDO.getId() == 0) {
                ciTemplateDO.setMd5(templateMd5);
                ciDao.addTemplate(ciTemplateDO);
            } else {
                if (StringUtils.isEmpty(ciTemplateDO.getMd5()) || !ciTemplateDO.getMd5().equals(templateMd5)) {
                    ciTemplateDO.setMd5(templateMd5);
                    ciTemplateDO.setVersion(ciTemplateDO.getVersion() + 1);
                }
                ciDao.updateTemplate(ciTemplateDO);
            }
            return new BusinessWrapper<CiTemplateDO>(ciTemplateDO);
        } catch (Exception e) {
            e.printStackTrace();
            BusinessWrapper<CiTemplateDO> wrapper = new BusinessWrapper<>(new CiTemplateDO());
            wrapper.setSuccess(false);
            return wrapper;
        }
    }

    @Override
    public BusinessWrapper<Boolean> updateTemplate(long jobId, int type) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        if (ciJobDO == null) return new BusinessWrapper<Boolean>(false);
        return new BusinessWrapper<Boolean>(updateJobTemplate(ciJobDO, type));
    }

    private boolean updateJobTemplate(CiJobDO ciJobDO, int type) {

        if (type == 0) {
            // TODO Check
            String templateMd5 = jenkinsService.getJobXmlMd5(ciJobDO.getJobTemplate());
            if (StringUtils.isEmpty(templateMd5)) return false;
            String jobMd5 = jenkinsService.getJobXmlMd5(ciJobDO.getJobName());
            if (StringUtils.isEmpty(jobMd5)) return false;
            // TODO Job为最新模版，无需更新
            if (jobMd5.equals(templateMd5)) return false;
            return jenkinsService.updateJob(ciJobDO.getJobName(), ciJobDO.getJobTemplate());
        }

        if (type == 1) {
            // TODO Check
            String templateMd5 = jenkinsService.getJobXmlMd5(ciJobDO.getDeployJobTemplate());
            if (StringUtils.isEmpty(templateMd5)) return false;
            String jobMd5 = jenkinsService.getJobXmlMd5(ciJobDO.getDeployJobName());
            if (StringUtils.isEmpty(jobMd5)) return false;
            // TODO Job为最新模版，无需更新
            if (jobMd5.equals(templateMd5)) return false;
            return jenkinsService.updateJob(ciJobDO.getDeployJobName(), ciJobDO.getDeployJobTemplate());
        }

        return false;
    }

    @Override
    public BusinessWrapper<Boolean> updateTemplates(long id) {
        CiTemplateDO ciTemplateDO = ciDao.getTemplate(id);
        if (ciTemplateDO == null) return new BusinessWrapper<Boolean>(false);
        List<CiJobDO> jobList = ciDao.queryCiJobByJobTemplate(ciTemplateDO.getName());
        boolean reslut = true;
        for (CiJobDO ciJobDO : jobList) {
            updateJobTemplate(ciJobDO, 0);
            updateJobTemplate(ciJobDO, 1);
        }
        return new BusinessWrapper<Boolean>(reslut);
    }


    @Override
    public BusinessWrapper<Boolean> delTemplate(long id) {
        try {
            ciDao.delTemplate(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public JenkinsVersion getJenkinsVersion() {
        return jenkinsService.version();
    }

    @Override
    public List<Job> getJenkinsTemplate() {
        Map<String, Job> jobMap = jenkinsService.getJobs();

        List<Job> jobList = new ArrayList<>();
        for (String key : jobMap.keySet()) {
            if (key.indexOf(TEMPLATE) == 0) {
                CiTemplateDO ciTemplateDO = ciDao.getTemplateByName(key);
                if (ciTemplateDO == null)
                    jobList.add(jobMap.get(key));
            }
        }
        return jobList;
    }


    /**
     * Ci专用，有版本信息
     *
     * @param groupId
     * @return
     */
    @Override
    public List<HostPatternCi>  getHostPatternCi(long groupId) {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(groupId);
        Map<String, List<ServerDO>> map = ansibleService.grouping(serverGroupDO, true);
        List<HostPatternCi> hostPatterns = new ArrayList<>();
        for (String key : map.keySet()) {
            List<ServerDO> servers = map.get(key);
            List<CiDeployHistoryVO> deployHistoryList = getDeployHistoryList(servers);
            deployHistoryList.sort(Comparator.naturalOrder());
            hostPatterns.add(new HostPatternCi(key, deployHistoryList));
        }
        return hostPatterns;
    }

    private List<CiDeployHistoryVO> getDeployHistoryList(List<ServerDO> servers) {
        List<CiDeployHistoryVO> list = new ArrayList<>();
        for (ServerDO serverDO : servers) {
            list.add(getCiDeployHistory(serverDO));
        }
        return list;

    }

    private CiDeployHistoryVO getCiDeployHistory(ServerDO serverDO) {
        CiDeployDO ciDeployDO = ciDao.getDeployByServerId(serverDO.getId());
        if (ciDeployDO == null) return new CiDeployHistoryVO(serverDO);
        CiDeployHistoryDO ciDeployHistoryDO = ciDao.getDeployHistory(ciDeployDO.getCiHistoryId());
        if (ciDeployHistoryDO == null) return new CiDeployHistoryVO(serverDO);
        CiBuildDO ciBuildDO = ciDao.getBuild(ciDeployHistoryDO.getBuildId());
        return new CiDeployHistoryVO(ciDeployHistoryDO, ciBuildDO);
    }

}
