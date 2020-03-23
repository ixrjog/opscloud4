package com.baiyi.opscloud.aliyun.ecs.instanceTemplate;

import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 2:58 下午
 * @Version 1.0
 */
@Data
public class InstanceTemplate {

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
