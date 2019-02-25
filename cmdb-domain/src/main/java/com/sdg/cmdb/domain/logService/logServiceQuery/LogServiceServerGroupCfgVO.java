package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class LogServiceServerGroupCfgVO extends LogServiceServerGroupCfgDO implements Serializable {
    private static final long serialVersionUID = 573733901286113356L;

    private boolean authed = false;

    private ServerGroupDO serverGroupDO;

    private ServerGroupUseTypeDO serverGroupUseTypeDO;

    public LogServiceServerGroupCfgVO(){

    }
    public LogServiceServerGroupCfgVO(ServerGroupDO serverGroupDO,ServerGroupUseTypeDO useType) {
        this.serverGroupDO = serverGroupDO;
        this.serverGroupUseTypeDO =useType;
    }

    public LogServiceServerGroupCfgVO(ServerGroupDO serverGroupDO, LogServiceServerGroupCfgDO logServiceServerGroupCfgDO, boolean authed,ServerGroupUseTypeDO useType) {
        this.authed = authed;
        this.serverGroupDO = serverGroupDO;
        setId(logServiceServerGroupCfgDO.getId());
        setServerGroupId(logServiceServerGroupCfgDO.getServerGroupId());
        setServerGroupName(logServiceServerGroupCfgDO.getServerGroupName());
        setProject(logServiceServerGroupCfgDO.getProject());
        setLogstore(logServiceServerGroupCfgDO.getLogstore());
        setTopic(logServiceServerGroupCfgDO.getTopic());
        setGmtCreate(logServiceServerGroupCfgDO.getGmtCreate());
        setGmtModify(logServiceServerGroupCfgDO.getGmtModify());
        this.serverGroupUseTypeDO =useType;
    }

}
