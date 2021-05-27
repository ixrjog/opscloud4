package com.baiyi.caesar.domain.param.sys;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class CredentialPageQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "凭据分类")
        private Integer kind;

    }
}
