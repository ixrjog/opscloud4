package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2020/2/15 5:20 下午
 * @Version 1.0
 */
public class AuthGroupParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class AuthGroupPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "资源组名称")
        private String groupName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

    }

}
