package com.baiyi.opscloud.example;

import com.baiyi.opscloud.BaseUnit;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/9/14 15:54
 * @Version 1.0
 */
public class ExampleTest extends BaseUnit {

    private static final String url = "https://oc.example.com";

    private static final String accessToken = "XXXXXXXXXXXXXXXX";

    private OpscloudV4API buildOc4API() {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OpscloudV4API.class, ExampleTest.url);
    }

    @Test
    public void helloWorldTest() {
        OpscloudV4API oc4API = buildOc4API();
        Object result = oc4API.helloWorld(accessToken);
        System.out.println(result.toString());
    }

}
