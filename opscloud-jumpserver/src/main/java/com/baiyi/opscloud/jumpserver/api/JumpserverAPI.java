package com.baiyi.opscloud.jumpserver.api;

import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.jumpserver.api.bo.Token;
import com.baiyi.opscloud.jumpserver.api.bo.UsersPubkeyUpdateResult;
import com.baiyi.opscloud.jumpserver.config.JumpserverConfig;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/14 6:30 下午
 * @Version 1.0
 */
@Component
public class JumpserverAPI {

    private static final String ENCODING = "UTF-8";

    @Resource
    private JumpserverConfig jumpserverConfig;

    @Resource
    private StringEncryptor stringEncryptor;

    private static final String API_AUTH = "/api/v1/authentication/auth/";

    private static final String API_USERS_PUBKEY_UPDATE = "/api/v1/users/users/${id}/pubkey/update/";

    private String getUserToken(OcUser ocUser) {
        Map<String, String> authMap = Maps.newHashMap();
        authMap.put("username", ocUser.getUsername());
        authMap.put("password", stringEncryptor.decrypt(ocUser.getPassword()));
        String api = Joiner.on("").join(jumpserverConfig.getUrl(), API_AUTH);
        String responseText = httpPostWithForm(api, authMap);
        Token tokenObj = new GsonBuilder().create().fromJson(responseText, Token.class);
        if (tokenObj == null || StringUtils.isEmpty(tokenObj.getToken()))
            return "";
        return tokenObj.getToken();
    }

    public boolean pushKey(OcUser ocUser, UsersUser usersUser, OcUserCredentialVO.UserCredential credential) {
        Map<String, String> paramsMap = Maps.newHashMap();
        String uid = UUIDUtils.convertUUID(usersUser.getId());
        paramsMap.put("id", uid);
        paramsMap.put("public_key", credential.getCredential());
        String api = API_USERS_PUBKEY_UPDATE.replace("${id}", uid);
        String token = getUserToken(ocUser);
        if(StringUtils.isEmpty(token))
            return false;
        // {"id":"07dd003c-5104-4448-a68d-56e522b5bed3","public_key":null}
        String result = httpPutWithForm(Joiner.on("").join(jumpserverConfig.getUrl(), api), paramsMap, token);
        if(StringUtils.isEmpty(result))
            return false;
        try{
            UsersPubkeyUpdateResult usersPubkeyUpdateResult = new GsonBuilder().create().fromJson(result, UsersPubkeyUpdateResult.class);
            if(uid.equals( usersPubkeyUpdateResult.getId()))
                return true;
        }catch (Exception e){
        }
        return false;
    }

    public static String httpPostWithForm(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            // method.addHeader("Content-Type", "application/json");
            if (paramsMap != null) {
                List<NameValuePair> paramList = Lists.newArrayList();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    public static String httpPutWithForm(String url, Map<String, String> paramsMap,String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPut method = new HttpPut(url);
            String bearer = Joiner.on(" ").join("Bearer", token);
            method.addHeader("Authorization", bearer);
            if (!paramsMap.isEmpty()) {
                List<NameValuePair> paramList = Lists.newArrayList();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }


}
