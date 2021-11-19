package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.zabbix.entity.ZabbixMedia;
import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 2:32 下午
 * @Version 1.0
 */
public class ZabbixUser {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryUserResponse extends ZabbixResponse.Response {
        private List<User> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class UpdateUserResponse extends ZabbixResponse.Response {
        private Userids result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CreateUserResponse extends UpdateUserResponse {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class DeleteUserResponse extends UpdateUserResponse {
    }

    @Data
    public static class Userids {
        private List<String> userids;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable {

        private static final long serialVersionUID = -8414101569080017905L;
        // @JsonProperty("userid")
        private String userid;
        private String alias;
        private String name;

        /**
         * 姓
         */
        private String surname;

        /**
         * 允许自动登录。
         * 0 - (default) 禁止自动登录;
         * 1 -允许自动登录。
         */
        //@JsonProperty("autologin")
        private Integer autologin;

        /**
         * 会话过期时间。 接受具有后缀的秒或时间单位。 如果设置为 0s, 用户登录会话永远不会过期。
         */
        //@JsonProperty("autologout")
        private String autologout;

        /**
         * 用户类型。
         * 1 - (default) Zabbix user;
         * 2 - Zabbix admin;
         * 3 - Zabbix super admin.
         */
        private Integer type;
        //@JsonProperty("sessionid")
        private String sessionid;

        /**
         * 用户使用的媒体
         */
        private List<ZabbixMedia> medias;
    }
}
