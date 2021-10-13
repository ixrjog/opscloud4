package com.baiyi.opscloud.tencent.exmail.handler;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import com.baiyi.opscloud.tencent.exmail.http.TencentExmailHttpUtil;
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
 * @Author baiyi
 * @Date 2021/10/12 3:42 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentExmailTokenHandler {

    @Resource
    private TencentExmailHttpUtil tencentExmailHttpUtil;

    private interface TokenApi {
        String GET = "/cgi-bin/gettoken";
    }

    @Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 1000))
    @Cacheable(cacheNames = CachingConfig.Repositories.API_TOKEN, key = "'tencentExmailTokenHandler'", unless = "#result == null")
    public String getToken(DsTencentExmailConfig.Tencent config) {
        DsTencentExmailConfig.Exmail exmail = config.getExmail();
        String url = Joiner.on("").join(exmail.getApiUrl()
                , TokenApi.GET
                , "?corpid="
                , exmail.getCorpId()
                , "&corpsecret="
                , exmail.getCorpSecret());
        try {
            JsonNode data = tencentExmailHttpUtil.httpGetExecutor(url);
            return tencentExmailHttpUtil.checkResponse(data) ? data.get("access_token").asText() : null;
        } catch (IOException e) {
            log.error("获取 TencentExmail token失败！", e);
        }
        return null;
    }


}
