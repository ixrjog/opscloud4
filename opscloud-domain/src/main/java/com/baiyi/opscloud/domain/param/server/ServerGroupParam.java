package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/2/21 10:56 上午
 * @Version 1.0
 */
public class ServerGroupParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class ServerGroupPageQuery extends SuperPageParam implements IExtend {

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

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class ServerGroupEnvHostPatternQuery {

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "服务器组名称")
        @NotBlank
        private String serverGroupName;

    }


}
