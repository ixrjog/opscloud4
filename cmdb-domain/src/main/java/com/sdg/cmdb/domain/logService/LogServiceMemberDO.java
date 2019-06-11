package com.sdg.cmdb.domain.logService;

import com.sdg.cmdb.domain.server.ServerGroupDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogServiceMemberDO implements Serializable {
    private static final long serialVersionUID = 1221875451124310900L;

    private long id;
    private long groupCfgId;
    private long serverGroupId;
    private String serverGroupName;
    private String gmtCreate;
    private String gmtModify;

    public LogServiceMemberDO() {
    }

    public LogServiceMemberDO(long groupCfgId, ServerGroupDO serverGroupDO) {
        this.groupCfgId = groupCfgId;
        this.serverGroupId = serverGroupDO.getId();
        this.serverGroupName = serverGroupDO.getName();
    }

}
