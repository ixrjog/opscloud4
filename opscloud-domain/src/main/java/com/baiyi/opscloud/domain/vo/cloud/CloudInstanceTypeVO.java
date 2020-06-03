package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/3/23 2:01 下午
 * @Version 1.0
 */
public class CloudInstanceTypeVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudInstanceType {

        private Set<String> zones;

        private Integer id;
        private Integer cloudType;
        private String instanceTypeFamily;
        private String instanceTypeId;
        private String regionId;
        private Integer cpuCoreCount;
        private Float memorySize;
        private String instanceFamilyLevel;
        private Date createTime;
        private Date updateTime;
        private String comment;

    }
}
