package com.baiyi.opscloud.tencent.exmail.handler;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.tencent.exmail.config.TencentExmailConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 10:55 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class TencentExmailTokenHandler extends TencentExmailHandler {

    @Resource
    private TencentExmailConfig tencentExmailConfig;

    private interface TokenUrls {
        String getToken = "/cgi-bin/gettoken";
    }

    @Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 1000))
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_API_TOKEN, key = "'tencentExmailTokenHandler'")
    public String getToken() {
        TencentExmailConfig.Account account = getMasterAccount();
        String url = Joiner.on("").join(tencentExmailConfig.getApiUrl()
                , TokenUrls.getToken
                , "?corpid="
                , account.getCorpId()
                , "&corpsecret="
                , account.getCorpSecret());
        try {
            JsonNode data = httpGetExecutor(url);
            return checkResponse(data) ? data.get("access_token").asText() : null;
        } catch (IOException e) {
            log.error("获取TencentExmail token失败", e);
            return null;
        }
    }


}
