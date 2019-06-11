package com.sdg.cmdb.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.oss.model.OSSObjectSummary;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.offbytwo.jenkins.helper.JenkinsVersion;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.PublicType;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.ci.*;
import com.sdg.cmdb.domain.ci.android.AndroidChannel;
import com.sdg.cmdb.domain.ci.jenkins.Notify;

import com.sdg.cmdb.domain.ci.jobParametersYaml.JobParameterYaml;
import com.sdg.cmdb.domain.ci.jobParametersYaml.JobParametersYaml;
import com.sdg.cmdb.domain.dingtalk.DingtalkDO;


import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
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
import org.yaml.snakeyaml.Yaml;

import java.util.*;

/**
 * Created by liangjian on 2017/2/17.
 */
@Service
public class CiServiceImpl implements CiService {

    private static final Logger logger = LoggerFactory.getLogger(CiServiceImpl.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private GitlabService gitlabService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private JenkinsService jenkinsService;

    @Autowired
    private CiDao ciDao;

    @Autowired
    private DingtalkDao dingtalkDao;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Autowired
    private ServerGroupService serverGroupService;

    @Autowired
    private DingtalkService dingtalkService;

    @Autowired
    private SchedulerManager schedulerManager;

    @Autowired
    private AnsibleFileProcessorService ansibleService;

    @Autowired
    private GitlabDao gitlabDao;

    @Autowired
    private AliyunOssService aliyunOssService;

    static public final String PARAM_BRANCH = "branch";
    static public final String PARAM_HOSTPATTERN = "hostPattern";
    static public final String PARAM_SSHURL = "sshUrl";
    static public final String PARAM_ARTIFACT_PATH = "artifactPath";
    static public final String PARAM_PROJECT_RECIPIENT_LIST = "projectRecipientList";
    static public final String PARAM_CHANNEL_TYPE = "channelType";
    static public final String PARAM_CHANNEL_GROUP = "channelGroup";

    // TODO Deploy
    static public final String PARAM_OSS_PATH = "ossPath";

    static public final String TEMPLATE = "tpl_";

    static public final String BUILD_CONSOLE = "console";

    static public final String BUILD_TEST_JACOCO = "jacoco";

    static public final String BUILD_TEST_NGREPORTS = "testngreports";

    static public final String BUILD_PLAIN_LINK = "badge/icon";

    static public final String VERSION_NAME = "release-";

    static public final String PKG_SERVER_URL = "https://pkg.ops.yangege.cn/";

    // TODO 构建图标网站 https://shields.io/#/examples/build
    public static final String BUILD_PASSING = "https://img.shields.io/badge/build-passing-brightgreen.svg";
    public static final String BUILD_FAILING = "https://img.shields.io/badge/build-failing-red.svg";
    public static final String BUILD_STARTED = "https://img.shields.io/badge/build-started-orange.svg";

    public static final String BUILD_TEST_PASSING = "https://img.shields.io/badge/test-passing-brightgreen.svg";
    public static final String BUILD_TEST_FAILING = "https://img.shields.io/badge/test-failing-red.svg";
    public static final String BUILD_TEST_STARTED = "https://img.shields.io/badge/test-started-green.svg";
    public static final String BUILD_TEST_UNSTABLE = "https://img.shields.io/badge/test-unstable-yellow.svg";

    public static final String BADGE_URL = "https://img.shields.io/badge/";

    // TODO buildType
    static public final int TYPE_BUILD = 0;
    static public final int TYPE_DEPLOY = 1;

    @Override
    public BusinessWrapper<Boolean> buildNotify(Notify notify) {
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
            // 更新 buildPhase/buildStatus 如果是队列则更新buildNumber
            BuildNotifyDO buildNotifyDO = new BuildNotifyDO(ciBuildDO.getId(), notify);
            try {
                BuildNotifyDO checkNotify = ciDao.getBuildNotifyByUnique(buildNotifyDO);
                if (checkNotify != null)
                    ciDao.delBuildNotify(checkNotify.getId());
                ciDao.addBuildNotify(buildNotifyDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (buildNotifyDO.getBuildPhase().equals("FINALIZED")) // 记录Artifacts
                saveBuildArtifacts(notify, ciBuildDO);
            updateDeployHistory(ciBuildDO, buildNotifyDO); // 更新DeployHistory
            notify(ciBuildDO, buildNotifyDO, isDeploy); // 触发通知
        });
        return new BusinessWrapper<>(true);
    }

    @Override
    public CiAppDO getCiAppByBuildId(long buildId) {
        CiBuildDO ciBuildDO = ciDao.getBuild(buildId);
        return getCiAppByJobId(ciBuildDO.getJobId());
    }

    @Override
    public CiAppDO getCiAppByJobId(long jobId) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        return ciDao.getCiApp(ciJobDO.getAppId());
    }

    public void autoBuild(GitlabWebHooksDO gitlabWebHooksDO) {
        schedulerManager.registerJob(() -> {
            String branch = gitlabWebHooksDO.getRef().replace(GitlabServiceImpl.BRANCH_REF, "");
            List<CiJobDO> jobs = ciDao.queryCiJobByAutoBuild(gitlabWebHooksDO.getProjectName(), branch);
            for (CiJobDO ciJobDO : jobs) {
                buildJob(ciJobDO.getId());
                gitlabWebHooksDO.setTriggerBuild(true);
                gitlabWebHooksDO.setJobName(ciJobDO.getJobName());
                gitlabDao.updateWebHooks(gitlabWebHooksDO);
            }
        });
    }


    /**
     * 刷新任务详情（Notify丢失）
     *
     * @param buildId
     * @return
     */
    public BusinessWrapper<Boolean> refreshBuildDetails(long buildId) {

        JobWithDetails jd = jenkinsService.getJobDetails("pipeline_oc_prod");
        Build build = jd.getBuildByNumber(5);
        try {
            BuildWithDetails bd = build.details();
            System.err.println(JSON.toJSONString(bd));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
            CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
            switch (ciAppDO.getAppType()) {
                case 0: //Java
                    dingtalkService.notifyCi(ciBuildDO, buildNotifyDO);
                    break;
                case 1: //Python
                    //
                case 2: //iOS
                    //
                case 3: //Android
                    dingtalkService.notifyCiAndroid(ciBuildDO, buildNotifyDO);
                    break;
                case 4: //Test
                    dingtalkService.notifyCiTest(ciBuildDO, buildNotifyDO);
                    break;
                default:
                    break;
            }
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
            // 任务插入默认版本号(Deploy不插入)
            if (!isDeploy && StringUtils.isEmpty(ciBuildDO.getVersionName())) {
                if (ciBuildDO.getBuildNumber() > 0)
                    ciBuildDO.setVersionName(VERSION_NAME + ciBuildDO.getBuildNumber());
            }

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
        if (ciBuildDO == null) return;
        Map<String, HashMap<String, String>> artifacts = notify.getBuild().getArtifacts();
        for (String artifactsName : artifacts.keySet()) {
            HashMap<String, String> artifact = artifacts.get(artifactsName);
            if (artifact.containsKey("archive")) {
                String archiveUrl = artifact.get("archive");
                if (StringUtils.isEmpty(archiveUrl)) return;
                BuildArtifactDO buildArtifactDO = new BuildArtifactDO(ciBuildDO.getId(), artifactsName, archiveUrl);
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
    public OSSObjectSummary getOssObject(CiBuildDO ciBuildDO, BuildArtifactDO buildArtifactDO) {
        try {
            String artifactPath = getBuildParamValue(ciBuildDO, PARAM_ARTIFACT_PATH);
            String artifactName = buildArtifactDO.getArtifactName().replace(artifactPath, "");
            String ossPath = getOssCiTypeName(ciBuildDO) + ciBuildDO.getJobName() + "/" + ciBuildDO.getBuildNumber() + "/" + artifactName;
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

    /**
     * 获取OSS中App类型名称
     *
     * @param ciBuildDO
     * @return
     */
    private String getOssCiTypeName(CiBuildDO ciBuildDO) {
        CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        if (ciAppDO.getAppType() == 0) return "ci/";
        return PublicType.CiAppTypeEnum.getCiAppTypeName(ciAppDO.getAppType()) + "/";
    }

    @Override
    public CiAppVO getApp(long id) {
        CiAppDO ciAppDO = ciDao.getCiApp(id);
        return getCiAppVO(ciAppDO);
    }

    @Override
    public List<CiAppVO> queryMyApp(String queryName, long labelId) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        // 管理员可查看所有App
        if (authService.isRole(username, RoleDO.roleAdmin))
            userDO.setId(0);
        List<CiAppDO> list = ciDao.getCiAppByAuthUserId(userDO.getId(), labelId, queryName, -1);
        List<CiAppVO> voList = new ArrayList<>();
        for (CiAppDO ciAppDO : list)
            voList.add(getCiAppVO(ciAppDO));
        return voList;
    }

    @Override
    public List<CiAppDO> queryAppByLabel(long labelId, String queryName) {
        List<CiAppDO> list = ciDao.queryAppByLabel(labelId, queryName);
        return list;
    }

    /**
     * 查询未授权应用
     *
     * @param queryName
     * @return
     */
    @Override
    public List<CiAppVO> queryUnauthApp(String queryName, int appType) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        List<CiAppDO> list = ciDao.getCiAppUnauthUserId(userDO.getId(), queryName, appType);
        List<CiAppVO> voList = new ArrayList<>();
        for (CiAppDO ciAppDO : list)
            voList.add(getCiAppVO(ciAppDO));
        return voList;
    }

    @Override
    public List<CiAppVO> queryMyAppByType(String queryName, int appType) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        // 管理员可查看所有App
        if (authService.isRole(username, RoleDO.roleAdmin))
            userDO.setId(0);
        List<CiAppDO> list = ciDao.getCiAppByAuthUserId(userDO.getId(), 0, queryName, appType);
        List<CiAppVO> voList = new ArrayList<>();
        for (CiAppDO ciAppDO : list)
            voList.add(getCiAppVO(ciAppDO));
        return voList;
    }

    @Override
    public CiAppVO queryAppByName(String appName) {
        return getCiAppVO(ciDao.getCiAppByName(appName));
    }

    @Override
    public List<CiAppVO> queryUserApp(long userId) {
        UserDO userDO = userDao.getUserById(userId);
        // 管理员可查看所有App
        if (authService.isRole(userDO.getUsername(), RoleDO.roleAdmin))
            userDO.setId(0);
        List<CiAppDO> list = ciDao.getCiAppByAuthUserId(userDO.getId(), 0, "", -1);
        List<CiAppVO> voList = new ArrayList<>();
        for (CiAppDO ciAppDO : list)
            voList.add(getCiAppVO(ciAppDO));
        return voList;

    }


    /**
     * 需要更新所有Job属性
     *
     * @param ciAppVO
     * @return
     */
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
                UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
                CiAppAuthDO authDO = new CiAppAuthDO(ciAppDO, userDO);
                ciAppAuth(authDO);
            } else {
                ciDao.updateCiApp(ciAppDO);
            }
            saveAppJobs(ciAppDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public boolean ciAppAuth(CiAppAuthDO authDO) {
        try {
            ciDao.addCiAppAuth(authDO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public BusinessWrapper<Boolean> delApp(long ciAppId) {
        List<CiJobDO> list = ciDao.getCiJobByAppId(ciAppId);
        if (list.size() != 0)
            return new BusinessWrapper<Boolean>(ErrorCode.ciAppJobsUndeleted);
        ciDao.delCiApp(ciAppId);
        return new BusinessWrapper<Boolean>(true);

    }

    private void saveAppJobs(CiAppDO ciAppDO) {
        List<CiJobDO> jobs = ciDao.getCiJobByAppId(ciAppDO.getId());
        for (CiJobDO ciJobDO : jobs) {
            ciJobDO.setCiType(ciAppDO.getCiType());
            ciDao.updateCiJob(ciJobDO);
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
        ciAppVO.setAuthUserList(userService.queryUserByApp(ciAppDO.getId()));
        return ciAppVO;
    }

    @Override
    public CiJobVO saveJob(CiJobVO ciJobVO) {
        try {
            if (ciJobVO.getId() == 0) {
                ciDao.addCiJob(ciJobVO);
            } else {
                ciDao.updateCiJob(ciJobVO);
            }
            return getCiJobVO(ciJobVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new CiJobVO();
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveJobParams(CiJobVO ciJobVO) {
        try {

            JobParametersYaml jobParams = getYaml(ciJobVO);
            if (!delJobParams(ciJobVO.getId()))
                return new BusinessWrapper<Boolean>(false);
            if (!addJobParams(ciJobVO.getId(), jobParams))
                return new BusinessWrapper<Boolean>(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(true);
    }

    private JobParametersYaml getYaml(CiJobVO ciJobVO) {
        Yaml yaml = new Yaml();
        Object paramsYaml = yaml.load(ciJobVO.getParamsYaml());
        ciDao.updateCiJobParamsYaml(ciJobVO);

        Gson gson = new GsonBuilder().create();
        JobParametersYaml jobYaml = gson.fromJson(JSON.toJSONString(paramsYaml), JobParametersYaml.class);
        return jobYaml;
    }

    private boolean addJobParams(long jobId, JobParametersYaml jobParams) {
        try {
            for (JobParameterYaml param : jobParams.getParameters()) {
                CiJobParamDO ciJobParamDO = new CiJobParamDO(jobId, param);
                ciDao.addJobParam(ciJobParamDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 清除Job所有参数
     *
     * @param jobId
     * @return
     */
    private boolean delJobParams(long jobId) {
        try {
            List<CiJobParamDO> list = ciDao.queryJobParamByJobId(jobId);
            for (CiJobParamDO ciJobParamDO : list)
                ciDao.delJobParam(ciJobParamDO.getId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public BusinessWrapper<Boolean> createJob(long jobId) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        if (!StringUtils.isEmpty(ciJobDO.getJobTemplate())) {
            Job job = jenkinsService.getJob(ciJobDO.getJobName());
            if (job == null){
                if (jenkinsService.createJobByTemplate(ciJobDO.getJobName(), ciJobDO.getJobTemplate(),getYaml(   getCiJobVO(ciJobDO))))
                    return new BusinessWrapper<Boolean>(false);
            }

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
        for (CiJobDO ciJobDO : list)
            voList.add(getCiJobVO(ciJobDO));
        return voList;
    }

    @Override
    public DeployInfo queryDeployInfo(long appId) {
        CiAppDO ciAppDO = ciDao.getCiApp(appId);
        if (ciAppDO.getServerGroupId() == 0) return new DeployInfo();
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ciAppDO.getServerGroupId());
        Map<String, List<ServerDO>> hostPatternMap = serverGroupService.getHostPatternFilterMap(serverGroupDO.getId());
        Map<String, String> uniqueDataKey = new HashMap<>();
        JSONArray versionData = new JSONArray();
        String defVersion = "def";
        for (String hostPattern : hostPatternMap.keySet()) {
            List<ServerDO> serverList = hostPatternMap.get(hostPattern);
            for (ServerDO serverDO : serverList) {
                String env = EnvType.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType());
                uniqueDataKey.put(ciAppDO.getAppName() + ":" + env, "");  // 应用/环境 （顶层数据 ）
                uniqueDataKey.put(env + ":" + hostPattern, "");           // 环境/分组
                CiDeployHistoryVO ciDeployHistoryVO = getCiDeployHistory(serverDO);
                String version = defVersion;
                if (ciDeployHistoryVO != null && !StringUtils.isEmpty(ciDeployHistoryVO.getVersionName()))
                    version = ciDeployHistoryVO.getVersionName();
                uniqueDataKey.put(hostPattern + ":" + version, ""); // 分组/版本
                uniqueDataKey.put(version + ":" + serverDO.acqServerName(), ""); // 版本/服务器
            }
        }

        for (String key : uniqueDataKey.keySet())
            invokeVersionData(versionData, key);
        DeployInfo info = new DeployInfo(ciAppDO.getAppName(), versionData);
        return info;
    }

    private void invokeVersionData(JSONArray versionData, String data) {
        JSONArray arrayObj = new JSONArray();
        String[] x = data.split(":");
        arrayObj.add(x[0]);
        arrayObj.add(x[1]);
        versionData.add(arrayObj);
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

    @Override
    public BusinessWrapper<Boolean> delJob(long id) {
        // 清理Job相关表数据
        CiJobDO ciJobDO = ciDao.getCiJob(id);
        // 清理Build相关数据
        if (!delBuilds(ciJobDO))
            return new BusinessWrapper<Boolean>(false);
        if (!delJobParams(ciJobDO.getId()))
            return new BusinessWrapper<Boolean>(false);
        ciDao.delCiJob(id);
        return new BusinessWrapper<Boolean>(true);
    }


    private boolean delBuilds(CiJobDO ciJobDO) {
        List<CiBuildDO> buildList = ciDao.getBuildByJobId(ciJobDO.getId());
        // 清理build相关数据
        for (CiBuildDO ciBuildDO : buildList)
            if (!delBuildDetails(ciBuildDO)) return false;
        return true;
    }

    private boolean delBuildDetails(CiBuildDO ciBuildDO) {
        try {
            // oc_ci_build_artifact
            List<BuildArtifactDO> buildArtifactList = ciDao.queryBuildArtifactByBuildId(ciBuildDO.getId());
            for (BuildArtifactDO buildArtifactDO : buildArtifactList)
                ciDao.delBuildArtifact(buildArtifactDO.getId());
            // oc_ci_build_commit
            List<CiBuildCommitDO> ciBuildCommitList = ciDao.queryCiBuildCommitByBuildId(ciBuildDO.getId());
            for (CiBuildCommitDO ciBuildCommitDO : ciBuildCommitList)
                ciDao.delCiBuildCommit(ciBuildCommitDO.getId());
            // oc_ci_build_notify
            List<BuildNotifyDO> buildNotifyList = ciDao.queryBuildNotifyByBuildId(ciBuildDO.getId());
            for (BuildNotifyDO buildNotifyDO : buildNotifyList)
                ciDao.delBuildNotify(buildNotifyDO.getId());
            // oc_ci_build
            ciDao.delBuild(ciBuildDO.getId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 新增部署记录
     */
    private void addDeployHistory(CiJobVO ciJobVO, CiBuildDO ciBuildDO) {
        if (ciJobVO.getCiType() == 0) return;
        CiBuildVO ciBuildVO = getBuildVO(ciJobVO, ciBuildDO);
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
        // 校验
        BusinessWrapper<Boolean> wrapper = checkBuildJob(ciJobVO);
        if (!wrapper.isSuccess())
            return wrapper;
        HashMap<String, String> params = getParams(ciJobVO);
        //  检查Job是否存在，不存在按模版创建
        Job job = checkJob(ciJobVO, ciJobVO.getJobName(), ciJobVO.getJobTemplate());
        if (isJobInQueue(ciJobVO.getJobName()))
            return new BusinessWrapper<Boolean>(false);
        if (job != null) {
            JobWithDetails jobWithDetails = jenkinsService.build(job, params);
            if (jobWithDetails != null) {
                CiBuildDO ciBuildDO = saveBuild(ciJobVO, params, jobWithDetails);
                //  流水线插入Deploy记录
                addDeployHistory(ciJobVO, ciBuildDO);
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }


    private boolean isJobInQueue(String jobName) {
        try {
            JobWithDetails jd = jenkinsService.getJobDetails(jobName);
            return jd.isInQueue();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 发布任务
     *
     * @param ciJobVO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> deployJob(CiJobVO ciJobVO) {
        // 校验
        BusinessWrapper<Boolean> wrapper = checkBuildJob(ciJobVO);
        if (!wrapper.isSuccess())
            return wrapper;
        HashMap<String, String> params = getParams(ciJobVO);
        BuildArtifactDO buildArtifactDO = ciDao.getBuildArtifact(ciJobVO.getDeployArtifactId());
        // 插入 ossPath
        invokeDeployParams(buildArtifactDO, params);
        // 检查Job是否存在，不存在按模版创建
        Job job = checkJob(ciJobVO, ciJobVO.getDeployJobName(), ciJobVO.getDeployJobTemplate());
        if (job != null) {
            JobWithDetails jobWithDetails = jenkinsService.build(job, params);
            if (jobWithDetails != null) {
                CiBuildDO ciBuildDO = saveBuildByDeploy(ciJobVO, params, jobWithDetails, buildArtifactDO);
                // 流水线插入Deploy记录
                addDeployHistory(ciJobVO, ciBuildDO);
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }

    private Job checkJob(CiJobVO ciJobVO, String jobName, String templateName) {
        JobParametersYaml jobYaml = getYaml(ciJobVO);
        if (StringUtils.isEmpty(jobName)) return null;
        Job job = jenkinsService.getJob(jobName);
        if (job != null) return job;
        if (jenkinsService.createJobByTemplate(jobName, templateName,jobYaml))
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
    private BusinessWrapper<Boolean> checkBuildJob(CiJobVO checkJobVO) {
        CiJobDO ciJobDO = ciDao.getCiJob(checkJobVO.getId());
        if (!StringUtils.isEmpty(SessionUtils.getUsername())) {
            String username = SessionUtils.getUsername();
            UserDO userDO = userDao.getUserByName(username);
            // 校验用户通过
            if (ciDao.checkCiAppAuth(userDO.getId(), ciJobDO.getAppId()) == 0) {
                if (!authService.isRole(username, RoleDO.roleAdmin))
                    return new BusinessWrapper<Boolean>(ErrorCode.ciBuildJobAuthFailure);
            }
        }

        // 校验服务器组
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        if (ciAppDO.getCiType() == PublicType.CiTypeEnum.cicd.getCode()) {
            Map<String, List<ServerDO>> map = serverGroupService.getHostPatternMap(ciAppDO.getServerGroupId());
            // 当前分组不存在
            if (!map.containsKey(checkJobVO.getHostPattern()))
                return new BusinessWrapper<Boolean>(ErrorCode.ciBuildJobHostPatternNotExist);
        }
        CiJobVO ciJobVO = getCiJobVO(ciJobDO);
        ciJobVO.setBranch(checkJobVO.getBranch());
        if (ciAppDO.getCiType() == PublicType.CiTypeEnum.cd.getCode())
            ciJobVO.setHostPattern(checkJobVO.getHostPattern());
        ciJobVO.setVersionName(checkJobVO.getVersionName());
        ciJobVO.setVersionDesc(checkJobVO.getVersionDesc());
        return new BusinessWrapper<Boolean>(true);
    }

    private CiBuildDO saveBuild(CiJobVO ciJobVO, HashMap<String, String> params, JobWithDetails jobWithDetails) {
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        CiBuildDO ciBuildDO = new CiBuildDO(ciJobVO, userDO, getParams(ciJobVO, params).toString(), false);
        invokeBuild(ciBuildDO, ciJobVO, jobWithDetails);
        String branch = getBuildParamValue(ciBuildDO, this.PARAM_BRANCH);
        GitlabBranch gitlabBranch = gitlabService.getProjectBranch(ciJobVO.getCiAppVO().getProjectId(), branch);
        if (gitlabBranch == null) return ciBuildDO;
        String commit = gitlabBranch.getCommit().getId();
        ciBuildDO.setCommit(commit);
        ciDao.addBuild(ciBuildDO);
        saveBuildCommit(ciBuildDO, ciJobVO.getId(), ciJobVO.getJobName(), branch);
        return ciBuildDO;
    }

    private void invokeBuild(CiBuildDO ciBuildDO, CiJobVO ciJobVO, JobWithDetails jobWithDetails) {
        if (!jobWithDetails.isInQueue()) {
            ciBuildDO.setBuildNumber(jobWithDetails.getNextBuildNumber());
            // 设置版本名称
            if (StringUtils.isEmpty(ciJobVO.getVersionName()))
                ciBuildDO.setVersionName(VERSION_NAME + ciBuildDO.getBuildNumber());
        } else {
            ciBuildDO.setBuildNumber(-1); // 设置为队列
        }
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
        CiBuildDO buildDO = ciDao.getBuild(buildArtifactDO.getBuildId());
        CiBuildDO ciBuildDO = new CiBuildDO(ciJobVO, userDO, getParams(ciJobVO, params).toString(), true);
        ciBuildDO.setVersionName(buildDO.getVersionName());
        ciBuildDO.setVersionDesc(buildDO.getVersionDesc());
        invokeBuild(ciBuildDO, ciJobVO, jobWithDetails);
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
        // 用户选择分支优先
        if (!StringUtils.isEmpty(ciJobVO.getBranch()))
            params.put(PARAM_BRANCH, ciJobVO.getBranch());

        // 如果有项目邮件列表则生成
        if (params.containsKey(PARAM_PROJECT_RECIPIENT_LIST)) {
            String projectRecipientList = getProjectRecipientList(ciJobVO.getAppId());
            if (StringUtils.isEmpty(projectRecipientList)) {
                params.remove(PARAM_PROJECT_RECIPIENT_LIST); //空邮件列表则删除
            } else {
                params.put(PARAM_PROJECT_RECIPIENT_LIST, projectRecipientList);
            }
        }
        if (!StringUtils.isEmpty(ciJobVO.getHostPattern())) {
            params.put(PARAM_HOSTPATTERN, ciJobVO.getHostPattern());
        }
        // 多渠道
        if (ciJobVO.getChannelType() == 1) {
            params.put(PARAM_CHANNEL_TYPE, "1");
            params.put(PARAM_CHANNEL_GROUP, getChannelGroup(ciJobVO.getChannelGroup()));
        }

        // 生成sshUrl
        params.put(PARAM_SSHURL, ciJobVO.getCiAppVO().getSshUrl());
        invokeParamByAppType(ciJobVO, params); // 按构建类型处理参数
        return params;
    }

    private String getChannelGroup(List<AndroidChannel> channelGroup) {
        String groupName = "";
        for (AndroidChannel androidChannel : channelGroup) {
            String name = PublicType.CiAndroidChannelTypeEnum.getCiAndroidChannelTypeName(androidChannel.getCode());
            if (name.equals("undefined")) continue;
            groupName += "./sign/*_" + name + "_sign.apk ";
        }
        return groupName;
    }

    private void invokeParamByAppType(CiJobVO ciJobVO, HashMap<String, String> params) {
        // Android
        if (ciJobVO.getCiAppVO().getAppType() == PublicType.CiAppTypeEnum.android.getCode()) {
            if (ciJobVO.getAndroidBuild() == null) return;
            AndroidBuild ab = ciJobVO.getAndroidBuild();
            ab.processor(params);
            return;
        }
    }

    private String getProjectRecipientList(long appId) {
        String projectRecipientList = "";
        List<UserVO> userList = userService.queryUserByApp(appId);
        for (UserVO userVO : userList) {
            if (StringUtils.isEmpty(userVO.getMail()))
                continue;
            if (StringUtils.isEmpty(projectRecipientList)) {
                projectRecipientList += userVO.getMail();
            } else {
                projectRecipientList += "," + userVO.getMail();
            }
        }
        return projectRecipientList;
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
        List<GitlabBranch> branchList;
        // gitlab分支(按GitFlow)
        if (ciAppDO.isGitFlow()) {
            branchList = gitlabService.getProjectBranchsByGitFlow(ciAppDO.getProjectId(), ciJobDO.getEnvType());
        } else {
            branchList = gitlabService.getProjectBranchs(ciAppDO.getProjectId());
        }
        // Params
        List<CiJobParamDO> paramList = ciDao.queryJobParamByJobId(ciJobDO.getId());
        CiJobVO ciJobVO = new CiJobVO(ciJobDO, branchList, paramList, getCiAppVO(ciAppDO));
        if (!StringUtils.isEmpty(ciJobDO.getJobTemplate())) {
            CiTemplateDO ciTemplateDO = ciDao.getTemplateByName(ciJobDO.getJobTemplate());
            ciJobVO.setCiTemplateDO(ciTemplateDO);
            //ciJobVO.setJobTemplateMd5(jenkinsService.getJobXmlMd5(ciJobDO.getJobName()));
        }

        if (!StringUtils.isEmpty(ciJobDO.getDeployJobTemplate())) {
            CiTemplateDO ciTemplateDO = ciDao.getTemplateByName(ciJobDO.getDeployJobTemplate());
            ciJobVO.setCiDeployTemplateDO(ciTemplateDO);
            //ciJobVO.setDeployJobTemplateMd5(jenkinsService.getJobXmlMd5(ciJobDO.getDeployJobName()));
        }
        // BuildDetails  只查询最新的3次build
        ciJobVO.setBuildDetails(getBuildDetails(ciJobDO));
        return ciJobVO;
    }

    private List<BuildDetail> getBuildDetails(CiJobDO ciJobDO) {
        List<CiBuildDO> buildList = ciDao.getBuildPage(ciJobDO.getId(), ciJobDO.getJobName(), 0, 3);
        List<BuildDetail> buildDetails = new ArrayList<>();
        for (CiBuildDO ciBuildDO : buildList)
            buildDetails.add(new BuildDetail(ciBuildDO));
        return buildDetails;
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
    public BusinessWrapper<Boolean> scanTpls() {
        List<CiTemplateDO> list = ciDao.queryTemplates();
        for (CiTemplateDO ciTemplateDO : list) {
            // 更新自己，转换字符
            jenkinsService.updateJob(ciTemplateDO.getName(), ciTemplateDO.getName());
            String md5 = jenkinsService.getJobXmlMd5(ciTemplateDO.getName());
            if (!ciTemplateDO.getMd5().equals(md5)) {
                ciTemplateDO.setMd5(md5);
                ciTemplateDO.setVersion(ciTemplateDO.getVersion() + 1);
                ciDao.updateTemplate(ciTemplateDO);
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }


    @Override
    public TableVO<List<CiTemplateVO>> getTemplatePage(String name, int appType, int ciType, int page, int length) {
        long size = ciDao.getTemplateSize(name, appType, ciType);
        List<CiTemplateDO> list = ciDao.getTemplatePage(name, appType, ciType, page * length, length);
        List<CiTemplateVO> voList = new ArrayList<>();
        for (CiTemplateDO ciTemplateDO : list) {
            CiTemplateVO tplVO = getCiTemplateVO(ciTemplateDO);
            tplVO.setEnv(new EnvType(tplVO.getEnvType()));
            voList.add(tplVO);
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
            voList.add(getBuildVO(ciJobDO, ciBuildDO));
        return new TableVO<>(size, voList);
    }

    @Override
    public CiBuildVO getBuildDetail(long buildId) {
        CiBuildDO ciBuildDO = ciDao.getBuild(buildId);
        CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
        CiBuildVO ciBuildVO = getBuildVO(ciJobDO, ciBuildDO);
        return ciBuildVO;
    }

    @Override
    public BusinessWrapper<Boolean> checkBuildDetail(long buildId) {
        CiBuildDO ciBuildDO = ciDao.getBuild(buildId);
        BuildWithDetails bd = jenkinsService.getBuildDetail(ciBuildDO);
        if (bd == null) return new BusinessWrapper<Boolean>(false);
        if (!bd.isBuilding()) {
            ciBuildDO.setBuildPhase("FINALIZED");
            ciBuildDO.setBuildStatus(bd.getResult().name());
            ciDao.updateBuild(ciBuildDO);
            return new BusinessWrapper<Boolean>(true);
        }
        return new BusinessWrapper<Boolean>(false);
    }

    /**
     * 部署详情（兼容Android加固）
     *
     * @param jobId
     * @param page
     * @param length
     * @return
     */
    @Override
    public TableVO<List<CiBuildVO>> getDeployPage(long jobId, int page, int length) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        long size = ciDao.getBuildSize(jobId, ciJobDO.getDeployJobName());
        List<CiBuildDO> list = ciDao.getBuildPage(jobId, ciJobDO.getDeployJobName(), page * length, length);
        List<CiBuildVO> voList = new ArrayList<>();
        for (CiBuildDO ciBuildDO : list)
            voList.add(getBuildVO(ciJobDO, ciBuildDO));
        return new TableVO<>(size, voList);
    }

    @Override
    public List<CiBuildVO> getArtifact(long jobId, int buildNumber) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        List<BuildArtifactDO> list = ciDao.queryBuildArtifactByJobName(ciJobDO.getJobName(), buildNumber);
        List<CiBuildVO> voList = new ArrayList<>();
        for (BuildArtifactDO buildArtifactDO : list) {
            CiBuildDO ciBuildDO = ciDao.getBuild(buildArtifactDO.getBuildId());
            voList.add(getBuildVO(ciJobDO, ciBuildDO));
        }
        return voList;
    }

    @Override
    public List<CiBuildVO> getArtifact(long jobId, String versionName) {
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        List<BuildArtifactDO> list = ciDao.queryBuildArtifactByVersionName(ciJobDO.getJobName(), versionName);
        List<CiBuildVO> voList = new ArrayList<>();
        for (BuildArtifactDO buildArtifactDO : list) {
            CiBuildDO ciBuildDO = ciDao.getBuild(buildArtifactDO.getBuildId());
            voList.add(getBuildVO(ciJobDO, ciBuildDO));
        }
        return voList;
    }

    @Override
    public List<GitlabCommit> getBuildCommit(long jobId, String jobName, String branch) {
        return gitlabService.getChanges(jobId, jobName, branch);
    }

    /**
     * 取Build详情
     *
     * @param ciJobDO
     * @param ciBuildDO
     * @return
     */
    private CiBuildVO getBuildVO(CiJobDO ciJobDO, CiBuildDO ciBuildDO) {
        CiBuildVO ciBuildVO = new CiBuildVO(ciBuildDO);
        ciBuildVO.setJobParams(getBuildParams(ciBuildDO));
        // notifyList
        List<BuildNotifyDO> notifyList = ciDao.queryBuildNotifyByBuildId(ciBuildDO.getId());
        ciBuildVO.setNotifyList(notifyList);
        // console http://ci.domain.cn/job/java_opscloud_prod/112/console
        invokeCiBuildVONotify(ciBuildVO);
        // artifactList ,android 加固详情
        ciBuildVO.setArtifactList(getArtifactList(ciBuildVO.getId()));
        // commitList
        List<CiBuildCommitDO> commitList = ciDao.queryCiBuildCommitByBuildId(ciBuildDO.getId());
        ciBuildVO.setCommitList(commitList);
        invokeBadgeIcon(ciJobDO, ciBuildVO); // badge icon
        // deployHistory
        List<CiDeployHistoryDO> serverList = ciDao.queryDeployHistoryByBuildId(ciBuildDO.getId());
        ciBuildVO.setServerList(serverList);
        return ciBuildVO;
    }

    private void invokeBadgeIcon(CiJobDO ciJobDO, CiBuildVO ciBuildVO) {
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        // 4 : Test
        if (ciAppDO.getAppType() == 4) {
            if (!StringUtils.isEmpty(ciBuildVO.getBuildPhase()) && ciBuildVO.getBuildPhase().equals("FINALIZED")) {
                if (ciBuildVO.getBuildStatus().equals("SUCCESS")) {
                    ciBuildVO.setBadgeIcon(BUILD_TEST_PASSING);
                    return;
                }
                if (ciBuildVO.getBuildStatus().equals("UNSTABLE")) {
                    ciBuildVO.setBadgeIcon(BUILD_TEST_UNSTABLE);
                    return;
                }
                ciBuildVO.setBadgeIcon(BUILD_TEST_FAILING);
            } else {
                ciBuildVO.setBadgeIcon(BUILD_TEST_STARTED);
            }
        } else {
            if (!StringUtils.isEmpty(ciBuildVO.getBuildPhase()) && ciBuildVO.getBuildPhase().equals("FINALIZED")) {
                if (ciBuildVO.getBuildStatus().equals("SUCCESS")) {
                    ciBuildVO.setBadgeIcon(BUILD_PASSING);
                } else {
                    ciBuildVO.setBadgeIcon(BUILD_FAILING);
                }
            } else {
                ciBuildVO.setBadgeIcon(BUILD_STARTED);
            }
        }
    }


    private void invokeCiBuildVONotify(CiBuildVO ciBuildVO) {
        HashMap<String, BuildNotifyDO> map = new HashMap<>();
        for (BuildNotifyDO buildNotifyDO : ciBuildVO.getNotifyList())
            map.put(buildNotifyDO.getBuildPhase(), buildNotifyDO);
        if (map.containsKey("STARTED"))
            ciBuildVO.setConsole(map.get("STARTED").getBuildFullUrl() + BUILD_CONSOLE);
        if (map.containsKey("FINALIZED"))
            ciBuildVO.setDingtalk(map.get("FINALIZED").isDingtalk());
        if (map.containsKey("QUEUED") && map.containsKey("STARTED")) {
            try {
                long timeQueue = TimeUtils.dateToStamp(map.get("STARTED").getGmtCreate()) - TimeUtils.dateToStamp(map.get("QUEUED").getGmtCreate());
                ciBuildVO.setTimeQueue(timeQueue / 1000);
            } catch (Exception e) {
            }
        }
        if (map.containsKey("FINALIZED") && map.containsKey("STARTED")) {
            try {
                long timeBuild = TimeUtils.dateToStamp(map.get("FINALIZED").getGmtCreate()) - TimeUtils.dateToStamp(map.get("STARTED").getGmtCreate());
                ciBuildVO.setTimeBuild(timeBuild / 1000);
            } catch (Exception e) {
            }
        }

    }

    private List<BuildArtifactVO> getArtifactList(long buildId) {
        List<BuildArtifactDO> artifactList = ciDao.queryBuildArtifactByBuildId(buildId);
        List<BuildArtifactVO> list = new ArrayList<>();
        //artifactList = ciDao.queryBuildArtifactByBuildId(buildId);
//        if (buildType == TYPE_BUILD) {
//            artifactList = ciDao.queryBuildArtifactByBuildId(buildId);
//        } else {
//            CiBuildDO ciBuildDO = ciDao.getBuild(buildId);
//            String ossPath = getBuildParamValue(ciBuildDO, PARAM_OSS_PATH);
//            if (!StringUtils.isEmpty(ossPath)) {
//                artifactList = ciDao.queryBuildArtifactByOssPath(ossPath);
//            } else {
//                return list;
//            }
//        }
        for (BuildArtifactDO buildArtifactDO : artifactList) {
            BuildArtifactVO vo = BeanCopierUtils.copyProperties(buildArtifactDO, BuildArtifactVO.class);
            vo.setSize(ByteUtils.getSize(buildArtifactDO.getArtifactSize()));
            invokePkgUrl(vo);
            invokeArtifactBadge(vo);
            list.add(vo);
        }
        return list;
    }

    private void invokePkgUrl(BuildArtifactVO buildArtifactVO) {
        CiBuildDO ciBuildDO = ciDao.getBuild(buildArtifactVO.getBuildId());
        CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        switch (ciAppDO.getAppType()) {
            case 1: //Python
                //
            case 2: //iOS
                //
            case 3: //Android
                buildArtifactVO.setPkgUrl(PKG_SERVER_URL + buildArtifactVO.getOssPath());
                break;
            default:
                break;
        }

    }

    private void invokeArtifactBadge(BuildArtifactVO buildArtifactVO) {
        try {
            String r = BADGE_URL + Joiner.on("-").join(buildArtifactVO.getOssPath().replaceAll("-", "--"), buildArtifactVO.getSize(), "blue.svg");
            buildArtifactVO.setBadge(r);
        } catch (Exception e) {
        }
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
        CiTemplateDO tpl = null;
        String jobName = "";
        try {
            // CI
            if (type == 0) {
                tpl = ciDao.getTemplateByName(ciJobDO.getJobTemplate());
                jobName = ciJobDO.getJobName();
                if (tpl.getVersion() == ciJobDO.getJobVersion())  // 比对版本Code
                    return true;
                ciJobDO.setJobVersion(tpl.getVersion());
            }
            // CD
            if (type == 1) {
                tpl = ciDao.getTemplateByName(ciJobDO.getDeployJobTemplate());
                jobName = ciJobDO.getDeployJobName();
                if (tpl.getVersion() == ciJobDO.getDeployJobVersion())  // 比对版本Code
                    return true;
                ciJobDO.setDeployJobVersion(tpl.getVersion());
            }
            // 比对版本Code
            jenkinsService.updateJob(jobName, tpl.getName(),getYaml(getCiJobVO(ciJobDO)));
            ciJobDO.setJobVersion(tpl.getVersion());
            ciDao.updateCiJob(ciJobDO);
            return true;
        } catch (Exception e) {
            return false;
        }
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
    public String previewTemplate(long templateId) {
        try {
            CiTemplateDO ciTemplateDO = ciDao.getTemplate(templateId);
            String tempXml = jenkinsService.getJobXml(ciTemplateDO.getName());
            return tempXml;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
    public List<HostPatternCi> getHostPatternCi(long groupId) {
        if (groupId == 0) return new ArrayList<>();
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

    @Override
    public int getMyAppSize() {
        return ciDao.getMyAppSize(SessionUtils.getUsername());
    }


    @Override
    public LabelDO addLabel(LabelDO labelDO) {
        try {
            ciDao.addLabel(labelDO);
        } catch (Exception e) {
        }
        return labelDO;
    }

    @Override
    public LabelDO saveLabel(LabelDO labelDO) {
        try {
            ciDao.updateLabel(labelDO);
        } catch (Exception e) {
        }
        return labelDO;
    }

    @Override
    public List<LabelVO> queryLabel() {
        List<LabelDO> list = ciDao.queryLabel();
        List<LabelVO> voList = new ArrayList<>();
        for (LabelDO labelDO : list) {
            LabelVO labelVO = BeanCopierUtils.copyProperties(labelDO, LabelVO.class);
            if (labelVO.getLabelType() == 0) {
                labelVO.setActive(true);
                labelVO.setColor(LabelVO.ACTIVE_COLOR);
            }
            labelVO.setMemberList(ciDao.getMemberByLabel(labelVO.getId()));
            voList.add(labelVO);
        }
        return voList;
    }

    @Override
    public BusinessWrapper<Boolean> addLabelMember(LabelMemberDO labelMemberDO) {
        try {
            ciDao.addMember(labelMemberDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delLabelMember(long id) {
        try {
            ciDao.delMember(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public List<LabelMemberVO> getLabelMember(long id) {
        return ciDao.getMemberByLabel(id);
    }

}
