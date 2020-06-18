package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/23 12:56 下午
 * @Version 1.0
 */
@Data
@Builder
public class CloudInstanceTypeBO {

    private Integer id;
    private Integer cloudType;
    private String instanceTypeFamily;
    private String instanceTypeId;
    @Builder.Default
    private String regionId = "";
    private Integer cpuCoreCount;
    private Float memorySize;
    private String instanceFamilyLevel;
    private Date createTime;
    private Date updateTime;
    private String comment;

}
