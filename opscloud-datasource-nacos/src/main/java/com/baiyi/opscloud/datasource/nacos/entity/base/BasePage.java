package com.baiyi.opscloud.datasource.nacos.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/11/15 2:59 下午
 * @Version 1.0
 */
public class BasePage {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = -5501378006388206219L;
        private Integer pageNumber;
        private Integer totalCount;
        private Integer pagesAvailable;

    }

}