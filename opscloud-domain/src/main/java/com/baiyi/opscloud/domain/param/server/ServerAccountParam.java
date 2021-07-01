package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:21 上午
 * @Version 1.0
 */
public class ServerAccountParam {

    @Data
    @Builder
    @ApiModel
    public static class UpdateServerAccountPermission {

        @ApiModelProperty(value = "服务器id", example = "1")
        @NotNull(message = "服务器id不能为空")
        private Integer serverId;

        @ApiModelProperty(value = "账户id列表")
        private Set<Integer> accountIds;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class ServerAccountPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "账户类型")
        private Integer accountType;

        @ApiModelProperty(value = "协议")
        private String protocol;

        private Boolean extend;
    }
}
