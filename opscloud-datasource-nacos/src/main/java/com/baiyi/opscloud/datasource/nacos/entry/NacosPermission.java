package com.baiyi.opscloud.datasource.nacos.entry;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 3:33 下午
 * @Version 1.0
 */
public class NacosPermission {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PermissionsResponse implements Serializable {

        private static final long serialVersionUID = 5679634631994334473L;
        private Integer pageNumber;
        private Integer totalCount;
        private Integer pagesAvailable;
        private List<Permission> pageItems;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Permission implements Serializable {

        private static final long serialVersionUID = -2841569536441847893L;
        private String action;
        private String resource;
        private String role;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }


}
