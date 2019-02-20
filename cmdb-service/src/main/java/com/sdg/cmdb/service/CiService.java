package com.sdg.cmdb.service;


import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Job;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ci.*;
import com.sdg.cmdb.domain.ci.ciStatus.CiStatusVO;
import com.sdg.cmdb.domain.ci.jenkins.Notify;
import com.sdg.cmdb.domain.server.HostPatternCi;
import com.sdg.cmdb.domain.server.ServerDO;
import org.gitlab.api.models.GitlabCommit;


import java.util.List;

/**
 * Created by liangjian on 2017/2/17.
 */
public interface CiService {


    BusinessWrapper<Boolean> buildNotify(Notify notify);


    CiAppVO getApp(long id);

    List<CiAppVO> queryMyApp(String projectName);

    BusinessWrapper<Boolean> saveApp(CiAppVO ciAppVO);

    BusinessWrapper<Boolean> saveJob(CiJobVO ciJobVO);

    List<CiJobVO> queryJob(long appId);

    BusinessWrapper<Boolean> createJob(long jobId);

    CiJobVO getJob(long id);

    BusinessWrapper<CiJobVO> saveParam(CiJobParamDO ciJobParamDO);

    BusinessWrapper<CiJobVO> delParam(long id);


    BusinessWrapper<Boolean> buildJob(long id);

    List<ServerDO> getHostPattern(CiBuildDO ciBuildDO, long serverGroupId);

    /**
     * 查询某次构建的参数
     *
     * @param ciBuildDO
     * @param paramName
     * @return
     */
    String getBuildParamValue(CiBuildDO ciBuildDO, String paramName);

    BusinessWrapper<Boolean> buildJob(CiJobVO ciJobVO);

    BusinessWrapper<Boolean> deployJob(CiJobVO ciJobVO);

    JenkinsVersion getJenkinsVersion();

    List<Job> getJenkinsTemplate();

    List<HostPatternCi> getHostPatternCi(long groupId);

    TableVO<List<CiTemplateVO>> getTemplatePage(String name, int appType, int ciType, int page, int length);

    BusinessWrapper<CiTemplateDO> saveTemplate(CiTemplateDO ciTemplateDO);

    BusinessWrapper<Boolean> updateTemplate(long jobId, int type);

    BusinessWrapper<Boolean> updateTemplates(long templateId);

    BusinessWrapper<Boolean> delTemplate(long id);

    TableVO<List<CiBuildVO>> getBuildPage(long jobId, int page, int length);

    TableVO<List<CiBuildVO>> getDeployPage(long jobId, int page, int length);


    List<CiBuildVO> getArtifact(long jobId, int buildNumber);

    List<GitlabCommit> getBuildCommit(long jobId, String jobName, String branch);

}
