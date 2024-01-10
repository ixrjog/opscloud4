package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IFilterTag;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2021/6/16 4:32 下午
 * @Version 1.0
 */
public class UserBusinessPermissionParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UserBusinessPermission {

        @NotNull(message = "用户ID不能为空")
        private Integer userId;

        @NotNull(message = "业务类型不能为空")
        private Integer businessType;

        @NotNull(message = "业务id不能为空")
        private Integer businessId;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserBusinessPermissionPageQuery extends SuperPageParam implements IExtend {

        private int businessType;

        @Schema(description = "查询名称")
        private String queryName;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        @Builder.Default
        private Boolean authorized = true;

        @Schema(description = "是否管理员")
        @Builder.Default
        private Boolean admin = false;

        @Builder.Default
        private Boolean extend = false;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class BusinessPermissionUserPageQuery extends PageParam implements IFilterTag, IExtend {

        @Schema(description = "查询用户")
        private String queryName;

        @Schema(description = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        @Builder.Default
        private Boolean authorized = true;

        private Boolean extend;

        @Schema(description = "业务对象类型")
        private int businessType;

        @Schema(description = "业务对象ID")
        private int businessId;

        private final String FILTER_SYSTEM_TAG = TagConstants.SYSTEM.getTag();

        @Schema(description = "过滤系统标签对象")
        private Boolean filterTag;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserPermissionServerGroupPageQuery extends UserBusinessPermissionPageQuery {

        private final int businessType = BusinessTypeEnum.SERVERGROUP.getType();

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserPermissionGroupPageQuery extends UserBusinessPermissionPageQuery {

        private final int businessType = BusinessTypeEnum.USERGROUP.getType();

    }

}