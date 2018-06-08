package com.sdg.cmdb.dao.cmdb;


import com.sdg.cmdb.domain.gitlab.RefsDO;
import com.sdg.cmdb.domain.gitlab.RepositoryDO;
import com.sdg.cmdb.domain.jenkins.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public interface JenkinsDao {

    int addBuildArtifact(BuildArtifactDO artifactDO);

    List<BuildArtifactDO> queryBuildArtifactByJobBuildsId(@Param("jobBuildsId") long jobBuildsId);

    BuildArtifactDO queryBuildArtifact(@Param("jobBuildsId") long jobBuildsId, @Param("artifactsName") String artifactsName, @Param("archiveUrl") String archiveUrl);

    int addJenkinsJob(JenkinsJobDO jenkinsJobDO);

    int updateJenkinsJob(JenkinsJobDO jenkinsJobDO);

    int delJenkinsJob(@Param("id") long id);

    List<JobParamDO> queryJobParamByJobId(@Param("jobId") long jobId);

    JobParamDO queryJobParamByJobIdAndName(@Param("jobId") long jobId, @Param("paramName") String paramName);

    int addJobParam(JobParamDO jobParamDO);

    int addJobBuildParam(JobBuildParamDO jobBuildParamDO);

    List<JobBuildParamDO> queryJobBuildParamByBuildsId(@Param("buildsId") long buildsId);

    JobBuildParamDO queryJobBuildParamByBuildsIdAndName(@Param("buildsId") long buildsId, @Param("paramName") String paramName);

    int updateJobParam(JobParamDO jobParamDO);

    int delJobParam(@Param("id") long id);

    JenkinsJobDO queryJenkinsJobByName(@Param("jobName") String jobName);

    JenkinsJobDO queryJenkinsJobById(@Param("id") long id);

    int addJenkinsJobBuild(JenkinsJobBuildDO jenkinsJobBuildDO);

    int updateJenkinsJobBuild(JenkinsJobBuildDO jenkinsJobBuildDO);

    int addJobNote(JobNoteDO jobNoteDO);

    int updateJobNote(JobNoteDO jobNoteDO);

    long getJobsSize(
            @Param("jobName") String jobName,
            @Param("jobEnvType") int jobEnvType,
            @Param("buildType") int buildType);

    List<JenkinsJobDO> getJobsPage(
            @Param("jobName") String jobName,
            @Param("jobEnvType") int jobEnvType,
            @Param("buildType") int buildType,
            @Param("pageStart") long pageStart, @Param("length") int length);

    long getJobBuildsSize(
            @Param("jobName") String jobName,
            @Param("buildNumber") int buildNumber);

    List<JenkinsJobBuildDO> getJobBuildsPage(
            @Param("jobName") String jobName,
            @Param("buildNumber") int buildNumber,
            @Param("pageStart") long pageStart, @Param("length") int length);

    JenkinsJobBuildDO queryJobBuildsByJobNameAndBuildNumber(
            @Param("jobName") String jobName,
            @Param("buildNumber") int buildNumber);

    /**
     * 查询队列中的任务
     *
     * @param jobName
     * @return
     */
    JenkinsJobBuildDO queryJobBuildsByQueue(
            @Param("jobName") String jobName);


    JenkinsJobBuildDO queryJobBuildsById(
            @Param("id") long id);

    List<JobNoteDO> queryJobNoteByJobNameAndBuildNumber(@Param("jobName") String jobName, @Param("buildNumber") int buildNumber);

    JobNoteDO queryJobNote(@Param("jobName") String jobName, @Param("buildNumber") int buildNumber, @Param("buildPhase") String buildPhase);

    RepositoryDO queryRepositoryByUrl(@Param("repositoryUrl") String repositoryUrl);

    int addRepository(RepositoryDO repositoryDO);

    List<RefsDO> queryRefsByRepoId(@Param("repositoryId") long repositoryId);

    RefsDO queryRefsByRefAndRefType(@Param("ref") String ref, @Param("refType") int refType);

    int delRefs(@Param("id") long id);

    int addRefs(RefsDO refsDO);

    long getProjectsSize(
            @Param("projectName") String projectName,
            @Param("content") String content,
            @Param("buildType") int buildType);

    List<JenkinsProjectsDO> getProjectsPage(
            @Param("projectName") String projectName,
            @Param("content") String content,
            @Param("buildType") int buildType,
            @Param("pageStart") long pageStart, @Param("length") int length);

    int addProjects(JenkinsProjectsDO jenkinsProjectsDO);

    int updateProjects(JenkinsProjectsDO jenkinsProjectsDO);

    JenkinsProjectsDO queryProjectsById(@Param("id") long id);

    int delProjects(@Param("id") long id);

    int addBaseParam(BaseParamDO baseParamDO);

    int updateBaseParam(BaseParamDO baseParamDO);

    int delBaseParam(@Param("id") long id);

    List<BaseParamDO> queryBaseParamByProjectId(@Param("projectId") long projectId);

    int addProjectsEnv(JenkinsProjectsEnvDO jenkinsProjectsEnvDO);

    List<JenkinsProjectsEnvDO> queryProjectsEnvByProjectId(@Param("projectId") long projectId);

    List<JenkinsProjectsEnvDO> queryProjectsEnvByProjectIdAndEnvType(@Param("projectId") long projectId, @Param("envType") long envType);

    JenkinsProjectsEnvDO queryProjectsEnvById(@Param("id") long id);

    int updateProjectsEnv(JenkinsProjectsEnvDO jenkinsProjectsEnvDO);

    int delProjectsEnv(@Param("id") long id);

    List<JenkinsEnvParamDO> queryEnvParamByEnvId(@Param("envId") long envId);

    JenkinsEnvParamDO queryEnvParamByEnvIdAndParamName(@Param("envId") long envId, @Param("paramName") String paramName);

    int addEnvParam(JenkinsEnvParamDO envParamDO);

    int updateEnvParam(JenkinsEnvParamDO envParamDO);

    int delEnvParam(@Param("id") long id);

    List<JobUserDO> queryJobUserByJobId(@Param("jobId") long jobId);

    int delJobUser(@Param("id") long id);

    int addJobUser(JobUserDO jobUserDO);

}

