package com.baiyi.opscloud.tencent.exmail.handler;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.datasource.TencentExmailDsInstanceConfig;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailToken;
import com.baiyi.opscloud.tencent.exmail.feign.TencentExmailTokenFeign;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:42 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentExmailTokenHandler {

    @Cacheable(cacheNames = CachingConfig.Repositories.API_TOKEN, key = "#config.exmail.corpId + '_v4_tencent_exmail_token'", unless = "#result == null")
    public ExmailToken getToken(TencentExmailDsInstanceConfig.Tencent config) {
        TencentExmailDsInstanceConfig.Exmail exmail = config.getExmail();
        TencentExmailTokenFeign exmailAPI = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(TencentExmailTokenFeign.class, exmail.getApiUrl());
        return exmailAPI.getToken(config.getExmail().getCorpId(), config.getExmail().getCorpSecret());
    }

}
