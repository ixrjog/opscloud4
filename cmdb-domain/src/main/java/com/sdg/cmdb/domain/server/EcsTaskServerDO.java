package com.sdg.cmdb.domain.server;

import lombok.Data;

import java.io.Serializable;

@Data
public class EcsTaskServerDO implements Serializable {
    private static final long serialVersionUID = -1801800002540266454L;

    private long taskId;
    private String instanceId;
    private String gmtModify;
    private String gmtCreate;

    public EcsTaskServerDO() {
    }

    public EcsTaskServerDO(long taskId, String instanceId) {
        this.taskId = taskId;
        this.instanceId = instanceId;
    }

}
