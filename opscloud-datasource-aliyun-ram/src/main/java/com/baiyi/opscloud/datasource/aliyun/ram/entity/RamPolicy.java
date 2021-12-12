package com.baiyi.opscloud.datasource.aliyun.ram.entity;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/12/10 1:54 PM
 * @Version 1.0
 */
public class RamPolicy {

    // ListPolicies
    @Data
    public static class Policy  {
        private String policyName;
        private String policyType;
        private String description;
        private String defaultVersion;
        private String createDate;
        private String updateDate;
        private Integer attachmentCount;

        private String attachDate; // ListPoliciesForUser
    }

}
