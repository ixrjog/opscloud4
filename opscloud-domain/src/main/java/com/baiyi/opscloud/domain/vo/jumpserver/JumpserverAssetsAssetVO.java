package com.baiyi.opscloud.domain.vo.jumpserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/12 2:47 下午
 * @Version 1.0
 */
public class JumpserverAssetsAssetVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetsAsset{

        @ApiModelProperty(value = "主键",example="格式为uuid")
        private String id;

        @ApiModelProperty(value = "ip地址")
        private String ip;

        @ApiModelProperty(value = "主机名")
        private String hostname;

        @ApiModelProperty(value = "端口",example = "22")
        private Integer port;

        @ApiModelProperty(value = "是否有效")
        private Boolean isActive;

        @ApiModelProperty(value = "公网ip地址")
        private String publicIp;

        private String number;

        private String vendor;

        private String model;

        private String sn;

        @ApiModelProperty(value = "cpu")
        private String cpuModel;

        private Integer cpuCount;

        private Integer cpuCores;

        private String memory;

        private String diskTotal;

        private String diskInfo;

        private String os;

        private String osVersion;

        private String osArch;

        private String hostnameRaw;

        private String createdBy;

        private Date dateCreated;

        private String adminUserId;

        private String domainId;

        private String protocol;

        private String orgId;

        private Integer cpuVcpus;

        private String protocols;

        private Integer platformId;

        private String comment;


    }
}
