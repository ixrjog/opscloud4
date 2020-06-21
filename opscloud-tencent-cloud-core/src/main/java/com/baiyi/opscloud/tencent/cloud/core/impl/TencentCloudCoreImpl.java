package com.baiyi.opscloud.tencent.cloud.core.impl;

import com.baiyi.opscloud.tencent.cloud.core.TencentCloudCore;
import com.baiyi.opscloud.tencent.cloud.core.config.TencentCloudCoreConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/20 4:55 下午
 * @Version 1.0
 */
@Component
public class TencentCloudCoreImpl implements TencentCloudCore {

    @Resource
    private TencentCloudCoreConfig tencentCloudCoreConfig;


}
