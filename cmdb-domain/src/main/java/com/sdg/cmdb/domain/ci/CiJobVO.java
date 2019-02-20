package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.gitlab.api.models.GitlabBranch;

import java.io.Serializable;
import java.util.List;

public class CiJobVO extends CiJobDO implements Serializable {
    private static final long serialVersionUID = 914462153331865408L;

    private List<GitlabBranch> branchList;

    private List<CiJobParamDO> paramList;

    private CiTemplateDO ciTemplateDO;

    private CiTemplateDO ciDeployTemplateDO;

    private CiAppVO ciAppVO;

    private String versionName;

    private String versionDesc;

    private String jobTemplateMd5;

    private String deployJobTemplateMd5;

    private long deployArtifactId;

    public CiJobVO() {
    }

    public CiJobVO(CiJobDO ciJobDO, List<GitlabBranch> branchList, List<CiJobParamDO> paramList, CiAppVO ciAppVO) {
        setId(ciJobDO.getId());
        setName(ciJobDO.getName());
        setContent(ciJobDO.getContent());
        setAppId(ciJobDO.getAppId());
        setCiType(ciJobDO.getCiType());
        setBranch(ciJobDO.getBranch());
        setHostPattern(ciJobDO.getHostPattern());
        setEnvType(ciJobDO.getEnvType());
        setJobName(ciJobDO.getJobName());
        setJobTemplate(ciJobDO.getJobTemplate());
        setJobVersion(ciJobDO.getJobVersion());
        setRollbackType(ciJobDO.getRollbackType());
        setDeployJobTemplate(ciJobDO.getDeployJobTemplate());
        setDeployJobName(ciJobDO.getDeployJobName());
        setDeployJobVersion(ciJobDO.getDeployJobVersion());
        setGmtCreate(ciJobDO.getGmtCreate());
        setGmtModify(ciJobDO.getGmtModify());
        this.branchList = branchList;
        this.paramList = paramList;
        this.ciAppVO = ciAppVO;
    }

    public List<GitlabBranch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<GitlabBranch> branchList) {
        this.branchList = branchList;
    }

    public List<CiJobParamDO> getParamList() {
        return paramList;
    }

    public void setParamList(List<CiJobParamDO> paramList) {
        this.paramList = paramList;
    }

    public CiAppVO getCiAppVO() {
        return ciAppVO;
    }

    public void setCiAppVO(CiAppVO ciAppVO) {
        this.ciAppVO = ciAppVO;
    }

    public CiTemplateDO getCiTemplateDO() {
        return ciTemplateDO;
    }

    public void setCiTemplateDO(CiTemplateDO ciTemplateDO) {
        this.ciTemplateDO = ciTemplateDO;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getJobTemplateMd5() {
        return jobTemplateMd5;
    }

    public void setJobTemplateMd5(String jobTemplateMd5) {
        this.jobTemplateMd5 = jobTemplateMd5;
    }

    public CiTemplateDO getCiDeployTemplateDO() {
        return ciDeployTemplateDO;
    }

    public void setCiDeployTemplateDO(CiTemplateDO ciDeployTemplateDO) {
        this.ciDeployTemplateDO = ciDeployTemplateDO;
    }

    public String getDeployJobTemplateMd5() {
        return deployJobTemplateMd5;
    }

    public void setDeployJobTemplateMd5(String deployJobTemplateMd5) {
        this.deployJobTemplateMd5 = deployJobTemplateMd5;
    }

    public long getDeployArtifactId() {
        return deployArtifactId;
    }

    public void setDeployArtifactId(long deployArtifactId) {
        this.deployArtifactId = deployArtifactId;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
