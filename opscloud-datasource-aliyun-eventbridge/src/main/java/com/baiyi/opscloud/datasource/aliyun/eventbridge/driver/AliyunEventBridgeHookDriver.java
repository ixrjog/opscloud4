package com.baiyi.opscloud.datasource.aliyun.eventbridge.driver;

import com.baiyi.opscloud.common.datasource.AliyunEventBridgeConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.AliyunEventBridgeResult;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.CloudEvents;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.feign.AliyunEventBridgeHookFeign;
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
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/8/31 09:47
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunEventBridgeHookDriver {

    private AliyunEventBridgeHookFeign buildFeign(String url) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 2))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(AliyunEventBridgeHookFeign.class, url);
    }

    public AliyunEventBridgeResult.Result publish(AliyunEventBridgeConfig.EventBridge config, CloudEvents.Event event) throws MalformedURLException, OCException {
        final String url = Optional.ofNullable(config)
                .map(AliyunEventBridgeConfig.EventBridge::getLeo)
                .map(AliyunEventBridgeConfig.Leo::getUrl)
                .orElseThrow(() -> new OCException("Aliyun eventBridge url configuration does not exist！"));

        final String token = Optional.of(config)
                .map(AliyunEventBridgeConfig.EventBridge::getLeo)
                .map(AliyunEventBridgeConfig.Leo::getToken)
                .orElseThrow(() -> new OCException("Aliyun eventBridge token configuration does not exist"));
        URL urlConfig = URI.create(url).toURL();
        AliyunEventBridgeHookFeign aliyunEventBridgeHookFeign = buildFeign(Joiner.on("://").join(urlConfig.getProtocol(), urlConfig.getHost()));
        return aliyunEventBridgeHookFeign.publish(urlConfig.getPath(), token, event);
    }

}