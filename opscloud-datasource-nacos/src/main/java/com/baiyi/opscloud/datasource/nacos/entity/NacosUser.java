package com.baiyi.opscloud.datasource.nacos.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.nacos.entity.base.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/15 2:17 下午
 * @Version 1.0
 */
public class NacosUser {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UsersResponse extends BasePage.PageResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = -4192497555316727625L;
        private List<User> pageItems;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateUserResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = 4110339556174099015L;
        private Integer code;
        private String message;
        private String data;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthRoleResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = -1258014694158106573L;
        private Integer code;
        private String message;
        private String data;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable {

        @Serial
        private static final long serialVersionUID = 3832085642768517657L;
        private String username;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

}