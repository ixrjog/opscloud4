package com.baiyi.opscloud.jumpserver.api;

import com.baiyi.opscloud.jumpserver.api.bo.Auth;
import com.baiyi.opscloud.jumpserver.api.bo.Token;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/3/14 6:30 下午
 * @Version 1.0
 */
@Component
public class JumpserverAPI {


    public String getToken() {
        Auth auth = Auth.builder()
                .username("admin")
                .password("admin")
                .build();
        String reslut = doPost("https://jump.ops.yangege.cn/api/v1/authentication/auth/", auth);
        Token token = new GsonBuilder().create().fromJson(reslut, Token.class);
        System.err.println(token.getToken());
        return token.getToken();
    }


    private String doPost(String webHook, Object body) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(webHook);
        // charset=utf-8
        httppost.addHeader("Content-Type", "application/json");
        // httppost.addHeader("Token", rocketToken); // 插入Token
        StringEntity se = new StringEntity(body.toString(), "utf-8");
        httppost.setEntity(se);
        try {
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                return result;
//                RocketAppResponse rocketAppResponse = new GsonBuilder().create().fromJson(result, RocketAppResponse.class);
//                if (rocketAppResponse.isStatus()) {
//                    return rocketAppResponse.getData();
//                } else {
//                    return RocketApp.builder().build();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
          //  log.error("RocketAPI接口请求失败,API:{},错误:{}", webHook, e.getMessage());
        }
       return "";
    }


}
