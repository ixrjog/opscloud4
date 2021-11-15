package com.baiyi.opscloud.datasource.nacos.entry;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.datasource.nacos.entry.base.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

        private static final long serialVersionUID = -4192497555316727625L;
        private List<User> pageItems;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable {

        private static final long serialVersionUID = 3832085642768517657L;
        private String username;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

}
