package com.baiyi.opscloud.datasource.metersphere.driver;

import com.baiyi.opscloud.common.datasource.MeterSphereConfig;
import com.baiyi.opscloud.datasource.metersphere.entity.LeoHookResult;
import com.baiyi.opscloud.datasource.metersphere.feign.MeterSphereHookV1Feign;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import com.google.common.base.Joiner;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * @Author baiyi
 * @Date 2023/5/15 15:18
 * @Version 1.0
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Slf4j
@Component
@RequiredArgsConstructor
public class MeterSphereDeployHookDriver {

    private MeterSphereHookV1Feign buildFeign(String url) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 2))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                // url: https://xxx.com
                .target(MeterSphereHookV1Feign.class, url);
    }

    public LeoHookResult.Result sendHook(MeterSphereConfig.MeterSphere config, LeoHook.DeployHook hook) throws MalformedURLException {
        URL urlConfig = URI.create(config.getHook().getDeployUrl()).toURL();
        // TODO 不支持port
        final String url = Joiner.on("://").join(urlConfig.getProtocol(), urlConfig.getHost());
        MeterSphereHookV1Feign meterSphereHookAPI = buildFeign(url);
        return meterSphereHookAPI.sendDeployHook(urlConfig.getPath(), hook);
    }

}