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
public class OcCloudInstanceTemplateVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudInstanceTemplate {

        @ApiModelProperty(value = "YAML格式配置模版")
        private String templateYAML;

        private InstanceTemplate instanceTemplate;

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
    public  static class InstanceTemplate {

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
        private String vpcId;
        private String vpcName;
        private String securityGroupId;
        private String securityGroupName;
        private String imageId;

    }

    @Data
    public  static class Instance {
        private String typeFamily;
        private String typeId;
        private Integer cpuCoreCount;
        private Float memorySize;
    }

    @Data
    public  static class DiskDetail {
        private Integer size;
        private String category;
    }

    @Data
    public  static class Disk {
        private DiskDetail sysDisk;
        private DiskDetail dataDisk;
    }
}
