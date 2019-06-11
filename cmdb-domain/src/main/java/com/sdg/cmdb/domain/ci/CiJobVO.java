package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sdg.cmdb.domain.PublicType;
import com.sdg.cmdb.domain.ci.android.AndroidChannel;
import com.sdg.cmdb.domain.server.EnvType;
import lombok.Data;
import org.gitlab.api.models.GitlabBranch;

import java.io.Serializable;
import java.util.List;

@Data
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
    private AndroidBuild androidBuild;
    private EnvType env;
    private List<BuildDetail> buildDetails;

    // 多渠道
    private int channelType ;
    private List<AndroidChannel> channelGroup;


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
        setAtAll(ciJobDO.isAtAll());
        this.env = new EnvType(getEnvType());
        setJobName(ciJobDO.getJobName());
        setJobTemplate(ciJobDO.getJobTemplate());
        setJobVersion(ciJobDO.getJobVersion());
        setRollbackType(ciJobDO.getRollbackType());
        setDeployJobTemplate(ciJobDO.getDeployJobTemplate());
        setDeployJobName(ciJobDO.getDeployJobName());
        setDeployJobVersion(ciJobDO.getDeployJobVersion());
        setParamsYaml(ciJobDO.getParamsYaml());
        setAutoBuild(ciJobDO.isAutoBuild());
        setGmtCreate(ciJobDO.getGmtCreate());
        setGmtModify(ciJobDO.getGmtModify());
        this.branchList = branchList;
        this.paramList = paramList;
        this.ciAppVO = ciAppVO;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
