package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/24 10:38 上午
 * @Version 1.0
 */
@Data
@Builder
public class CloudInstanceTemplateBO {

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
    private String templateYaml;
}
