package com.baiyi.caesar.opscloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/24 10:33 上午
 * @Version 1.0
 */
public class OcServerVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Server {

        private OcServerGroupVO.ServerGroup serverGroup;

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "服务器名称")
        private String name;

        @ApiModelProperty(value = "服务器组id",example="1")
        private Integer serverGroupId;

        @ApiModelProperty(value = "登录类型",example="1")
        private Integer loginType;

        @ApiModelProperty(value = "登录账户")
        private String manageUser;

        @ApiModelProperty(value = "登录账户")
        private String loginUser;

        @ApiModelProperty(value = "环境类型",example="1")
        private Integer envType;

        @ApiModelProperty(value = "公网ip")
        private String publicIp;

        @ApiModelProperty(value = "私网ip")
        private String privateIp;

        @ApiModelProperty(value = "服务器类型",example="1")
        private Integer serverType;

        @ApiModelProperty(value = "地区")
        private String area;

        @ApiModelProperty(value = "序号",example="1")
        private Integer serialNumber;

        @ApiModelProperty(value = "监控状态",example="1")
        private Integer monitorStatus;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        @ApiModelProperty(value = "服务器状态",example="1")
        private Integer serverStatus;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "云主机id,云主机录入专用")
        private Integer cloudServerId;


    }
}
