package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class AnsibleTaskServerVO extends AnsibleTaskServerDO implements Serializable {
    private static final long serialVersionUID = 8119533679643993785L;

    private ServerDO serverDO;
    private EnvType env;

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
        this.env = new EnvType(serverDO.getEnvType());
    }

    public AnsibleTaskServerVO() {
    }

}
