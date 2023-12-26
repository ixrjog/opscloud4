package com.baiyi.opscloud.domain.param.event;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema
    public static class UserPermissionEventPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "查询名称")
        private String name;

        @Schema(description = "业务类型")
        private Integer businessType;

        @Builder.Default
        private Boolean isActive = true;

        @Builder.Default
        private Boolean extend = true;

        private String eventType;

    }

}