package com.baiyi.opscloud.aliyun.ecs.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/3/20 1:09 下午
 * @Version 1.0
 */
public class AliyunInstanceTypeVO implements Serializable {

    @Data
    public static class InstanceType implements Serializable {
        private static final long serialVersionUID = -747040287046719204L;
        private String instanceTypeId;
        private Integer cpuCoreCount;
        private Float memorySize;
        private String instanceTypeFamily;
        private Long localStorageCapacity;
        private Integer localStorageAmount;
        private String localStorageCategory;
        private Integer gPUAmount;
        private String gPUSpec;
        private Integer initialCredit;
        private Integer baselineCredit;
        private Integer eniQuantity;
        private Integer eniPrivateIpAddressQuantity;
        private Integer instanceBandwidthRx;
        private Integer instanceBandwidthTx;
        private Long instancePpsRx;
        private Long instancePpsTx;
        private String instanceFamilyLevel;
    }


}
