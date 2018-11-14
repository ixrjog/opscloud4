package com.sdg.cmdb.domain.nginx;

import java.io.Serializable;
import java.util.List;

public class VhostVO extends VhostDO implements Serializable {
    private static final long serialVersionUID = 5202258284556972461L;

    private List<VhostEnvVO> envList;

    public List<VhostEnvVO> getEnvList() {
        return envList;
    }

    public void setEnvList(List<VhostEnvVO> envList) {
        this.envList = envList;
    }

    public VhostVO() {

    }

    public VhostVO(VhostDO vhostDO) {
        setId(vhostDO.getId());
        setServerName(vhostDO.getServerName());
        setServerKey(vhostDO.getServerKey());
        setContent(vhostDO.getContent());
        setGmtCreate(vhostDO.getGmtCreate());
        setGmtModify(vhostDO.getGmtModify());
    }

    public VhostVO(VhostDO vhostDO, List<VhostEnvVO> envList) {
        setId(vhostDO.getId());
        setServerName(vhostDO.getServerName());
        setServerKey(vhostDO.getServerKey());
        setContent(vhostDO.getContent());
        setGmtCreate(vhostDO.getGmtCreate());
        setGmtModify(vhostDO.getGmtModify());
        this.envList = envList;
    }
}
