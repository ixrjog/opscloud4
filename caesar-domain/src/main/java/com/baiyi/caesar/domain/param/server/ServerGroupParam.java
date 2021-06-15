package com.baiyi.caesar.domain.param.server;

import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.PageParam;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/2/21 10:56 上午
 * @Version 1.0
 */
public class ServerGroupParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class ServerGroupPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "组名")
        private String name;

        @ApiModelProperty(value = "组类型")
        private Integer serverGroupTypeId;

        private Boolean extend;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserPermissionServerGroupPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "组名")
        private String queryName;

        @ApiModelProperty(value = "用户id")
        @NotNull(message = "用户id不能为空")
        private Integer userId;

        @ApiModelProperty(value = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        private Boolean authorized;

        private Boolean extend;

        private final int businessType = BusinessTypeEnum.SERVERGROUP.getType();

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserServerTreeQuery {

        private Integer userId;

        @ApiModelProperty(value = "查询名称")
        private String name;

        @ApiModelProperty(value = "服务器组类型", example = "1")
        private Integer serverGroupTypeId;

        private Boolean isAdmin;

        private final int businessType = BusinessTypeEnum.SERVERGROUP.getType();
    }

}
