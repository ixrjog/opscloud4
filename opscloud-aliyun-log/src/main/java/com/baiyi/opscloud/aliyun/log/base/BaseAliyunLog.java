package com.baiyi.opscloud.aliyun.log.base;

import com.aliyun.openservices.log.Client;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/13 11:49 上午
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseAliyunLog implements InitializingBean {

    static final String ALIYUN_LOG_ENDPOINT = "cn-hangzhou.log.aliyuncs.com";

    protected static Client client;

    protected static final int QUERY_SIZE = 100;

    @Resource
    private AliyunCore aliyunCore;

    private void initClient(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        // 构建一个客户端实例
        BaseAliyunLog.client = new Client(ALIYUN_LOG_ENDPOINT, aliyunAccount.getAccessKeyId(), aliyunAccount.getSecret());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initClient(aliyunCore.getAccount());
    }
}
