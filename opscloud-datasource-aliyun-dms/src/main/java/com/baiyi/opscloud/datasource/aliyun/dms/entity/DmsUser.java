package com.baiyi.opscloud.datasource.aliyun.dms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/16 3:10 PM
 * @Version 1.0
 */
public class DmsUser {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        public Long curExecuteCount;
        public Long curResultCount;
        public String dingRobot;
        public String email;
        public String lastLoginTime;
        public Long maxExecuteCount;
        public Long maxResultCount;
        public String mobile;
        public String nickName;
        public String notificationMode;
        public String parentUid;
        public String signatureMethod;
        public String state;
        public String uid;
        public String userId;
        public String webhook;
    }

}
