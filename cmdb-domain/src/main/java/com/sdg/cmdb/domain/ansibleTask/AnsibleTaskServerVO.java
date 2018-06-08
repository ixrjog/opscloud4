package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class AnsibleTaskServerVO extends AnsibleTaskServerDO implements Serializable {
    private static final long serialVersionUID = 8119533679643993785L;

    private ServerDO serverDO;

    public AnsibleTaskServerVO(AnsibleTaskServerDO ansibleTaskServerDO, ServerDO serverDO) {

        setId(ansibleTaskServerDO.getId());
        setAnsibleTaskId(ansibleTaskServerDO.getAnsibleTaskId());
        setServerId(ansibleTaskServerDO.getServerId());
        setIp(ansibleTaskServerDO.getIp());
        setReturncode(ansibleTaskServerDO.getReturncode());
        setResult(ansibleTaskServerDO.getResult());
        setSuccess(ansibleTaskServerDO.isSuccess());
        setMsg(ansibleTaskServerDO.getMsg());
        setGmtCreate(ansibleTaskServerDO.getGmtCreate());
        setGmtModify(ansibleTaskServerDO.getGmtModify());

        this.serverDO = serverDO;
    }

    public AnsibleTaskServerVO() {

    }

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }
}
