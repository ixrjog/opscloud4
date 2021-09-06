package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/9/3 5:53 下午
 * @Version 1.0
 */
public class RegisteredInstanceParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class RegisteredInstancePageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "实例名称")
        private String name;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        private Boolean extend;

    }
}
