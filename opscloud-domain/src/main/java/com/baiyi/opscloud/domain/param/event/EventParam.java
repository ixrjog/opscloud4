package com.baiyi.opscloud.domain.param.event;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2021/10/13 11:28 上午
 * @Version 1.0
 */
public class EventParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class UserPermissionEventPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "查询名称")
        private String name;

        @ApiModelProperty(value = "业务类型")
        private Integer businessType;

        @Builder.Default
        private Boolean isActive = true;

        @Builder.Default
        private Boolean extend = true;

        private String eventType;

    }
}
