package com.sdg.cmdb.service;

import com.offbytwo.jenkins.model.Job;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.gitlab.RefsVO;
import com.sdg.cmdb.domain.jenkins.*;
import com.sdg.cmdb.domain.server.ServerVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JenkinsService {


    String acqJenkinsHost();

    BusinessWrapper<Boolean> jobNotes(JobNoteVO jobNoteVO);

    void dingtalkNotes(JobNoteDO jobNoteDO, JenkinsJobBuildDO jobBuildDO);

    /**
     * 执行jenkins job
     *
     * @param webHooks
     * @param params
     * @param envType
     * @return
     */
    boolean buildFtJob(GitlabWebHooksDO webHooks, HashMap<String, String> params, int envType);

    /**
     * 创建job
     *
     * @param jobName
     * @param envType
     * @return
     */
    boolean createFtJob(String jobName, int envType);

    boolean createIosJob(String jobName);

    boolean createAndroidJob(String jobName);

    boolean createTestJob(JenkinsJobDO jenkinsJobDO);

    public Map<String, Job> getJobs();

    TableVO<List<GitlabWebHooksDO>> getWebHooksPage(String projectName, String repositoryName, int webHooksType, int triggerBuild, int page, int length);


    TableVO<List<JenkinsJobVO>> queryJobsPage(String jobName, int jobEnvType, int buildType, int page, int length);


    BusinessWrapper<Boolean> buildJob(long id);

    BusinessWrapper<Boolean> createJob(long id);

    /**
     * ios构建需要选择分支
     *
     * @param id
     * @param mbranch
     * @param buildType
     * @return
     */
    BusinessWrapper<Boolean> buildJob(long id, String mbranch, String buildType);


    RefsVO queryJobRefs(long id);

    RefsVO changeJobRefs(long id,String ref,int type);

    RefsVO updateJobRefs(long id);

    /**
     * 任务详情重新build
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> rebuildJob(long id);

    /**
     * 官网发布
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> appLink(long id);

    List<JobParamDO> queryJobParams(long jobId);

    BusinessWrapper<Boolean> delJobParams(long id);

    BusinessWrapper<Boolean> addJobParams(JobParamDO jobParamDO);

    BusinessWrapper<Boolean> updateJobParams(JobParamDO jobParamDO);

    JenkinsJobDO saveJob(JenkinsJobVO jenkinsJobVO);

    BusinessWrapper<Boolean> delJob(long id);

    TableVO<List<JenkinsJobBuildVO>> queryJobBuildsPage(String jobName, int buildNumber, int page, int length);

    JenkinsJobBuildVO queryJobBuilds(long id);


    TableVO<List<JenkinsProjectsVO>> getProjectsPage(String projectName, String content, int buildType, int page, int length);

    JenkinsProjectsDO saveProject(JenkinsProjectsDO jenkinsProjectsDO);

    BusinessWrapper<Boolean> delProject(long id);

    BusinessWrapper<Boolean> saveProjectParam(BaseParamDO baseParamDO);

    BusinessWrapper<Boolean> delProjectParam(long id);

    List<BaseParamDO> queryProjectParams(long id);

    BusinessWrapper<Boolean> saveProjectsEnv(JenkinsProjectsEnvDO jenkinsProjectsEnvDO);

    List<JenkinsProjectsEnvVO> queryProjectsEnv(long id);

    BusinessWrapper<Boolean> delProjectsEnv(long id,long projectId,int envType);

    BusinessWrapper<Boolean> saveProjectsEnvParams(JenkinsProjectsEnvVO jenkinsProjectsEnvVO);

    BusinessWrapper<Boolean> saveProjectJob(long id);
}
