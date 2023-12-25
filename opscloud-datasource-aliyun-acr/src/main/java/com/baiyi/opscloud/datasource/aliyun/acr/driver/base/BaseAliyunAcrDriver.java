package com.baiyi.opscloud.datasource.aliyun.acr.driver.base;

import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/8/15 16:26
 * @Version 1.0
 */
public abstract class BaseAliyunAcrDriver {

    @Resource
    protected AliyunClient aliyunClient;

    public interface Query {
        int PAGE_SIZE = 30;
        String NORMAL = "NORMAL";
    }

}