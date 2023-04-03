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
    @Builder
    @Schema
    public static class UpdateServerAccountPermission {

        @Schema(name = "服务器id", example = "1")
        @NotNull(message = "服务器id不能为空")
        private Integer serverId;

        @Schema(name = "账户id列表")
        private Set<Integer> accountIds;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerAccountPageQuery extends PageParam implements IExtend {

        @Schema(name = "用户名")
        private String username;

        @Schema(name = "账户类型")
        private Integer accountType;

        @Schema(name = "协议")
        private String protocol;

        private Boolean extend;

    }
}
