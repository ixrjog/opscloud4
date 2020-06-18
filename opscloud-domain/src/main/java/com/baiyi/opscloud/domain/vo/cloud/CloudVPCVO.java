package com.baiyi.opscloud.domain.vo.cloud;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:12 下午
 * @Version 1.0
 */
public class CloudVPCVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudVpc {

        private List<CloudVPCSecurityGroupVO.SecurityGroup> securityGroups;
        private Map<String, List<CloudVSwitchVO.VSwitch>> vswitchMap;

        @ApiModelProperty(value = "主键")
        private Integer id;
        private String uid;
        private String accountName;
        private String regionId;
        private String vpcId;
        private String vpcName;
        private String cidrBlock;
        private Integer cloudType;
        private String description;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date creationTime;
        private Integer isActive;
        private Integer isDeleted;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
        private String comment;

    }
}
