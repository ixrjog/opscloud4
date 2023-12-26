package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:21 上午
 * @Version 1.0
 */
public class ServerAccountParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class ServerAccount {

        private Integer id;

        private String username;

        private String displayName;

        @NotNull(message = "凭据不能为空")
        private Integer credentialId;

        @NotNull(message = "账户类型不能为空")
        private Integer accountType;

        @NotNull(message = "协议不能为空")
        private String protocol;

        private Boolean isActive;

        private String comment;

    }

    @Data
    @Builder
    @Schema
    public static class UpdateServerAccountPermission {

        @Schema(description = "服务器ID", example = "1")
        @NotNull(message = "服务器id不能为空")
        private Integer serverId;

        @Schema(description = "账户ID列表")
        private Set<Integer> accountIds;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerAccountPageQuery extends PageParam implements IExtend {

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "账户类型")
        private Integer accountType;

        @Schema(description = "协议")
        private String protocol;

        private Boolean extend;

    }

}