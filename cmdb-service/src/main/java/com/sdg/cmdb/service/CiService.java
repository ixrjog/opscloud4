package com.sdg.cmdb.service;


import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Job;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ci.*;
import com.sdg.cmdb.domain.ci.jenkins.Notify;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.server.HostPatternCi;
import com.sdg.cmdb.domain.server.ServerDO;
import org.gitlab.api.models.GitlabCommit;

import java.util.List;

/**
 * Created by liangjian on 2017/2/17.
 */
public interface CiService {

    BusinessWrapper<Boolean> buildNotify(Notify notify);

    void autoBuild(GitlabWebHooksDO gitlabWebHooksDO);

    CiAppVO getApp(long id);

    List<CiAppVO> queryMyApp(String queryName,long labelId);

    /**
     * 按标签查询
     * @param labelId
     * @param queryName
     * @return
     */
    List<CiAppDO> queryAppByLabel(long labelId, String queryName);

    List<CiAppVO> queryUnauthApp(String queryName, int appType);

    List<CiAppVO> queryMyAppByType(String queryName, int appType);

    CiAppVO queryAppByName(String appName);

    List<CiAppVO> queryUserApp(long userId);

    BusinessWrapper<Boolean> saveApp(CiAppVO ciAppVO);

    boolean ciAppAuth(CiAppAuthDO authDO);

    BusinessWrapper<Boolean> delApp(long ciAppId);

    CiJobVO saveJob(CiJobVO ciJobVO);

    BusinessWrapper<Boolean> saveJobParams(CiJobVO ciJobVO);

    /**
     * 查询当前Job详情
     *
     * @param appId
     * @return
     */
    List<CiJobVO> queryJob(long appId);

    DeployInfo queryDeployInfo(long appId);

    BusinessWrapper<Boolean> createJob(long jobId);

    CiJobVO getJob(long id);

    BusinessWrapper<CiJobVO> saveParam(CiJobParamDO ciJobParamDO);

    BusinessWrapper<CiJobVO> delParam(long id);

    BusinessWrapper<Boolean> buildJob(long id);

    /**
     * 删除Job
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delJob(long id);

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

    /**
     * 扫描模版，更新字符重新生成Hash
     *
     * @return
     */
    BusinessWrapper<Boolean> scanTpls();

    BusinessWrapper<CiTemplateDO> saveTemplate(CiTemplateDO ciTemplateDO);

    BusinessWrapper<Boolean> updateTemplate(long jobId, int type);

    BusinessWrapper<Boolean> updateTemplates(long templateId);

    /**
     * 预览模版Xml
     *
     * @param templateId
     * @return
     */
    String previewTemplate(long templateId);

    BusinessWrapper<Boolean> delTemplate(long id);

    TableVO<List<CiBuildVO>> getBuildPage(long jobId, int page, int length);

    CiBuildVO getBuildDetail(long buildId);

    BusinessWrapper<Boolean> checkBuildDetail(long buildId);

    TableVO<List<CiBuildVO>> getDeployPage(long jobId, int page, int length);

    List<CiBuildVO> getArtifact(long jobId, int buildNumber);

    List<CiBuildVO> getArtifact(long jobId, String versionName);

    List<GitlabCommit> getBuildCommit(long jobId, String jobName, String branch);

    int getMyAppSize();

    CiAppDO getCiAppByBuildId(long buildId);

    CiAppDO getCiAppByJobId(long jobId);

    LabelDO addLabel(LabelDO labelDO);

    LabelDO saveLabel(LabelDO labelDO);

    List<LabelVO> queryLabel();

    BusinessWrapper<Boolean> addLabelMember(LabelMemberDO labelMemberDO);

    BusinessWrapper<Boolean> delLabelMember(long id);

    List<LabelMemberVO> getLabelMember(long id);
}
