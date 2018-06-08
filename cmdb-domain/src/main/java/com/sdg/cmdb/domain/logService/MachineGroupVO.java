package com.sdg.cmdb.domain.logService;

import com.aliyun.openservices.log.common.MachineGroup;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerVO;

import java.io.Serializable;
import java.util.List;

public class MachineGroupVO implements Serializable {


    private static final long serialVersionUID = -1701452513670561764L;

    private MachineGroup machineGroup;

    private List<ServerDO> serverList;

    public MachineGroupVO() {
    }

    public MachineGroupVO(MachineGroup machineGroup, List<ServerDO> serverList) {
        this.machineGroup = machineGroup;
        this.serverList = serverList;

    }

    public MachineGroup getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(MachineGroup machineGroup) {
        this.machineGroup = machineGroup;
    }

    public List<ServerDO> getServerList() {
        return serverList;
    }

    public void setServerList(List<ServerDO> serverList) {
        this.serverList = serverList;
    }
}
