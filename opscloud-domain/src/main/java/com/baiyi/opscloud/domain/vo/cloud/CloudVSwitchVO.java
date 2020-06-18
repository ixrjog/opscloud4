package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/19 3:49 下午
 * @Version 1.0
 */
public class CloudVSwitchVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class VSwitch {
        private Integer id;
        private String regionId;
        private String zoneId;
        private String vpcId;
        private String vswitchName;
        private String vswitchId;
        private String vswitchStatus;
        private String cidrBlock;
        private Integer cloudType;
        private String description;
        private Date creationTime;
        private Integer availableIpAddressCount;
        private Integer isActive;
        private String comment;

    }

}
