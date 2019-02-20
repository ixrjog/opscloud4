package com.sdg.cmdb.domain.ci;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CiBuildVO extends CiBuildDO implements Serializable {
    private static final long serialVersionUID = -6672713240140106681L;

    public CiBuildVO() {
    }

    public CiBuildVO(CiBuildDO ciBuildDO) {
        setId(ciBuildDO.getId());
        setJobId(ciBuildDO.getJobId());
        setJobName(ciBuildDO.getJobName());
        setDisplayName(ciBuildDO.getDisplayName());
        setMail(ciBuildDO.getMail());
        setMobile(ciBuildDO.getMobile());
        setBuildNumber(ciBuildDO.getBuildNumber());
        setVersionName(ciBuildDO.getVersionName());
        setVersionDesc(ciBuildDO.getVersionDesc());
        setBuildPhase(ciBuildDO.getBuildPhase());
        setBuildStatus(ciBuildDO.getBuildStatus());
        setGmtCreate(ciBuildDO.getGmtCreate());
        setGmtModify(ciBuildDO.getGmtModify());
    }

    private String badgeIcon;
    private List<BuildNotifyDO> notifyList;
    private List<BuildArtifactVO> artifactList;
    private List<CiBuildCommitDO> commitList;
    private JobParams jobParams;
    private String console;
    private CiJobVO ciJobVO;
    private List<CiDeployHistoryDO> serverList;

}
