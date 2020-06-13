package com.baiyi.opscloud.zabbix.config;

/**
 * @Author baiyi
 * @Date 2020/1/3 2:28 下午
 * @Version 1.0
 */

import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2020/1/3 11:22 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "zabbix", ignoreInvalidFields = true)
public class ZabbixConfig {

    private String version = "4.0";
    private String url;
    private String user;
    private String password;
    private String zone;

    public ZabbixRequest buildLoginRequest() {
        return ZabbixRequestBuilder.newBuilder()
                .paramEntry("user", user)
                .paramEntry("password", password)
                .method("user.login").build();
    }

    public URI buildURI() throws URISyntaxException {
        return new URI(url.trim());
    }

}