package com.sdg.cmdb.domain.workflow.detail;

import com.sdg.cmdb.domain.auth.RoleDO;
import java.io.Serializable;

public class TodoDetailRole extends TodoDetailAbs implements Serializable {
    private static final long serialVersionUID = 4762775750555084989L;
    /**
     * 申请
     */
    private boolean apply = false;
    /**
     * 是否绑定
     */
    private boolean bind = false;
    private RoleDO roleDO;

    public TodoDetailRole() {
    }

    public TodoDetailRole(RoleDO roleDO, boolean bind) {
        this.roleDO = roleDO;
        this.bind = bind;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public RoleDO getRoleDO() {
        return roleDO;
    }

    public void setRoleDO(RoleDO roleDO) {
        this.roleDO = roleDO;
    }
}
