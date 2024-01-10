package com.baiyi.opscloud.datasource.aliyun.dms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/16 5:25 PM
 * @Version 1.0
 */
public class DmsTenant {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tenant  {
        public String status;
        public String tenantName;
        public Long tid;
    }

}