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
 * @Date 2022/9/19 10:12
 * @Version 1.0
 */
public class AuthPlatformParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class AuthPlatformLogPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "平台ID")
        private Integer platformId;

        @ApiModelProperty(value = "认证用户名")
        private String username;

        @ApiModelProperty(value = "是否成功")
        private Boolean result;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

    }

}
