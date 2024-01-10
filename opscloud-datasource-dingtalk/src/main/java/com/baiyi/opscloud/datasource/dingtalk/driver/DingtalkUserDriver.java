package com.baiyi.opscloud.datasource.dingtalk.driver;

import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkToken;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkUser;
import com.baiyi.opscloud.datasource.dingtalk.feign.DingtalkUserFeign;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkUserParam;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import feign.Feign;
import feign.Response;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

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

    public File getUserAvatar(String userAvatarUrl) throws IOException {
        URL url = URI.create(userAvatarUrl).toURL();
        DingtalkUserFeign dingtalkUserFeign = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DingtalkUserFeign.class, Joiner.on("://").join(url.getProtocol(), url.getHost()));

        Map<String, String> queryMap = Maps.newHashMap();
        queryMap.put("path", url.getPath());
        Response res = dingtalkUserFeign.getAvatar(queryMap);
        if (res.status() == 200) {
            File file = new File("/ddd", "aaaa.jpg");
            FileUtils.copyInputStreamToFile(res.body().asInputStream(), file);
            return file;
        }
        throw new OCException("下载文件错误");
    }

}