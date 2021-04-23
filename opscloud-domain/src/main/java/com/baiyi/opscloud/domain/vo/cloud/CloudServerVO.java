package com.baiyi.opscloud.domain.vo.cloud;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:43 上午
 * @Version 1.0
 */
public class CloudServerVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudServer {

        @ApiModelProperty(value = "前端用，同步中")
        private Boolean syncing = false;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "服务器名称")
        private String serverName;

        @ApiModelProperty(value = "实例id")
        private String instanceId;

        @ApiModelProperty(value = "实例")
        private String instanceName;

        @ApiModelProperty(value = "区域")
        private String zone;

        @ApiModelProperty(value = "云主机类型")
        private Integer cloudServerType;

        @ApiModelProperty(value = "私有ip")
        private String privateIp;

        @ApiModelProperty(value = "公网ip")
        private String publicIp;

        @ApiModelProperty(value = "cpu")
        private Integer cpu;

        @ApiModelProperty(value = "内存")
        private Integer memory;

        @ApiModelProperty(value = "vpc网络id")
        private String vpcId;

        @ApiModelProperty(value = "实例类型")
        private String instanceType;

        @ApiModelProperty(value = "镜像id")
        private String imageId;

        @ApiModelProperty(value = "系统盘容量")
        private Integer systemDiskSize;

        @ApiModelProperty(value = "数据盘容量")
        private Integer dataDiskSize;

        @ApiModelProperty(value = "云主机创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdTime;

        @ApiModelProperty(value = "云主机过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        @ApiModelProperty(value = "付费类型")
        private String chargeType;

        @ApiModelProperty(value = "服务器状态")
        private Integer serverStatus;

        @ApiModelProperty(value = "续费设置")
        private String renewalStatus;

        @ApiModelProperty(value = "绑定服务器id")
        private Integer serverId;

        @ApiModelProperty(value = "允许电源管理")
        private Boolean powerMgmt;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

        @ApiModelProperty(value = "倒计时天数")
        private Integer expiresDays;
    }


}
