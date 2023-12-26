package com.baiyi.opscloud.domain.param.server;

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
 * @Date 2020/2/21 4:48 下午
 * @Version 1.0
 */
public class ServerParam {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddServer {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "服务器名称")
        @NotBlank(message = "服务器名称不能为空")
        private String name;

        @Schema(description = "显示名称")
        private String displayName;

        @Schema(description = "服务器组id", example = "1")
        @NotNull(message = "服务器组不能为空")
        private Integer serverGroupId;

        @Schema(description = "环境类型", example = "1")
        @NotNull(message = "环境类型不能为空")
        private Integer envType;

        @Schema(description = "公网IP")
        private String publicIp;

        @Schema(description = "私网IP")
        @NotBlank(message = "私网ip不能为空")
        private String privateIp;

        @Schema(description = "服务器类型", example = "1")
        @NotNull(message = "服务器类型不能为空")
        private Integer serverType;

        @Schema(description = "地区")
        private String area;

        @Schema(description = "系统类型")
        private String osType;

        @Schema(description = "序号", example = "1")
        private Integer serialNumber;

        @Schema(description = "监控状态", example = "1")
        private Integer monitorStatus;

        @Schema(description = "资源描述")
        private String comment;

        @Schema(description = "服务器状态", example = "1")
        private Integer serverStatus;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "资产ID")
        private Integer assetId;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UpdateServer {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "服务器名称")
        @NotBlank(message = "服务器名称不能为空")
        private String name;

        @Schema(description = "显示名称")
        private String displayName;

        @Schema(description = "服务器组id", example = "1")
        @NotNull(message = "服务器组不能为空")
        private Integer serverGroupId;

        @Schema(description = "环境类型", example = "1")
        @NotNull(message = "环境类型不能为空")
        private Integer envType;

        @Schema(description = "公网IP")
        private String publicIp;

        @Schema(description = "私网IP")
        @NotBlank(message = "私网ip不能为空")
        private String privateIp;

        @Schema(description = "服务器类型", example = "1")
        @NotNull(message = "服务器类型不能为空")
        private Integer serverType;

        @Schema(description = "地区")
        private String area;

        @Schema(description = "系统类型")
        private String osType;

        @Schema(description = "序号", example = "1")
        private Integer serialNumber;

        @Schema(description = "监控状态", example = "1")
        private Integer monitorStatus;

        @Schema(description = "资源描述")
        private String comment;

        @Schema(description = "服务器状态", example = "1")
        private Integer serverStatus;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "资产ID")
        private Integer assetId;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ServerPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "关键字查询")
        private String queryName;

//        @Schema(description = "查询ip")
//        private String queryIp;

        @Schema(description = "服务器组ID")
        private Integer serverGroupId;

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "状态")
        private Integer serverStatus;

        @Schema(description = "监控状态")
        private Integer monitorStatus;

        @Schema(description = "标签ID")
        private Integer tagId;

        private final Integer businessType = BusinessTypeEnum.SERVER.getType();

        private Boolean extend;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UserRemoteServerPageQuery extends PageParam implements IExtend {

        private Integer userId;

        @Schema(description = "关键字查询")
        private String queryName;

        @Schema(description = "服务器组ID")
        private Integer serverGroupId;

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "状态")
        private Integer serverStatus;

        @Schema(description = "标签ID")
        private Integer tagId;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UserPermissionServerPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "用户ID")
        @NotNull
        private Integer userId;

        @Schema(description = "服务器名")
        private String name;

        @Schema(description = "查询IP")
        private String queryIp;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private Boolean extend;

    }

}