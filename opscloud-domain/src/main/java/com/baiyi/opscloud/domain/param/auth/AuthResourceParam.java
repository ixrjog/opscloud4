package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2020/2/14 9:56 下午
 * @Version 1.0
 */
public class AuthResourceParam {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Resource  {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "资源组id", example = "1")
        @Valid
        private Integer groupId;

        @Schema(description = "资源路径")
        @NotNull(message = "必须指定资源名称")
        private String resourceName;

        @Schema(description = "资源描述")
        private String comment;

        @Schema(description = "需要鉴权")
        private Boolean needAuth;

        @Schema(description = "用户界面")
        private Boolean ui;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AuthResourcePageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "资源组ID")
        private Integer groupId;

        @Schema(description = "资源路径")
        private String resourceName;

        @Schema(description = "鉴权")
        private Boolean needAuth;

        @Override
        public Boolean getExtend() {
            return true;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class RoleBindResourcePageQuery extends PageParam {

        @Schema(description = "资源组ID")
        private Integer groupId;

        @Schema(description = "资源ID")
        private Integer roleId;

        @Schema(description = "是否绑定")
        private Boolean bind;

    }

}