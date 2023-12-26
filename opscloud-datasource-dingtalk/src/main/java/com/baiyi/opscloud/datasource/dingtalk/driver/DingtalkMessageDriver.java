package com.baiyi.opscloud.datasource.dingtalk.driver;

import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkMessage;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkToken;
import com.baiyi.opscloud.datasource.dingtalk.feign.DingtalkMessageFeign;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkMessageParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/12/1 5:09 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DingtalkMessageDriver {

    private final DingtalkTokenDriver dingtalkTokenDriver;

    private DingtalkMessageFeign buildFeign(DingtalkConfig.Dingtalk config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DingtalkMessageFeign.class, config.getUrl());
    }

    public DingtalkMessage.MessageResponse asyncSend(DingtalkConfig.Dingtalk config, DingtalkMessageParam.AsyncSendMessage asyncSendMessage) {
        DingtalkToken.TokenResponse tokenResponse = dingtalkTokenDriver.getToken(config);
        asyncSendMessage.setAgentId(Long.valueOf(config.getApp().getAgentId()));
        DingtalkMessageFeign dingtalkAPI = buildFeign(config);
        return dingtalkAPI.asyncSend(tokenResponse.getAccessToken(), asyncSendMessage);
    }

}