package com.baiyi.opscloud.datasource.nacos.entity;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.datasource.nacos.entity.base.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/15 3:11 下午
 * @Version 1.0
 */
public class NacosRole {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RolesResponse extends BasePage.PageResponse implements Serializable {

        private static final long serialVersionUID = -1855804540035445313L;
        private List<Role> pageItems;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Role implements Serializable {

        private static final long serialVersionUID = -83954783773211983L;
        private String username;

        private String role;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }
}
