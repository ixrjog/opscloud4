package com.baiyi.opscloud.datasource.dingtalk.driver;

import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkToken;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkUser;
import com.baiyi.opscloud.datasource.dingtalk.feign.DingtalkUserFeign;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkUserParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/29 4:41 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DingtalkUserDriver {

    private final DingtalkTokenDriver dingtalkTokenDriver;

    private DingtalkUserFeign buildFeign(DingtalkConfig.Dingtalk config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DingtalkUserFeign.class, config.getUrl());
    }

    public DingtalkUser.UserResponse list(DingtalkConfig.Dingtalk config, DingtalkUserParam.QueryUserPage queryUserPage) {
        DingtalkToken.TokenResponse tokenResponse = dingtalkTokenDriver.getToken(config);
        DingtalkUserFeign dingtalkAPI = buildFeign(config);
        return dingtalkAPI.list(tokenResponse.getAccessToken(), queryUserPage);
    }

}
