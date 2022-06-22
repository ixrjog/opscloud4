package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IFilterTag;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/6/16 4:32 下午
 * @Version 1.0
 */
public class UserBusinessPermissionParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class UserBusinessPermissionPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @ApiModelProperty(value = "用户id")
//        @NotNull(message = "用户id不能为空")
        private Integer userId;

        @ApiModelProperty(value = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        @Builder.Default
        private Boolean authorized = true;

        @ApiModelProperty(value = "是否管理员")
        @Builder.Default
        private Boolean admin = false;

        @Builder.Default
        private Boolean extend = false;

        private int businessType;
    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class BusinessPermissionUserPageQuery extends PageParam implements IFilterTag, IExtend {

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

        private final String FILTER_SYSTEM_TAG = TagConstants.SYSTEM.getTag();

        @ApiModelProperty(value = "过滤系统标签对象")
        private Boolean filterTag;

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
