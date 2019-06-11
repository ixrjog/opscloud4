package com.sdg.cmdb.domain.ansibleTask;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.logCleanup.LogcleanupDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class AnsibleTaskServerDO implements Serializable {
    private static final long serialVersionUID = 3018213675433820265L;

    private long id;

    private long ansibleTaskId;

    private long serverId;
    private long scriptId;

    private String ip;

    private String returncode;

    private String result;

    private boolean success = false;

    private String msg;

    private String gmtCreate;

    private String gmtModify;

    public AnsibleTaskServerDO(ServerVO serverVO, long ansibleTaskId) {
        this.serverId = serverVO.getId();
        this.ip = serverVO.getInsideIP().getIp();
        this.ansibleTaskId = ansibleTaskId;
    }

    public AnsibleTaskServerDO(ServerDO serverDO, long ansibleTaskId) {
        this.serverId = serverDO.getId();
        this.ip = serverDO.getInsideIp();
        this.ansibleTaskId = ansibleTaskId;
    }

    public AnsibleTaskServerDO(LogcleanupDO logcleanupDO, String ip) {
        this.serverId = logcleanupDO.getServerId();
        this.scriptId = logcleanupDO.getScriptId();
        this.ip = ip;
    }

    public AnsibleTaskServerDO() {
    }

    public void setResult(String result) {
        this.result = result;
        if (result == null)
            return;
        if (result.equalsIgnoreCase("SUCCESS"))
            this.success = true;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
