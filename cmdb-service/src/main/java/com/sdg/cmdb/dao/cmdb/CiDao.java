package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ci.*;
import com.sdg.cmdb.domain.ci.ciStatus.CiDeployVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiProjectVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiRepoVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiDeployUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/2/17.
 */
@Component
public interface CiDao {


    /**
     * 统计部署次数
     *
     * @return
     */
    int getCiDeployedCnt();

    /**
     * 统计项目总数
     *
     * @return
     */
    int getCiProjectCnt();

    /**
     * 统计项目部署Top
     *
     * @return
     */
    List<CiProjectVO> statusCiDeployProject();

    /**
     * 统计部署次数最多的用户Top
     *
     * @return
     */
    List<CiDeployUserVO> statusCiDeployUser();

    /**
     * 统计部署最多的仓库Top
     *
     * @return
     */
    List<CiRepoVO> statusCiRepo();

    /**
     * 统计最新部署
     *
     * @return
     */
    List<CiDeployVO> statusCiDeploy();


//    List<CiAppDO> getByAuthUsername(@Param("username") String username);

    List<CiAppDO> getCiAppByAuthUserId(@Param("userId") long userId, @Param("projectName") String projectName);

    int checkCiAppAuth(@Param("userId") long userId, @Param("appId") long appId);

    int addCiApp(CiAppDO ciAppDO);

    int updateCiApp(CiAppDO ciAppDO);

    CiAppDO getCiApp(@Param("id") long id);

    int addCiJob(CiJobDO ciJobDO);

    int updateCiJob(CiJobDO ciJobDO);

    CiJobDO getCiJob(@Param("id") long id);

    List<CiJobDO> getCiJobByAppId(@Param("appId") long appId);

    CiJobDO getCiJobByJobName(@Param("jobName") String jobName);

    List<CiJobDO> queryCiJobByJobTemplate(@Param("jobTemplate") String jobTemplate);

    List<CiJobDO> queryCiJobByDeployJobTemplate(@Param("deployJobTemplate") String deployJobTemplate);

    int addJobParam(CiJobParamDO ciJobParamDO);

    int updateJobParam(CiJobParamDO ciJobParamDO);

    int delJobParam(@Param("id") long id);

    CiJobParamDO getJobParam(@Param("id") long id);

    List<CiJobParamDO> queryJobParamByJobId(@Param("jobId") long jobId);

    int addTemplate(CiTemplateDO ciTemplateDO);

    int updateTemplate(CiTemplateDO ciTemplateDO);

    int delTemplate(@Param("id") long id);

    long getTemplateSize(
            @Param("name") String name,
            @Param("appType") int appType,
            @Param("ciType") int ciType
    );

    List<CiTemplateDO> getTemplatePage(@Param("name") String name,
                                       @Param("appType") int appType,
                                       @Param("ciType") int ciType,
                                       @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    CiTemplateDO getTemplate(@Param("id") long id);

    CiTemplateDO getTemplateByName(@Param("name") String name);


    int addBuild(CiBuildDO ciBuildDO);

    int updateBuild(CiBuildDO ciBuildDO);

    int delBuild(@Param("id") long id);

    long getBuildSize(
            @Param("jobId") long jobId, @Param("jobName") String jobName
    );

    List<CiBuildDO> getBuildPage(@Param("jobId") long jobId, @Param("jobName") String jobName,
                                 @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    CiBuildDO getBuild(@Param("id") long id);

    /**
     * 查询队列中的任务
     *
     * @param jobName
     * @return
     */
    CiBuildDO getBuildByQueue(
            @Param("jobName") String jobName);

    /**
     * 查询Job Build
     *
     * @param jobName
     * @param buildNumber
     * @return
     */
    CiBuildDO getBuildByUnique(
            @Param("jobName") String jobName, @Param("buildNumber") int buildNumber);

    int addBuildNotify(BuildNotifyDO buildNotifyDO);

    int updateBuildNotify(BuildNotifyDO buildNotifyDO);

    int delBuildNotify(@Param("id") long id);

    List<BuildNotifyDO> queryBuildNotifyByBuildId(@Param("buildId") long buildId);

    String queryNotifyStartedByJobName(@Param("jobName") String jobName);

    String queryNotifyFinalizedByJobName(@Param("jobName") String jobName);


    /**
     * 查询唯一build通知
     *
     * @param buildNotifyDO
     * @return
     */
    BuildNotifyDO getBuildNotifyByUnique(BuildNotifyDO buildNotifyDO);

    BuildNotifyDO getBuildNotify(@Param("id") long id);

    int addBuildArtifact(BuildArtifactDO buildArtifactDO);

    // TODO 校验是否保存构件
    int checkBuildArtifactByUnique(BuildArtifactDO buildArtifactDO);

    List<BuildArtifactDO> queryBuildArtifactByOssPath(@Param("ossPath") String ossPath);


    List<BuildArtifactDO> queryBuildArtifactByBuildId(@Param("buildId") long buildId);

    List<BuildArtifactDO> queryBuildArtifactByJobName(@Param("jobName") String jobName, @Param("buildNumber") int buildNumber);

    BuildArtifactDO getBuildArtifact(@Param("id") long id);

    int delBuildArtifact(@Param("id") long id);

    int addDeploy(CiDeployDO ciDeployDO);

    int updateDeploy(CiDeployDO ciDeployDO);

    CiDeployDO getDeployByServerId(@Param("serverId") long serverId);

    int addDeployHistory(CiDeployHistoryDO ciDeployHistoryDO);

    int updateDeployHistory(CiDeployHistoryDO ciDeployHistoryDO);

    CiDeployHistoryDO getDeployHistoryByUnique(@Param("serverId") long serverId, @Param("buildId") long buildId);

    CiDeployHistoryDO getDeployHistory(@Param("id") long id);

    List<CiDeployHistoryDO> queryDeployHistoryByServerId(@Param("serverId") long serverId);

    // TODO 查询构建任务的服务器版本信息
    List<CiDeployHistoryDO> queryDeployHistoryByBuildId(@Param("buildId") long buildId);

    int addCiBuildCommit(CiBuildCommitDO ciBuildCommitDO);

    List<CiBuildCommitDO> queryCiBuildCommitByBuildId(@Param("buildId") long buildId);

    int delCiBuildCommit(@Param("id") long id);

}
