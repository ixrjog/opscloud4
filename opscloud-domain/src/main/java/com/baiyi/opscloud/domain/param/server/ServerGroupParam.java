package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2020/2/21 10:56 上午
 * @Version 1.0
 */
public class ServerGroupParam {

    @Data
    @Schema
    public static class AddServerGroup {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "组名称")
        @NotBlank(message = "组名称不能为空")
        private String name;

        @Schema(description = "组类型", example = "1")
        @NotNull(message = "组类型不能为空")
        private Integer serverGroupTypeId;

        @Schema(description = "是否支持工单")
        @NotNull(message = "是否支持工单不能为空")
        private Boolean allowOrder;

        @Schema(description = "资源描述")
        private String comment;

    }

    @Data
    @Schema
    public static class UpdateServerGroup {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "组名称")
        @NotBlank(message = "组名称不能为空")
        private String name;

        @Schema(description = "组类型", example = "1")
        @NotNull(message = "组类型不能为空")
        private Integer serverGroupTypeId;

        @Schema(description = "是否支持工单")
        @NotNull(message = "是否支持工单不能为空")
        private Boolean allowOrder;

        @Schema(description = "资源描述")
        private String comment;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ServerGroupPageQuery extends SuperPageParam implements IExtend, IAllowOrder {

        @Schema(description = "组名")
        private String name;

        @Schema(description = "组类型")
        private Integer serverGroupTypeId;

        private Boolean extend;

        private Boolean allowOrder;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserPermissionServerGroupPageQuery extends PageParam implements IExtend {

        @Schema(description = "组名")
        private String queryName;

        @Schema(description = "用户ID")
        @NotNull(message = "用户ID不能为空")
        private Integer userId;

        @Schema(description = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        private Boolean authorized;

        private Boolean extend;

        private final int businessType = BusinessTypeEnum.SERVERGROUP.getType();

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UserServerTreeQuery {

        private Integer userId;

        @Schema(description = "查询名称")
        private String name;

        @Schema(description = "服务器组类型", example = "1")
        private Integer serverGroupTypeId;

        private Boolean isAdmin;

        private final int businessType = BusinessTypeEnum.SERVERGROUP.getType();

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ServerGroupEnvHostPatternQuery {

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "服务器组名称")
        @NotBlank
        private String serverGroupName;

    }

}