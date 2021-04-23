package com.baiyi.opscloud.dingtalk.impl;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.dingtalk.DingtalkTokenApi;
import com.baiyi.opscloud.dingtalk.handler.DingtalkTokenApiHandler;
import com.dingtalk.api.response.OapiGettokenResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远/a>
 * @Date 2020/12/14 10:53 上午
 * @Since 1.0
 */

@Component
public class DingtalkTokenApiImpl implements DingtalkTokenApi {

    @Resource
    private DingtalkTokenApiHandler dingtalkTokenApiHandler;

    @Override
    @Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 1000))
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DINGTALK_API_TOKEN, key = "'dingtalkApiToken'")
    public String getDingtalkToken(String uid) throws RuntimeException {
        OapiGettokenResponse response = dingtalkTokenApiHandler.getToken(uid);
        if (response == null)
            throw new RuntimeException("获取dingtalk token 失败,retry");
        return response.getAccessToken();
    }

    @Override
    public String reGetDingtalkToken(String uid) {
        evictPreview();
        return getDingtalkToken(uid);
    }

    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_DINGTALK_API_TOKEN, key = "'dingtalkApiToken'", beforeInvocation = true)
    public void evictPreview() {
    }
}
