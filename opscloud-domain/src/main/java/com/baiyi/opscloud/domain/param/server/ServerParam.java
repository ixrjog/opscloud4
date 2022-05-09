package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:48 下午
 * @Version 1.0
 */
public class ServerParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class ServerPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

//        @ApiModelProperty(value = "查询ip")
//        private String queryIp;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "状态")
        private Integer serverStatus;

        @ApiModelProperty(value = "监控状态")
        private Integer monitorStatus;

        @ApiModelProperty(value = "标签id")
        private Integer tagId;

        private final Integer businessType = BusinessTypeEnum.SERVER.getType();

        private Boolean extend;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class UserRemoteServerPageQuery extends PageParam implements IExtend {

        private Integer userId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "状态")
        private Integer serverStatus;

        @ApiModelProperty(value = "标签id")
        private Integer tagId;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class UserPermissionServerPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "用户id")
        @NotNull
        private Integer userId;

        @ApiModelProperty(value = "服务器名")
        private String name;

        @ApiModelProperty(value = "查询ip")
        private String queryIp;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private Boolean extend;

    }

}
