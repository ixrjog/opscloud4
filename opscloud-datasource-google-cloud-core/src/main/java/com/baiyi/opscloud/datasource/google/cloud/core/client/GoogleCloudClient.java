package com.baiyi.opscloud.datasource.google.cloud.core.client;


import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.util.Date;


/**
 * 例子
 * @Author baiyi
 * @Date 2023/3/27 16:24
 * @Version 1.0
 */
public class GoogleCloudClient {

    public static Credentials getCredentials(String accessToken, Date expirationTime) {
        return GoogleCredentials.create(new AccessToken(accessToken, expirationTime));
    }

}
