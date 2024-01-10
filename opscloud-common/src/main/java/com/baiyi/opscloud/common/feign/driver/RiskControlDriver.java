package com.baiyi.opscloud.common.feign.driver;

import com.baiyi.opscloud.common.feign.request.RiskControlRequest;
import com.baiyi.opscloud.common.feign.response.MgwCoreResponse;
import com.baiyi.opscloud.common.feign.ser.RiskControlFeign;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @Author 修远
 * @Date 2023/7/5 2:50 PM
 * @Since 1.0
 */
public class RiskControlDriver {

    private static RiskControlFeign riskControlFeignBuild(String url) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(RiskControlFeign.class, url);
    }

    public static MgwCoreResponse<?> serReload(String url, String applicationName, RiskControlRequest.SerLoader request) {
        RiskControlFeign feign = riskControlFeignBuild(url);
        return feign.riskControlSerReload(applicationName, request);
    }

}