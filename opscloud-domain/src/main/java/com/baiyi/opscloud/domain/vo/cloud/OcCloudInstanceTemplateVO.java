package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
        private String template;

    }
}
