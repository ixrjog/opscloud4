package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema
    public static class RegisteredInstancePageQuery extends PageParam implements IExtend {

        @Schema(description = "实例名称")
        private String name;

        @Schema(description = "有效")
        private Boolean isActive;

        private Boolean extend;

    }

}