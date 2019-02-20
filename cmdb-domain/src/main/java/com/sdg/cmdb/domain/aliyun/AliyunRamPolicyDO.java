package com.sdg.cmdb.domain.aliyun;


import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import lombok.Data;

import java.io.Serializable;

@Data
public class AliyunRamPolicyDO implements Serializable {
    private static final long serialVersionUID = 804111741722429204L;

    public AliyunRamPolicyDO(){

    }

    public AliyunRamPolicyDO(ListPoliciesResponse.Policy policy){
        this.policyName = policy.getPolicyName();
        this.policyType = policy.getPolicyType();
        this.description = policy.getDescription();
        this.defaultVersion = policy.getDefaultVersion();
    }

    private long id;
    private String policyName;
    private String policyType;
    private String description;
    private String defaultVersion;
    private boolean allows;
    private String gmtCreate;
    private String gmtModify;

}
