package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/6/16 4:32 下午
 * @Version 1.0
 */
public class UserBusinessPermissionParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class UserBusinessPermissionPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "用户id")
        @NotNull(message = "用户id不能为空")
        private Integer userId;

        @ApiModelProperty(value = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        @Builder.Default
        private Boolean authorized = true;

        private Boolean extend;

        private int businessType;
    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class BusinessPermissionUserPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "查询用户")
        private String queryName;

        @ApiModelProperty(value = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        @Builder.Default
        private Boolean authorized = true;

        private Boolean extend;

        @ApiModelProperty(value = "业务对象类型")
        @NotNull(message = "业务对象类型不能为空")
        private int businessType;

        @ApiModelProperty(value = "业务对象ID")
        @NotNull(message = "业务对象ID不能为空")
        private int businessId;

    }


    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserPermissionServerGroupPageQuery extends UserBusinessPermissionPageQuery {

        private final int businessType = BusinessTypeEnum.SERVERGROUP.getType();

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserPermissionGroupPageQuery extends UserBusinessPermissionPageQuery {

        private final int businessType = BusinessTypeEnum.USERGROUP.getType();

    }
}
