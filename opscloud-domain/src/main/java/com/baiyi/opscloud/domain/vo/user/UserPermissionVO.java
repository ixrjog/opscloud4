package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/8/14 1:58 下午
 * @Version 1.0
 */
public class UserPermissionVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class UserPermission extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = 8880598889712701828L;
        private Integer id;
        private Integer userId;
        private Integer businessId;
        private Integer businessType;
        private Integer rate;
        private String permissionRole;
        private String content;

    }

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

}
