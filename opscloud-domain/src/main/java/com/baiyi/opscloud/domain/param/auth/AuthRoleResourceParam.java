package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/5/12 2:42 下午
 * @Version 1.0
 */
public class AuthRoleResourceParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class RoleResourcePageQuery extends PageParam {

        @Schema(description = "资源组ID")
        private Integer groupId;

        @Schema(description = "资源ID")
        private Integer roleId;

        @Schema(description = "是否绑定")
        private Boolean bind;

    }

}
