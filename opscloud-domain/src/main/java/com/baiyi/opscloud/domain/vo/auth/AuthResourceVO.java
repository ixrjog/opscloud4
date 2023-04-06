package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

        @Schema(name = "主键", example = "1")
        private Integer id;

        @Schema(name = "资源组id", example = "1")
        @Valid
        private Integer groupId;

        @Schema(name = "资源路径")
        @NotNull(message = "必须指定资源名称")
        private String resourceName;

        @Schema(name = "资源描述")
        private String comment;

        @Schema(name = "需要鉴权")
        private Boolean needAuth;

        @Schema(name = "用户界面")
        private Boolean ui;
    }

}
