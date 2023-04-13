package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/17 5:32 下午
 * @Version 1.0
 */
public class CredentialParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class CredentialPageQuery extends PageParam {

        @Schema(name = "查询名称")
        private String queryName;

        @Schema(name = "凭据分类")
        private Integer kind;

    }

}