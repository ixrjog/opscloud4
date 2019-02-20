package com.sdg.cmdb.domain.workflow.detail;

import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.sdg.cmdb.domain.aliyun.AliyunRamPolicyDO;


import java.io.Serializable;

public class TodoDetailRAMPolicy extends TodoDetailAbs implements Serializable {
    private static final long serialVersionUID = 8390880502108033267L;

    public static final int DETAIL_TYPE_AUTHED = 0;
    public static final int DETAIL_TYPE_APPLY = 1;

    public TodoDetailRAMPolicy() {
    }

    public TodoDetailRAMPolicy(ListPoliciesForUserResponse.Policy policy) {
        this.policyName = policy.getPolicyName();
        this.policyType = policy.getPolicyType();
        this.detailType = 0;
    }


    public TodoDetailRAMPolicy(AliyunRamPolicyDO policy) {
        this.policyName = policy.getPolicyName();
        this.policyType = policy.getPolicyType();
        setContent(policy.getDescription());
        this.detailType = 1;
    }

    private String policyName;

    private String policyType;

    /**
     * 0 用户当前的RAM策略
     * 1 用户申请的RAM策略
     */
    private int detailType;

    /**
     * 附加授权
     * AttachPolicyToUser
     */
    private boolean attachPolicy = true;

    /**
     * 撤销授权
     * DetachPolicyFromUser
     */
    private boolean detachPolicy = false;

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }

    public boolean isAttachPolicy() {
        return attachPolicy;
    }

    public void setAttachPolicy(boolean attachPolicy) {
        this.attachPolicy = attachPolicy;
    }

    public boolean isDetachPolicy() {
        return detachPolicy;
    }

    public void setDetachPolicy(boolean detachPolicy) {
        this.detachPolicy = detachPolicy;
    }
}

