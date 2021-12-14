package com.baiyi.opscloud.datasource.aliyun.ram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/10 1:37 PM
 * @Version 1.0
 */
public class RamUser {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class User {
        private String userId;
        private String userName;
        private String displayName;
        private String mobilePhone;
        private String email;
        private String comments;
        private String createDate;
        private String updateDate; // ListUsersResponse

        private String attachDate; // ListEntitiesForPolicy
    }

}
