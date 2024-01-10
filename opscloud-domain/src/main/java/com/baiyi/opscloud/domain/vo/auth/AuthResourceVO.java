package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/14 9:52 下午
 * @Version 1.0
 */
public class AuthResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Resource extends BaseVO implements AuthGroupVO.IAuthGroup {

        // 资源组
        private AuthGroupVO.Group group;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "资源组ID", example = "1")
        private Integer groupId;

        @Schema(description = "资源路径")
        private String resourceName;

        @Schema(description = "资源描述")
        private String comment;

        @Schema(description = "需要鉴权")
        private Boolean needAuth;

        @Schema(description = "用户界面")
        private Boolean ui;
    }

}