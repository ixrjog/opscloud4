package com.baiyi.opscloud.aliyun.ram.base;

import com.aliyuncs.IAcsClient;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/16 2:27 下午
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseAliyunRAM {

    @Resource
    private AliyunCore aliyunCore;

    public static final int MAX_ITEMS = 100;

    protected IAcsClient acqAcsClient(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        // 构建一个客户端实例
        return aliyunCore.getAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
    }

}
