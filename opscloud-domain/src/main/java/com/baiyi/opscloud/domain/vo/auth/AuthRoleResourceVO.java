package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2020/2/19 10:50 上午
 * @Version 1.0
 */
public class AuthRoleResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class RoleResource extends BaseVO {

        @Schema(name = "主键",example="1")
        private Integer id;

        @Schema(name = "role主键",example="1")
        private Integer roleId;

        @Schema(name = "资源主键",example="1")
        private Integer resourceId;

    }

}
