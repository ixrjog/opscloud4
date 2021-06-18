package com.baiyi.caesar.domain.param.user;

import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.PageParam;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/6/16 4:32 下午
 * @Version 1.0
 */
public class UserBusinessPermissionParam {


    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserBusinessPermissionPageQuery extends PageParam implements IExtend {
        @ApiModelProperty(value = "组名")
        private String queryName;

        @ApiModelProperty(value = "用户id")
        @NotNull(message = "用户id不能为空")
        private Integer userId;

        @ApiModelProperty(value = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        private Boolean authorized;

        private Boolean extend;

        private int businessType;
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
