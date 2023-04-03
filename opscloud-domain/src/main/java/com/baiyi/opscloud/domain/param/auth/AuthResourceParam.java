package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AuthResourcePageQuery extends SuperPageParam implements IExtend {

        @Schema(name = "资源组id")
        private Integer groupId;

        @Schema(name = "资源路径")
        private String resourceName;

        @Schema(name = "鉴权")
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

        @Schema(name = "资源组id")
        private Integer groupId;

        @Schema(name = "资源id")
        private Integer roleId;


        @Schema(name = "是否绑定")
        private Boolean bind;

    }
}
