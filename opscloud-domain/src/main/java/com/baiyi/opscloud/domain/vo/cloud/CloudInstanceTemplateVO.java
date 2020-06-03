package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:59 下午
 * @Version 1.0
 */
public class CloudInstanceTemplateVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudInstanceTemplate {

        @ApiModelProperty(value = "YAML格式配置模版")
        private String templateYAML;

        @ApiModelProperty(value = "选中的虚拟交换机")
        private List<String> vswitchChecked;

        private InstanceTemplate instanceTemplate;

        @ApiModelProperty(value = "实例可用区")
        private List<InstanceZone> instanceZones;

        @ApiModelProperty(value = "选中的安全组")
        private CloudVPCSecurityGroupVO.SecurityGroup securityGroup;

        @ApiModelProperty(value = "选中的云镜像")
        private CloudImageVO.CloudImage cloudImage;

        private Integer id;
        private Integer cloudType;
        private String templateName;
        private String regionId;
        private String imageId;
        private String vpcId;
        private String vpcName;
        private String securityGroupId;
        private String securityGroupName;
        private String ioOptimized;
        private Integer usageCount;
        private Date createTime;
        private Date updateTime;
        private String comment;

    }

    @Data
    public static class InstanceTemplate {
        // 云类型
        private Integer cloudType;
        // 模版名称
        private String templateName;
        // 区
        private String regionId;
        private String comment; // 说明
        private String ioOptimized; // io优化
        private Instance instance; // 实例
        private Disk disk; // 磁盘
        // 可用区
        private List<String> zoneIds;
        private List<VSwitch> vswitchs;
        private String vpcId;
        private String vpcName;
        private String securityGroupId;
        private String securityGroupName;
        private String imageId;
    }

    @Data
    public static class InstanceZone {
        private String zoneId;
        @ApiModelProperty(value = "是否有效，实例可用区&VPC虚拟交换机都存在")
        private boolean isActive = false;
    }

    @Data
    public static class Instance {
        private String typeFamily;
        private String typeId;
        private Integer cpuCoreCount;
        private Float memorySize;
    }

    @Data
    public static class DiskDetail {
        private Integer size;
        private String category;
    }

    @Data
    public static class Disk {
        private DiskDetail sysDisk;
        private DiskDetail dataDisk;
    }

    @Data
    public static class VSwitch {
        private String vswitchName;
        private String vswitchId;
        private String zoneId;
    }
}
