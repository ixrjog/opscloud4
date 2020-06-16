package com.baiyi.opscloud.aliyun.ecs.base;

import com.aliyuncs.IAcsClient;
import com.baiyi.opscloud.aliyun.core.AliyunCore;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/16 11:35 上午
 * @Version 1.0
 */
public class BaseAliyunECS  {

    protected static final int QUERY_PAGE_SIZE = 100;

    @Resource
    protected AliyunCore aliyunCore;

    protected IAcsClient acqAcsClient(String regionId) {
        return aliyunCore.getAcsClient(regionId);
    }

}
