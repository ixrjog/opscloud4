package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

}