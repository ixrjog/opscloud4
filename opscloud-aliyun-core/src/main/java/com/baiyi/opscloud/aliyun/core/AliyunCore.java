package com.baiyi.opscloud.aliyun.core;

import com.aliyuncs.IAcsClient;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:52 下午
 * @Version 1.0
 */
public interface AliyunCore {

    List<String> getRegionIds();

    IAcsClient getAcsClient(String regionId);
}
