package com.baiyi.opscloud.jumpserver.api;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
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
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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
import java.io.UnsupportedEncodingException;
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

    @Resource
    private RedisUtil redisUtil;

    private static final String API_AUTH = "/api/v1/authentication/auth/";

    private static final String API_USERS_PUBKEY_UPDATE = "/api/v1/users/users/${id}/pubkey/update/";

    public String getAdminToken() {
        // JUMPSERVER_ADMIN_TOKEN
        if (redisUtil.hasKey(Global.JUMPSERVER_ADMIN_TOKEN)) {
            String token = (String) redisUtil.get(Global.JUMPSERVER_ADMIN_TOKEN);
            return stringEncryptor.decrypt(token);
        } else {
            String token = getToken(jumpserverConfig.getAdmin().getUsername(), jumpserverConfig.getAdmin().getPassword());
            // 缓存30分钟
            redisUtil.set(Global.JUMPSERVER_ADMIN_TOKEN, stringEncryptor.encrypt(token), 30 * 60);
            return token;
        }
    }

    public String getUserToken(OcUser ocUser) {
        return getToken(ocUser.getUsername(), stringEncryptor.decrypt(ocUser.getPassword()));
    }

    private String getToken(String username, String password) {
        Map<String, String> authMap = Maps.newHashMap();
        authMap.put("username", username);
        authMap.put("password", password);
        String api = Joiner.on("").join(jumpserverConfig.getUrl(), API_AUTH);
        String responseText = httpPostWithForm(api, authMap, null);
        Token tokenObj = new GsonBuilder().create().fromJson(responseText, Token.class);
        if (tokenObj == null || StringUtils.isEmpty(tokenObj.getToken()))
            return "";
        return tokenObj.getToken();
    }

    public boolean pushKey(OcUser ocUser, UsersUser usersUser, UserCredentialVO.UserCredential credential) {
        Map<String, String> paramsMap = Maps.newHashMap();
        String uid = UUIDUtils.convertUUID(usersUser.getId());
        paramsMap.put("id", uid);
        paramsMap.put("public_key", credential.getCredential());
        String api = API_USERS_PUBKEY_UPDATE.replace("${id}", uid);
        String token = getUserToken(ocUser);
        if (StringUtils.isEmpty(token))
            return false;
        // {"id":"07dd003c-5104-4448-a68d-56e522b5bed3","public_key":null}
        String result = httpPutWithForm(Joiner.on("").join(jumpserverConfig.getUrl(), api), paramsMap, token);
        if (StringUtils.isEmpty(result))
            return false;
        try {
            UsersPubkeyUpdateResult usersPubkeyUpdateResult = new GsonBuilder().create().fromJson(result, UsersPubkeyUpdateResult.class);
            if (uid.equals(usersPubkeyUpdateResult.getId()))
                return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static String httpPostWithForm(String url, Map<String, String> paramMap, String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (!StringUtils.isEmpty(token)) {
                String bearer = Joiner.on(" ").join("Bearer", token);
                method.addHeader("Authorization", bearer);
            }
            invokeParams(method, paramMap);
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

    private static void invokeParams(HttpEntityEnclosingRequestBase method, Map<String, String> paramsMap) {
        if (paramsMap == null) return;
        List<NameValuePair> paramList = Lists.newArrayList();
        paramsMap.forEach((key, value) -> {
            NameValuePair pair = new BasicNameValuePair(key, value);
            paramList.add(pair);
        });
        try {
            method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
        } catch (UnsupportedEncodingException ignored) {
        }
    }

    public static String httpPutWithForm(String url, Map<String, String> paramsMap, String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPut method = new HttpPut(url);
            String bearer = Joiner.on(" ").join("Bearer", token);
            method.addHeader("Authorization", bearer);
            invokeParams(method, paramsMap);
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
