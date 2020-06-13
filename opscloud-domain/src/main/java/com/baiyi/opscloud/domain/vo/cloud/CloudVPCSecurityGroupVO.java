package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/19 2:43 下午
 * @Version 1.0
 */
public class CloudVPCSecurityGroupVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SecurityGroup {

        private Integer id;
        private String regionId;
        private String vpcId;
        private String securityGroupName;
        private String securityGroupId;
        private String securityGroupType;
        private Integer cloudType;
        private String description;
        private Date creationTime;
        private Integer isActive;
        private String comment;

    }
}
