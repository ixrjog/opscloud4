package com.baiyi.opscloud.datasource.aliyun.util;

import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.google.common.collect.Sets;

import java.util.Optional;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/12/14 4:22 PM
 * @Version 1.0
 */
public class AliyunRegionIdUtil {

    private AliyunRegionIdUtil(){}

    public static Set<String> toOnsRegionIds(AliyunConfig.Aliyun aliyun) {
        Set<String> regionIds = Sets.newHashSet(aliyun.getRegionIds());
        regionIds.add(aliyun.getRegionId());
        regionIds.add(Optional.of(aliyun)
                .map(AliyunConfig.Aliyun::getOns)
                .map(AliyunConfig.Ons::getInternetRegionId)
                .orElse(null));
        return regionIds;
    }

}