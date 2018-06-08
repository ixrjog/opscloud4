package com.sdg.cmdb.service.impl;


import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import com.sdg.cmdb.dao.cmdb.GitlabDao;
import com.sdg.cmdb.dao.cmdb.JenkinsDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.JenkinsItemEnum;
import com.sdg.cmdb.domain.gitlab.*;
import com.sdg.cmdb.domain.jenkins.*;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import com.sdg.cmdb.util.TplUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class JenkinsServiceImpl implements JenkinsService, InitializingBean {

    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");
    private static final Logger logger = LoggerFactory.getLogger(JenkinsServiceImpl.class);

    public static final String DEPLOY_PATH_STATIC_NAME = "deployPathStatic";

    public static final String DEPLOY_PATH_NAME = "deployPath";

    private static JenkinsServer jenkinsServer;

    @Resource
    private JenkinsDao jenkinsDao;

    @Resource
    private GitlabDao gitlabDao;

    @Resource
    private UserDao userDao;

    @Resource
    private GitService gitService;

    @Resource
    private DingtalkService dingtalkService;

    private String jenkinsHost;

    @Resource
    private ConfigCenterService configCenterService;


    private HashMap<String, String> configMap;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.JENKINS.getItemKey());
    }

    private void init() {
        HashMap<String, String> configMap = acqConifMap();
        String jenkinsHost = configMap.get(JenkinsItemEnum.JENKINS_HOST.getItemKey());
        String jenkinsUser = configMap.get(JenkinsItemEnum.JENKINS_USER.getItemKey());
        // tokin
        String jenkinsPwd = configMap.get(JenkinsItemEnum.JENKINS_PWD.getItemKey());
        try {
            URI tmp = new URI(jenkinsHost);
            JenkinsServer jenkins = new JenkinsServer(tmp, jenkinsUser, jenkinsPwd);
            this.jenkinsServer = jenkins;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String acqJenkinsHost() {
        if (StringUtils.isEmpty(jenkinsHost)) {
            HashMap<String, String> configMap = acqConifMap();
            jenkinsHost = configMap.get(JenkinsItemEnum.JENKINS_HOST.getItemKey());
        }
        return jenkinsHost;
    }

    @Override
    public BusinessWrapper<Boolean> jobNotes(JobNoteVO jobNoteVO) {
        JobNoteDO jobNoteDO = new JobNoteDO(jobNoteVO);
        JobNoteDO jobNote = jenkinsDao.queryJobNote(jobNoteDO.getJobName(), jobNoteDO.getBuildNumber(), jobNoteDO.getBuildPhase());
        if (jobNote != null) return new BusinessWrapper<>(true);
        jenkinsDao.addJobNote(jobNoteDO);
        JenkinsJobBuildDO jobBuildDO = jenkinsDao.queryJobBuildsByJobNameAndBuildNumber(jobNoteDO.getJobName(), jobNoteDO.getBuildNumber());
        if (jobBuildDO == null) {
            // 查询任务队列记录
            jobBuildDO = jenkinsDao.queryJobBuildsByQueue(jobNoteDO.getJobName());
            if (jobBuildDO != null) {
                jobBuildDO.setBuildNumber(jobNoteDO.getBuildNumber());
                // 更新构建编号
                jenkinsDao.updateJenkinsJobBuild(jobBuildDO);
            }
        }
        // 记录 artifacts
        Map<String, HashMap<String, String>> artifacts = jobNoteVO.getBuild().getArtifacts();
        for (String artifactsName : artifacts.keySet()) {
            HashMap<String, String> artifact = artifacts.get(artifactsName);
            if (artifact.containsKey("archive")) {
                String archiveUrl = artifact.get("archive");
                BuildArtifactDO artifactDO = new BuildArtifactDO(jobBuildDO.getId(), artifactsName, archiveUrl);
                try {
                    BuildArtifactDO checkArtifact = jenkinsDao.queryBuildArtifact(artifactDO.getJobBuildsId(), artifactDO.getArtifactsName(), artifactDO.getArchiveUrl());
                    if (checkArtifact == null)
                        jenkinsDao.addBuildArtifact(artifactDO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            webHookAndroidDebug(jobNoteDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dingtalkNotes(jobNoteDO, jobBuildDO);
        return new BusinessWrapper<>(true);
    }

    private BusinessWrapper<Boolean> webHookAndroidDebug(JobNoteDO jobNoteDO) {
        if (!jobNoteDO.getBuildPhase().equalsIgnoreCase("FINALIZED")) return new BusinessWrapper<Boolean>(false);

        if (StringUtils.isEmpty(jobNoteDO.getBuildStatus()) || !jobNoteDO.getBuildStatus().equalsIgnoreCase("SUCCESS"))
            return new BusinessWrapper<Boolean>(false);

        JenkinsJobBuildDO jobBuildDO = jenkinsDao.queryJobBuildsByJobNameAndBuildNumber(jobNoteDO.getJobName(), jobNoteDO.getBuildNumber());
        // 非android跳过
        if (jobBuildDO.getBuildType() != GitlabWebHooksDO.HooksTypeEnum.android.getCode())
            return new BusinessWrapper<Boolean>(false);
        JenkinsJobDO jenkinsJobDO = jenkinsDao.queryJenkinsJobById(jobBuildDO.getJobId());
        // 非debug跳过
        if (jenkinsJobDO.getJobEnvType() != JenkinsJobDO.JobEnvTypeEnum.debug.getCode())
            return new BusinessWrapper<Boolean>(false);
        JobBuildParamDO paramProject = jenkinsDao.queryJobBuildParamByBuildsIdAndName(jobBuildDO.getId(), "appName");
        List<BuildArtifactDO> artifacts = jenkinsDao.queryBuildArtifactByJobBuildsId(jobBuildDO.getId());
        for (BuildArtifactDO buildArtifactDO : artifacts) {
            if (buildArtifactDO.getArtifactsName().indexOf(".apk") != -1) {
                Map<String, String> map = new HashMap<>();
                map.put("project", paramProject.getParamValue());
                map.put("apk", buildArtifactDO.getArchiveUrl());
                HashMap<String, String> configMap = acqConifMap();
                String debugUrl = configMap.get(JenkinsItemEnum.ANDROID_DEBUG_URL.getItemKey());
                HttpResponse response = WebHookService.post(map, debugUrl);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    jobNoteDO.setWebHook(2);
                    jenkinsDao.updateJobNote(jobNoteDO);
                    new BusinessWrapper<Boolean>(true);
                } else {
                    jobNoteDO.setWebHook(1);
                    jenkinsDao.updateJobNote(jobNoteDO);
                    new BusinessWrapper<Boolean>(false);
                }

            }
        }
        return new BusinessWrapper<Boolean>(false);
    }


    /**
     * 发送dingtalk通知
     *
     * @param jobNoteDO
     */
    @Override
    public void dingtalkNotes(JobNoteDO jobNoteDO, JenkinsJobBuildDO jobBuildDO) {
        if (jobBuildDO == null) return;
        // 判断任务环境类型
        JenkinsJobDO jenkinsJobDO = jenkinsDao.queryJenkinsJobById(jobBuildDO.getJobId());
        if (jenkinsJobDO.getBuildType() == GitlabWebHooksDO.HooksTypeEnum.ft.getCode()) {
            ftDingtalkNotes(jenkinsJobDO, jobNoteDO, jobBuildDO);
            return;
        }

        if (jenkinsJobDO.getBuildType() == GitlabWebHooksDO.HooksTypeEnum.android.getCode()) {
            androidDingtalkNotes(jenkinsJobDO, jobNoteDO, jobBuildDO);
            return;
        }

        if (jenkinsJobDO.getBuildType() == GitlabWebHooksDO.HooksTypeEnum.ios.getCode()) {
            iosDingtalkNotes(jenkinsJobDO, jobNoteDO, jobBuildDO);
            return;
        }
    }

    private void iosDingtalkNotes(JenkinsJobDO jenkinsJobDO, JobNoteDO jobNoteDO, JenkinsJobBuildDO jobBuildDO) {
        // prod
        if (jobNoteDO.getBuildPhase().equalsIgnoreCase("QUEUED")) return;
        if (jobNoteDO.getBuildPhase().equalsIgnoreCase("COMPLETED")) return;

        try {
            List<BuildArtifactDO> artifacts = jenkinsDao.queryBuildArtifactByJobBuildsId(jobBuildDO.getId());
            HashMap<String, String> params = acqParams(jobBuildDO);
            dingtalkService.sendIosBuildMsg(artifacts, params, jobBuildDO, new JobNoteVO(jobNoteDO, acqJenkinsHost(), jobBuildDO.getBuildType()));
            jobNoteDO.setNotice(true);
            jenkinsDao.updateJobNote(jobNoteDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void androidDingtalkNotes(JenkinsJobDO jenkinsJobDO, JobNoteDO jobNoteDO, JenkinsJobBuildDO jobBuildDO) {
        // prod
        if (jobNoteDO.getBuildPhase().equalsIgnoreCase("QUEUED")) return;
        if (jobNoteDO.getBuildPhase().equalsIgnoreCase("COMPLETED")) return;

        try {
            List<BuildArtifactDO> artifacts = jenkinsDao.queryBuildArtifactByJobBuildsId(jobBuildDO.getId());
            HashMap<String, String> params = acqParams(jobBuildDO);
            dingtalkService.sendAndroidBuildMsg(artifacts, params, jobBuildDO, new JobNoteVO(jobNoteDO, acqJenkinsHost(), jobBuildDO.getBuildType()));
            jobNoteDO.setNotice(true);
            jenkinsDao.updateJobNote(jobNoteDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void ftDingtalkNotes(JenkinsJobDO jenkinsJobDO, JobNoteDO jobNoteDO, JenkinsJobBuildDO jobBuildDO) {
        // daily
        if (jenkinsJobDO.getJobEnvType() == GitlabWebHooksVO.EnvTypeEnum.header.getCode()) {
            if (!jobNoteDO.getBuildPhase().equalsIgnoreCase("FINALIZED")) return;
        }
        // prod
        if (jenkinsJobDO.getJobEnvType() == GitlabWebHooksVO.EnvTypeEnum.tag.getCode()) {
            if (jobNoteDO.getBuildPhase().equalsIgnoreCase("QUEUED")) return;
            if (jobNoteDO.getBuildPhase().equalsIgnoreCase("COMPLETED")) return;
        }

        try {
            HashMap<String, String> params = acqParams(jobBuildDO);
            dingtalkService.sendFtBuildMsg(jenkinsJobDO.getJobEnvType(), params, jobBuildDO, new JobNoteVO(jobNoteDO, acqJenkinsHost(), jobBuildDO.getBuildType()));
            jobNoteDO.setNotice(true);
            jenkinsDao.updateJobNote(jobNoteDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean buildFtJob(GitlabWebHooksDO webHooks, HashMap<String, String> params, int envType) {
        logger.info("Jenkins build ftJob={} ", webHooks.getProjectName() + "." + webHooks.getRepositoryName());
        String jobName = webHooks.getProjectName() + "." + webHooks.getRepositoryName();
        String fullJobName = acqFullJobName(jobName, envType);
        // 保存任务
        JenkinsJobDO jenkinsJobDO = saveJenkinsJob(webHooks, fullJobName, envType, GitlabWebHooksDO.HooksTypeEnum.ft.getCode());
        // 保存任务参数 & 重新通过deployPathStatic参数计算deployPath
        String deployPathStatic = queryJobParam(jenkinsJobDO, DEPLOY_PATH_STATIC_NAME);
        // deployPathStatic = true 静态路径发布
        if (!StringUtils.isEmpty(deployPathStatic) && deployPathStatic.equalsIgnoreCase("true")) {
            params.put("deployPath", queryJobParam(jenkinsJobDO, DEPLOY_PATH_NAME));
        }
        saveFtJobParams(jenkinsJobDO, params);


        Job ftJob = getJob(fullJobName);
        // job不存在
        if (ftJob == null) {
            coreLogger.info("Jenkins job={} does not exist", fullJobName);
            if (!createFtJob(fullJobName, envType)) {
                coreLogger.info("Jenkins create job={} failed", fullJobName);
                return false;
            } else {
                // 重新获取job
                ftJob = getJob(fullJobName);
                if (ftJob == null)
                    return false;
            }
        }
        UserDO userDO = acqUser(webHooks);
        return build(ftJob, params, userDO, jenkinsJobDO, webHooks.getId());
    }

    /**
     * 获取job配置的参数
     *
     * @param jobDO
     * @param paramName
     * @return
     */
    private String queryJobParam(JenkinsJobDO jobDO, String paramName) {
        JobParamDO param = jenkinsDao.queryJobParamByJobIdAndName(jobDO.getId(), paramName);
        if (param != null)
            return param.getParamValue();
        return "";
    }

    /**
     * 保存前端构建的参数
     *
     * @param jenkinsJobDO
     * @param params
     */
    private void saveFtJobParams(JenkinsJobDO jenkinsJobDO, HashMap<String, String> params) {
        if (jenkinsJobDO == null || jenkinsJobDO.getId() == 0) return;

        for (String key : params.keySet()) {
            String value = params.get(key);
            JobParamDO param = jenkinsDao.queryJobParamByJobIdAndName(jenkinsJobDO.getId(), key);
            if (param != null) return;
            param = new JobParamDO(jenkinsJobDO.getId(), key, value);
            try {
                jenkinsDao.addJobParam(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public BusinessWrapper<Boolean> buildJob(long id) {
        JenkinsJobDO jobDO = jenkinsDao.queryJenkinsJobById(id);
        if (jobDO == null) return new BusinessWrapper<Boolean>(false);
        if (jobDO.getBuildType() == GitlabWebHooksDO.HooksTypeEnum.android.getCode())
            return buildAndroidJob(jobDO);
        return new BusinessWrapper<Boolean>(false);
    }


    @Override
    public BusinessWrapper<Boolean> createJob(long id) {
        JenkinsJobDO jobDO = jenkinsDao.queryJenkinsJobById(id);
        if (jobDO == null) return new BusinessWrapper<Boolean>(false);
        if (jobDO.getBuildType() == GitlabWebHooksDO.HooksTypeEnum.test.getCode())
            return new BusinessWrapper<Boolean>(createTestJob(jobDO));
        return new BusinessWrapper<Boolean>(false);
    }

    @Override
    public BusinessWrapper<Boolean> buildJob(long id, String mbranch, String buildType) {
        JenkinsJobDO jobDO = jenkinsDao.queryJenkinsJobById(id);
        if (jobDO == null) return new BusinessWrapper<Boolean>(false);
        if (jobDO.getBuildType() == GitlabWebHooksDO.HooksTypeEnum.ios.getCode())
            return buildIosJob(jobDO, mbranch, buildType);
        return new BusinessWrapper<Boolean>(false);
    }

    private BusinessWrapper<Boolean> buildIosJob(JenkinsJobDO jobDO, String mbranch, String buildType) {
        String jobName = jobDO.getJobName();
        Job iosJob = getJob(jobName);
        // job不存在 按模版新建
        if (iosJob == null) {
            coreLogger.info("Jenkins ios job={} does not exist", jobName);
            if (!createIosJob(jobName)) {
                coreLogger.info("Jenkins create ios job={} failed", jobName);
                return new BusinessWrapper<Boolean>(false);
            } else {
                // 重新获取job
                iosJob = getJob(jobName);
                if (iosJob == null)
                    return new BusinessWrapper<Boolean>(false);
            }
        }
        HashMap<String, String> params = acqParams(jobDO, mbranch, buildType);
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        boolean r = build(iosJob, params, userDO, jobDO, 0);
        return new BusinessWrapper<Boolean>(r);
    }

    /**
     * iOS params
     *
     * @param jobDO
     * @param mbranch
     * @param buildType
     * @return
     */
    private HashMap<String, String> acqParams(JenkinsJobDO jobDO, String mbranch, String buildType) {
        HashMap<String, String> params = acqParams(jobDO);
        // 部署路径
        //params.put("deployPath", acqDeployPath(params));
        params.put("mbranch", mbranch);
        params.put("buildType", buildType);
        /**
         * 计算阿里云oss部署路径
         * e.g.
         * http://img0-cdn.oss-cn-hangzhou.aliyuncs.com/ci/ios/lhapprn/${year}_${month}/${branch}_${buildType}_${month}${day}_${hour}${minute}${second}/
         */
        String ossPath = params.get("ossPath");
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        String deployPath = ossPath;
        Integer month = c.get(Calendar.MONTH) + 1;
        deployPath += c.get(Calendar.YEAR) + "_" + month + "/" + mbranch + "_" + buildType + "_";

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMdd_HHmmss");
        java.util.Date currentTime = date;//得到当前系统时间
        String strDate = formatter.format(currentTime); //将日期时间格式化
        deployPath += strDate;
        params.put("deployPath", deployPath);
        /**
         * 计算plist文件内容
         */
        HashMap<String, String> configMap = acqConifMap();

        String ossBucket = configMap.get(JenkinsItemEnum.JENKINS_OSS_BUCKET_FT_ONLINE.getItemKey());
        String packageName = params.get("packageName");
        String packageUrl = deployPath.replace(ossBucket, "http://cdn.52shangou.com") + "/" + packageName;
        //String plist = TplUtils.doCreateJenkinsIosJobPlist(params, packageUrl);
        params.put("packageUrl", packageUrl);
        return params;
    }

    @Override
    public RefsVO queryJobRefs(long id) {
        JenkinsJobDO jobDO = jenkinsDao.queryJenkinsJobById(id);
        String repositoryUrl = jobDO.getRepositoryUrl();
        //System.err.println(repositoryUrl);
        return gitService.queryRefsByUrl(repositoryUrl, GitServiceImpl.GITLAB_REPOSITORY);
    }


    @Override
    public RefsVO changeJobRefs(long id, String ref, int type) {
        JenkinsJobDO jobDO = jenkinsDao.queryJenkinsJobById(id);
        // String repositoryUrl = jobDO.getRepositoryUrl();

        String repositoryGit = gitService.acqRepositoryGitByUrl(GitServiceImpl.GITLAB_REPOSITORY, jobDO.getRepositoryUrl());
        System.err.println(repositoryGit);
        RepositoryDO repositoryDO = jenkinsDao.queryRepositoryByUrl(repositoryGit);
        System.err.println(repositoryDO);
        RefsDO refsDO = jenkinsDao.queryRefsByRefAndRefType(ref, type);
        try {
            if (refsDO == null) {
                refsDO = new RefsDO(repositoryDO.getId(), type, ref);
                System.err.println(refsDO);
                jenkinsDao.addRefs(refsDO);
            } else {
                jenkinsDao.delRefs(refsDO.getId());
            }
            return queryJobRefs(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new RefsVO();
        }
    }

    @Override
    public RefsVO updateJobRefs(long id) {
        JenkinsJobDO jobDO = jenkinsDao.queryJenkinsJobById(id);
        String repositoryUrl = jobDO.getRepositoryUrl();
        //System.err.println(repositoryUrl);
        coreLogger.info("Update git refs {}", repositoryUrl);
        return gitService.getRefsByUrl(repositoryUrl, GitServiceImpl.GITLAB_REPOSITORY);
    }

    @Override
    public BusinessWrapper<Boolean> rebuildJob(long id) {
        JenkinsJobBuildDO jobBuildDO = jenkinsDao.queryJobBuildsById(id);
        JenkinsJobDO jobDO = jenkinsDao.queryJenkinsJobById(jobBuildDO.getJobId());
        String jobName = jobBuildDO.getJobName();
        Job job = getJob(jobName);
        // job不存在
        if (job == null) {
            coreLogger.info("Jenkins job={} does not exist", jobName);
            return new BusinessWrapper<Boolean>(false);
        }
        HashMap<String, String> params = acqParams(jobBuildDO);
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        boolean r = build(job, params, userDO, jobDO, 0);
        return new BusinessWrapper<Boolean>(r);

    }

    @Override
    public BusinessWrapper<Boolean> appLink(long id) {
        JenkinsJobBuildDO jobBuildDO = jenkinsDao.queryJobBuildsById(id);
        String link = acqArtifactCdnUrl(jobBuildDO);
        HashMap<String, String> params = acqParams(jobBuildDO);
        String appName = params.get("appName");
        NewAppLink appLink = new NewAppLink(appName, link);
        //System.err.println(appLink);
        HttpResponse response = WebHookService.post("http://www.52shangou.com/version/official/newAppLink", appLink);
        if (response == null)
            return new BusinessWrapper<Boolean>(false);
        //System.err.println(response.getStatusLine().getReasonPhrase());
        //System.err.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return new BusinessWrapper<Boolean>(true);
        } else {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    /**
     * android打包
     *
     * @param jobDO
     * @return
     */
    private BusinessWrapper<Boolean> buildAndroidJob(JenkinsJobDO jobDO) {
        String jobName = jobDO.getJobName();
        Job androidJob = getJob(jobName);
        if (androidJob == null) {
            coreLogger.info("Jenkins android job={} does not exist", jobName);
            if (!createAndroidJob(jobName)) {
                coreLogger.info("Jenkins create android job={} failed", jobName);
                return new BusinessWrapper<Boolean>(false);
            } else {
                // 重新获取job
                androidJob = getJob(jobName);
                if (androidJob == null)
                    return new BusinessWrapper<Boolean>(false);
            }
        }

        HashMap<String, String> params = acqParams(jobDO);
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        boolean r = build(androidJob, params, userDO, jobDO, 0);
        return new BusinessWrapper<Boolean>(r);
    }

    private HashMap<String, String> acqParams(JenkinsJobBuildDO jobBuildDO) {
        List<JobBuildParamDO> paramList = jenkinsDao.queryJobBuildParamByBuildsId(jobBuildDO.getId());
        HashMap<String, String> params = new HashMap<>();
        for (JobBuildParamDO param : paramList) {
            params.put(param.getParamName(), param.getParamValue());
        }
        return params;
    }

    /**
     * android
     *
     * @param jobDO
     * @return
     */
    private HashMap<String, String> acqParams(JenkinsJobDO jobDO) {
        List<JobParamDO> list = jenkinsDao.queryJobParamByJobId(jobDO.getId());
        HashMap<String, String> params = new HashMap<>();
        // 自定义参数
        for (JobParamDO jobParamDO : list) {
            params.put(jobParamDO.getParamName(), jobParamDO.getParamValue());
        }
        // 部署路径
        params.put("deployPath", acqDeployPath(params));
        // 项目名
        params.put("project", gitService.acqProject(jobDO.getRepositoryUrl()));
        // 仓库名
        params.put("repo", gitService.acqRepo(jobDO.getRepositoryUrl()));
        return params;
    }


    String acqDeployPath(HashMap<String, String> params) {
        // img0-cdn/ci/android/lhapp/
        // http://img0-cdn.oss-cn-hangzhou.aliyuncs.com/ci/android/lhapp/${year}_${month}/${branch}_${buildType}_${month}${day}_${hour}${minute}${second}/
        String buildType = params.get("buildType");
        String mbranch = params.get("mbranch");
        String ossPath = params.get("ossPath");
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        String deployPath = ossPath;
        Integer month = c.get(Calendar.MONTH) + 1;

        deployPath += c.get(Calendar.YEAR) + "_" + month + "/" + mbranch + "_" + buildType + "_";

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMdd_HHmmss");
        java.util.Date currentTime = date;//得到当前系统时间
        String strDate = formatter.format(currentTime); //将日期时间格式化
        deployPath += strDate;
        return deployPath;
    }


    @Override
    public boolean createFtJob(String fullJobName, int envType) {
        String jobXml = TplUtils.doCreateJenkinsFtJob(fullJobName, envType);
        return createJob(fullJobName, jobXml);
    }

    @Override
    public boolean createIosJob(String jobName) {
        String jobXml = TplUtils.doCreateJenkinsIosJob(jobName);
        return createJob(jobName, jobXml);
    }

    @Override
    public boolean createAndroidJob(String jobName) {
        String jobXml = TplUtils.doCreateJenkinsAndroidJob(jobName);
        return createJob(jobName, jobXml);
    }


    @Override
    public boolean createTestJob(JenkinsJobDO jenkinsJobDO) {
        // JenkinsJobDO jenkinsJobDO = jenkinsDao.queryJenkinsJobByName(jobName);
        String jobName = jenkinsJobDO.getJobName();
        List<JobParamDO> params = jenkinsDao.queryJobParamByJobId(jenkinsJobDO.getId());
        List<JobUserDO> users = jenkinsDao.queryJobUserByJobId(jenkinsJobDO.getId());
        String recipientList = acqRecipientList(users);

        String jobXml = TplUtils.doCreateJenkinsTestJob(jobName, params, jenkinsJobDO.getRepositoryUrl(), recipientList);
        if (createJob(jobName, jobXml)) {
            jenkinsJobDO.setCreated(true);
            jenkinsDao.updateJenkinsJob(jenkinsJobDO);
            return true;
        }
        return false;
    }

    //获取邮件通知列表
    private String acqRecipientList(List<JobUserDO> users) {
        String recipientList = "";
        for (JobUserDO jobUserDO : users) {
            UserDO userDO = userDao.getUserById(jobUserDO.getUserId());
            if (StringUtils.isEmpty(recipientList)) {
                recipientList = userDO.getMail();
            } else {
                recipientList += ", " + userDO.getMail();
            }
        }
        return recipientList;
    }

    private boolean createJob(String jobName, String jobXml) {
        try {
            jenkinsServer.createJob(jobName, jobXml, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private String acqFullJobName(String jobName, int envType) {
        String envName = GitlabWebHooksVO.EnvTypeEnum.getEnvTypeName(envType);
        String fullJobName = jobName + "." + envName;
        return fullJobName;
    }


    private UserDO acqUser(GitlabWebHooksDO webHooks) {
        String email = webHooks.getEmail();
        if (StringUtils.isEmpty(email))
            return new UserDO(webHooks.getUserName());
        String s[] = email.split("@");
        UserDO userDO = userDao.getUserByName(s[0]);
        return userDO;
    }

    /**
     * 获取job
     *
     * @param fullJobName
     * @return
     */
    private Job getJob(String fullJobName) {
        Map<String, Job> jobMap = getJobs();
        if (jobMap.containsKey(fullJobName)) {
            // 存在
            return jobMap.get(fullJobName);
        } else {
            // 不存在
            return null;
        }
    }

    /**
     * 查询所有job
     *
     * @return
     */
    @Override
    public Map<String, Job> getJobs() {
        Map<String, Job> jobMap = new HashMap<>();
        try {
            jobMap = jenkinsServer.getJobs();
            return jobMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobMap;
    }

    /**
     * job build
     *
     * @param job
     * @param params
     * @return
     */
    public boolean build(Job job, HashMap<String, String> params, UserDO userDO, JenkinsJobDO jenkinsJobDO, long webHookId) {
        coreLogger.info("Jenkins job={} run build", job.getName());
        //for (String key : jobMap.keySet()) {
        // Job job = jobMap.get(key);
        try {
            QueueReference queueReference = job.build(params, true);
            JobWithDetails jobWithDetails = job.details();
            Build build = jobWithDetails.getLastBuild();
            BuildWithDetails buildWithDetails = build.details();
            boolean isBuilding = buildWithDetails.isBuilding();
            // 记录构建任务
            if (isBuilding) {
                JenkinsJobBuildDO jenkinsJobBuildDO = new JenkinsJobBuildDO(jenkinsJobDO, userDO, webHookId, build.getNumber());
                // 支持队列
                JenkinsJobBuildDO jobBuild = jenkinsDao.queryJobBuildsByJobNameAndBuildNumber(jenkinsJobBuildDO.getJobName(), build.getNumber());
                // 构建编号重置为队列
                if (jobBuild != null)
                    jenkinsJobBuildDO.setBuildNumber(JenkinsJobBuildDO.BUILD_NUMBER_QUEUE);

                jenkinsDao.addJenkinsJobBuild(jenkinsJobBuildDO);
                saveJobBuildsParams(jenkinsJobBuildDO, params);
            }
            return isBuilding;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存每次构建任务的参数
     *
     * @param jobBuild
     * @param params
     */
    private void saveJobBuildsParams(JenkinsJobBuildDO jobBuild, HashMap<String, String> params) {
        if (jobBuild == null || jobBuild.getId() == 0) return;

        for (String key : params.keySet()) {
            String value = params.get(key);
            JobBuildParamDO param = new JobBuildParamDO(jobBuild.getId(), key, value);
            try {
                jenkinsDao.addJobBuildParam(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JenkinsJobDO saveJenkinsJob(GitlabWebHooksDO webHooks, String fullJobName, int envType, int buildType) {
        JenkinsJobDO jenkinsJobDO = jenkinsDao.queryJenkinsJobByName(fullJobName);
        if (jenkinsJobDO == null) {
            jenkinsJobDO = new JenkinsJobDO(webHooks, fullJobName, envType, buildType);
            try {
                jenkinsDao.addJenkinsJob(jenkinsJobDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jenkinsJobDO;
    }

    @Override
    public TableVO<List<GitlabWebHooksDO>> getWebHooksPage(String projectName, String repositoryName, int webHooksType, int triggerBuild, int page, int length) {
        long size = gitlabDao.getWebHooksSize(projectName, repositoryName, webHooksType, triggerBuild);
        List<GitlabWebHooksDO> list = gitlabDao.getWebHooksPage(projectName, repositoryName, webHooksType, triggerBuild, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public TableVO<List<JenkinsJobVO>> queryJobsPage(String jobName, int jobEnvType, int buildType, int page, int length) {
        long size = jenkinsDao.getJobsSize(jobName, jobEnvType, buildType);
        List<JenkinsJobDO> list = jenkinsDao.getJobsPage(jobName, jobEnvType, buildType, page * length, length);
        List<JenkinsJobVO> voList = new ArrayList<>();

        for (JenkinsJobDO jenkinsJobDO : list) {
            List<JobParamDO> params = jenkinsDao.queryJobParamByJobId(jenkinsJobDO.getId());
            JenkinsJobVO jenkinsJobVO = new JenkinsJobVO(jenkinsJobDO, params);
            // 自动化测试任务
            if (jenkinsJobDO.getBuildType() == GitlabWebHooksDO.HooksTypeEnum.test.getCode()) {
                List<JobUserDO> jobUsers = jenkinsDao.queryJobUserByJobId(jenkinsJobDO.getId());
                jenkinsJobVO.setEmailUsers(acqJobUsers(jobUsers));
            }
            voList.add(jenkinsJobVO);
        }
        return new TableVO<>(size, voList);
    }

    /**
     * 转换用户
     *
     * @param jobUsers
     * @return
     */
    private List<UserDO> acqJobUsers(List<JobUserDO> jobUsers) {
        List<UserDO> users = new ArrayList<>();
        for (JobUserDO jobUserDO : jobUsers) {
            UserDO userDO = userDao.getUserById(jobUserDO.getUserId());
            users.add(userDO);
        }
        return users;

    }

    @Override
    public List<JobParamDO> queryJobParams(long jobId) {
        return jenkinsDao.queryJobParamByJobId(jobId);
    }

    @Override
    public BusinessWrapper<Boolean> delJobParams(long id) {
        try {
            jenkinsDao.delJobParam(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public JenkinsJobDO saveJob(JenkinsJobVO jenkinsJobVO) {
        //  System.err.println(jenkinsJobVO);
        JenkinsJobDO jenkinsJobDO = new JenkinsJobDO(jenkinsJobVO);
        try {
            if (jenkinsJobDO.getId() == 0) {
                jenkinsDao.addJenkinsJob(jenkinsJobDO);
            } else {
                jenkinsDao.updateJenkinsJob(jenkinsJobDO);
            }
            jenkinsJobVO.setId(jenkinsJobDO.getId());
            saveJobParams(jenkinsJobVO);
            saveJobUser(jenkinsJobVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jenkinsJobDO;
    }

    // 保存job中的参数
    private void saveJobParams(JenkinsJobVO jenkinsJobVO) {
        if (jenkinsJobVO == null || jenkinsJobVO.getId() == 0) return;
        if (jenkinsJobVO.getParams() == null || jenkinsJobVO.getParams().size() == 0) return;
        List<JobParamDO> params = jenkinsDao.queryJobParamByJobId(jenkinsJobVO.getId());
        // 删除老的配置
        try {
            for (JobParamDO jobParamDO : params) {
                jenkinsDao.delJobParam(jobParamDO.getId());
            }

            for (JobParamDO jobParamDO : jenkinsJobVO.getParams()) {
                jobParamDO.setJobId(jenkinsJobVO.getId());
                jenkinsDao.addJobParam(jobParamDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 保存job中的参数
    private void saveJobUser(JenkinsJobVO jenkinsJobVO) {
        if (jenkinsJobVO == null || jenkinsJobVO.getId() == 0) return;
        if (jenkinsJobVO.getEmailUsers() == null || jenkinsJobVO.getEmailUsers().size() == 0) return;
        List<JobUserDO> jobUsers = jenkinsDao.queryJobUserByJobId(jenkinsJobVO.getId());
        // 删除老的配置
        try {
            for (JobUserDO jobUserDO : jobUsers) {
                jenkinsDao.delJobUser(jobUserDO.getId());
            }
            for (UserDO userDO : jenkinsJobVO.getEmailUsers()) {
                JobUserDO jobUserDO = new JobUserDO(userDO, jenkinsJobVO.getId(), JobUserDO.EMAIL_USER);
                jenkinsDao.addJobUser(jobUserDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BusinessWrapper<Boolean> addJobParams(JobParamDO jobParamDO) {
        try {
            jenkinsDao.addJobParam(jobParamDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> updateJobParams(JobParamDO jobParamDO) {
        try {
            jenkinsDao.updateJobParam(jobParamDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delJob(long id) {
        try {
            jenkinsDao.delJenkinsJob(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public TableVO<List<JenkinsJobBuildVO>> queryJobBuildsPage(String jobName, int buildNumber, int page, int length) {
        long size = jenkinsDao.getJobBuildsSize(jobName, buildNumber);
        List<JenkinsJobBuildDO> list = jenkinsDao.getJobBuildsPage(jobName, buildNumber, page * length, length);
        List<JenkinsJobBuildVO> voList = new ArrayList<>();
        String jenkinsHost = acqJenkinsHost();
        for (JenkinsJobBuildDO jobBuildDO : list) {
            List<JobNoteDO> jobNotesList = jenkinsDao.queryJobNoteByJobNameAndBuildNumber(jobBuildDO.getJobName(), jobBuildDO.getBuildNumber());
            List<JobNoteVO> jobNotesVOList = acqJobNotes(jobBuildDO, jobNotesList);

            GitlabWebHooksDO webHooks = gitlabDao.queryWebHooksById(jobBuildDO.getWebHookId());
            List<JobBuildParamDO> paramList = jenkinsDao.queryJobBuildParamByBuildsId(jobBuildDO.getId());
            List<BuildArtifactDO> artifacts = jenkinsDao.queryBuildArtifactByJobBuildsId(jobBuildDO.getId());
            // System.err.println("jobBuildsId="+jobBuildDO.getId());
            JenkinsJobBuildVO jenkinsJobBuildVO = new JenkinsJobBuildVO(jobBuildDO, jobNotesVOList, webHooks, paramList, artifacts);
            // 插入二维码地址
            String qrcode = acqQrcode(paramList, artifacts, jobBuildDO.getBuildType());
            jenkinsJobBuildVO.setQrcode(qrcode);
            voList.add(jenkinsJobBuildVO);
        }
        return new TableVO<>(size, voList);
    }

    private List<JobNoteVO> acqJobNotes(JenkinsJobBuildDO jobBuildDO, List<JobNoteDO> jobNotesList) {
        List<JobNoteVO> jobNotesVOList = new ArrayList<>();
        if (jobBuildDO.getBuildNumber() != JenkinsJobBuildDO.BUILD_NUMBER_QUEUE) {
            for (JobNoteDO jobNoteDO : jobNotesList) {
                JobNoteVO jobNoteVO = new JobNoteVO(jobNoteDO, jenkinsHost, jobBuildDO.getBuildType());
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                    Date createDate = format.parse(jobNoteDO.getGmtCreate());
                    jobNoteVO.setTimeView(TimeViewUtils.format(createDate));
                } catch (Exception e) {
                }
                jobNotesVOList.add(jobNoteVO);
            }
        }
        return jobNotesVOList;
    }

    @Override
    public TableVO<List<JenkinsProjectsVO>> getProjectsPage(String projectName, String content, int buildType, int page, int length) {
        long size = jenkinsDao.getProjectsSize(projectName, content, buildType);
        List<JenkinsProjectsDO> list = jenkinsDao.getProjectsPage(projectName, content, buildType, page * length, length);
        List<JenkinsProjectsVO> voList = new ArrayList<>();
        for (JenkinsProjectsDO jenkinsProjectsDO : list) {
            List<JenkinsProjectsEnvVO> envs = queryProjectsEnv(jenkinsProjectsDO.getId());
            List<BaseParamDO> baseParams = jenkinsDao.queryBaseParamByProjectId(jenkinsProjectsDO.getId());
            voList.add(new JenkinsProjectsVO(jenkinsProjectsDO, envs, baseParams));
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public JenkinsJobBuildVO queryJobBuilds(long id) {
        JenkinsJobBuildDO jobBuildDO = jenkinsDao.queryJobBuildsById(id);
        List<JobNoteDO> jobNotesList = jenkinsDao.queryJobNoteByJobNameAndBuildNumber(jobBuildDO.getJobName(), jobBuildDO.getBuildNumber());
        List<JobNoteVO> jobNotesVOList = acqJobNotes(jobBuildDO, jobNotesList);
        GitlabWebHooksDO webHooks = gitlabDao.queryWebHooksById(jobBuildDO.getWebHookId());
        List<JobBuildParamDO> paramList = jenkinsDao.queryJobBuildParamByBuildsId(jobBuildDO.getId());
        List<BuildArtifactDO> artifacts = jenkinsDao.queryBuildArtifactByJobBuildsId(jobBuildDO.getId());
        // System.err.println("jobBuildsId="+jobBuildDO.getId());
        JenkinsJobBuildVO jenkinsJobBuildVO = new JenkinsJobBuildVO(jobBuildDO, jobNotesVOList, webHooks, paramList, artifacts);
        // 插入二维码地址
        String qrcode = acqQrcode(paramList, artifacts, jobBuildDO.getBuildType());
        jenkinsJobBuildVO.setQrcode(qrcode);
        // 插入cdn地址
        String cdnUrl = acqArtifactCdnUrl(artifacts, paramList);
        if (!StringUtils.isEmpty(cdnUrl))
            jenkinsJobBuildVO.setCdnUrl(cdnUrl);

        return jenkinsJobBuildVO;
    }

    private String acqArtifactCdnUrl(JenkinsJobBuildDO jobBuildDO) {
        List<JobBuildParamDO> paramList = jenkinsDao.queryJobBuildParamByBuildsId(jobBuildDO.getId());
        List<BuildArtifactDO> artifacts = jenkinsDao.queryBuildArtifactByJobBuildsId(jobBuildDO.getId());
        return acqArtifactCdnUrl(artifacts, paramList);
    }

    private String acqArtifactCdnUrl(List<BuildArtifactDO> artifacts, List<JobBuildParamDO> paramList) {
        for (BuildArtifactDO artifact : artifacts) {
            try {
                String r[] = artifact.getArtifactsName().split("/");
                String artifactName = r[r.length - 1];
                //String deployPath = params.get("deployPath");
                String deployPath = paramListGet(paramList, "deployPath");
                String uri = deployPath.replace("img0-cdn", "");
                String cdnUrl = "http://cdn.52shangou.com" + uri + "/" + artifactName;
                return cdnUrl;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "";
    }


    private String paramListGet(List<JobBuildParamDO> paramList, String key) {
        for (JobBuildParamDO param : paramList) {
            if (param.getParamName().equalsIgnoreCase(key))
                return param.getParamValue();
        }
        return "";
    }

    private String acqQrcode(List<JobBuildParamDO> paramList, List<BuildArtifactDO> artifacts, int buildType) {
        String qrcode = "";
        if (buildType == GitlabWebHooksDO.HooksTypeEnum.ios.getCode()) {
            for (JobBuildParamDO param : paramList) {
                if (param.getParamName().equals("deployPath")) {
                    String path = param.getParamValue();
                    qrcode = path.replace("img0-cdn", "https://cdn.52shangou.com") + "/package.plist";
                    qrcode = "itms-services://?action=download-manifest&url=" + qrcode;
                    return qrcode;
                }

            }
        }
        if (buildType == GitlabWebHooksDO.HooksTypeEnum.android.getCode()) {
            for (BuildArtifactDO artifact : artifacts) {
                if (!StringUtils.isEmpty(artifact.getArchiveUrl())) {
                    return artifact.getArchiveUrl();
                }
            }
        }
        return qrcode;
    }


    @Override
    public JenkinsProjectsDO saveProject(JenkinsProjectsDO jenkinsProjectsDO) {
        JenkinsProjectsDO project = new JenkinsProjectsDO();
        if (jenkinsProjectsDO == null)
            return project;
        try {
            if (jenkinsProjectsDO.getId() == 0) {
                jenkinsDao.addProjects(jenkinsProjectsDO);
            } else {
                jenkinsDao.updateProjects(jenkinsProjectsDO);
            }
            return jenkinsProjectsDO;
        } catch (Exception e) {
            e.printStackTrace();
            return project;
        }
    }

    @Override
    public BusinessWrapper<Boolean> delProject(long id) {
        try {
            jenkinsDao.delProjects(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveProjectParam(BaseParamDO baseParamDO) {
        if (baseParamDO.getProjectId() == 0) return new BusinessWrapper<Boolean>(false);
        try {
            if (baseParamDO.getId() == 0) {
                jenkinsDao.addBaseParam(baseParamDO);
            } else {
                jenkinsDao.updateBaseParam(baseParamDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delProjectParam(long id) {
        if (id <= 0) return new BusinessWrapper<Boolean>(false);
        try {
            jenkinsDao.delBaseParam(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public List<BaseParamDO> queryProjectParams(long id) {
        return jenkinsDao.queryBaseParamByProjectId(id);
    }

    @Override
    public BusinessWrapper<Boolean> saveProjectsEnv(JenkinsProjectsEnvDO jenkinsProjectsEnvDO) {
        try {
            jenkinsDao.addProjectsEnv(jenkinsProjectsEnvDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public List<JenkinsProjectsEnvVO> queryProjectsEnv(long id) {
        List<BaseParamDO> baseParamList = jenkinsDao.queryBaseParamByProjectId(id);
        List<JenkinsProjectsEnvDO> envList = jenkinsDao.queryProjectsEnvByProjectId(id);
        List<JenkinsProjectsEnvVO> voList = new ArrayList<>();
        for (JenkinsProjectsEnvDO env : envList) {
            for (BaseParamDO baseParamDO : baseParamList) {
                // 可变参数
                if (baseParamDO.getParamType() == BaseParamDO.ParamTypeEnum.changeable.getCode()) {
                    JenkinsEnvParamDO envParamDO = jenkinsDao.queryEnvParamByEnvIdAndParamName(env.getId(), baseParamDO.getParamName());
                    if (envParamDO == null) {
                        envParamDO = new JenkinsEnvParamDO(baseParamDO, env.getId());
                        jenkinsDao.addEnvParam(envParamDO);
                    }
                }
            }
            List<JenkinsEnvParamDO> params = jenkinsDao.queryEnvParamByEnvId(env.getId());
            JenkinsProjectsEnvVO envVO = new JenkinsProjectsEnvVO(env, params);
            voList.add(envVO);
        }
        return voList;
    }

    @Override
    public BusinessWrapper<Boolean> delProjectsEnv(long id, long projectId, int envType) {
        try {

            List<JenkinsProjectsEnvDO> envs = jenkinsDao.queryProjectsEnvByProjectIdAndEnvType(projectId, envType);
            for (JenkinsProjectsEnvDO env : envs)
                jenkinsDao.delProjectsEnv(env.getId());
            jenkinsDao.delProjectsEnv(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveProjectsEnvParams(JenkinsProjectsEnvVO jenkinsProjectsEnvVO) {
        try {
            List<JenkinsEnvParamDO> params = jenkinsProjectsEnvVO.getParams();
            for (JenkinsEnvParamDO param : params)
                jenkinsDao.updateEnvParam(param);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveProjectJob(long id) {
        JenkinsProjectsEnvDO projectsEnvDO = jenkinsDao.queryProjectsEnvById(id);
        long projectId = projectsEnvDO.getProjectId();
        JenkinsProjectsDO projectsDO = jenkinsDao.queryProjectsById(projectId);
        String jobName = GitlabWebHooksDO.HooksTypeEnum.getHooksTypeName(projectsDO.getBuildType());
        jobName += "_" + projectsDO.getProjectName();
        HashMap<String, String> paramMap = new HashMap<>();
        List<BaseParamDO> baseParams = jenkinsDao.queryBaseParamByProjectId(projectId);
        for (BaseParamDO baseParamDO : baseParams)
            paramMap.put(baseParamDO.getParamName(), baseParamDO.getParamValue());
        List<JenkinsEnvParamDO> envParams = jenkinsDao.queryEnvParamByEnvId(id);
        for (JenkinsEnvParamDO envParamDO : envParams)
            paramMap.put(envParamDO.getParamName(), envParamDO.getParamValue());
        String name = paramMap.get("name");
        if (!StringUtils.isEmpty(name)) {
            jobName += "." + name;
        }
        jobName += "." + JenkinsJobDO.JobEnvTypeEnum.getJobEnvTypeName(projectsEnvDO.getEnvType());
        JenkinsJobDO jenkinsJobDO = new JenkinsJobDO(projectsDO, projectsEnvDO, jobName);

        try {
            JenkinsJobDO job = jenkinsDao.queryJenkinsJobByName(jobName);
            if (job == null) {
                jenkinsDao.addJenkinsJob(jenkinsJobDO);
            } else {
                jenkinsDao.updateJenkinsJob(jenkinsJobDO);
            }
            long jobId = jenkinsJobDO.getId();
            // 更新关联id
            projectsEnvDO.setJobsId(jobId);
            jenkinsDao.updateProjectsEnv(projectsEnvDO);

            // 清理老参数
            List<JobParamDO> jobParams = jenkinsDao.queryJobParamByJobId(jobId);
            for (JobParamDO jobParamDO : jobParams)
                jenkinsDao.delJobParam(jobParamDO.getId());
            // 插入新的参数
            for (BaseParamDO baseParamDO : baseParams) {
                if (baseParamDO.getParamType() == BaseParamDO.ParamTypeEnum.inherit.getCode()) {
                    JobParamDO param = new JobParamDO(baseParamDO, jobId);
                    jenkinsDao.addJobParam(param);
                }
                if (baseParamDO.getParamType() == BaseParamDO.ParamTypeEnum.changeable.getCode()) {
                    for (JenkinsEnvParamDO envParamDO : envParams) {
                        if (baseParamDO.getParamName().equals(envParamDO.getParamName())) {
                            JobParamDO param = new JobParamDO(baseParamDO, envParamDO, jobId);
                            jenkinsDao.addJobParam(param);
                            break;
                        }
                    }
                }
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
