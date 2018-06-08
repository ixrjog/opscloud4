package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerVO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class AnsibleTaskServerDO implements Serializable {
    private static final long serialVersionUID = 3018213675433820265L;

    private long id;

    private long ansibleTaskId;

    private long serverId;

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

    public AnsibleTaskServerDO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAnsibleTaskId() {
        return ansibleTaskId;
    }

    public void setAnsibleTaskId(long ansibleTaskId) {
        this.ansibleTaskId = ansibleTaskId;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        if (result == null)
            return;

        if (result.equalsIgnoreCase("SUCCESS"))
            this.success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
