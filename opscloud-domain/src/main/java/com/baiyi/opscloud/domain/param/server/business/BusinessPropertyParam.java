package com.baiyi.opscloud.domain.param.server.business;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/12/18 11:13
 * @Version 1.0
 */
public class BusinessPropertyParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Property implements BaseBusiness.IBusiness {

        private Integer id;
        private Integer businessType;
        private Integer businessId;
        private String comment;
        private String property;

    }

}