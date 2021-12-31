package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
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
    @ApiModel
    public static class AuthResourcePageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "资源组id")
        private Integer groupId;

        @ApiModelProperty(value = "资源路径")
        private String resourceName;

        @ApiModelProperty(value = "鉴权")
        private Boolean needAuth;

        @Override
        public Boolean getExtend() {
            return true;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class RoleBindResourcePageQuery extends PageParam {

        @ApiModelProperty(value = "资源组id")
        private Integer groupId;

        @ApiModelProperty(value = "资源id")
        private Integer roleId;


        @ApiModelProperty(value = "是否绑定")
        private Boolean bind;

    }
}
