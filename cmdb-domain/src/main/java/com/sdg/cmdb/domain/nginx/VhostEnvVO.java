package com.sdg.cmdb.domain.nginx;

import java.io.Serializable;
import java.util.List;

public class VhostEnvVO extends VhostEnvDO implements Serializable {
    private static final long serialVersionUID = -372348389639325535L;

    private List<EnvFileDO> envFiles;

    public VhostEnvVO() {

    }

    public VhostEnvVO(VhostEnvDO vhostEnvDO, List<EnvFileDO> envFiles) {

        setId(vhostEnvDO.getId());
        setVhostId(vhostEnvDO.getVhostId());
        setEnvType(vhostEnvDO.getEnvType());
        setConfPath(vhostEnvDO.getConfPath());
        setAutoBuild(vhostEnvDO.isAutoBuild());
        setContent(vhostEnvDO.getContent());
        setGmtCreate(vhostEnvDO.getGmtCreate());
        setGmtModify(vhostEnvDO.getGmtModify());

        this.envFiles = envFiles;

    }

    public List<EnvFileDO> getEnvFiles() {
        return envFiles;
    }

    public void setEnvFiles(List<EnvFileDO> envFiles) {
        this.envFiles = envFiles;
    }
}
