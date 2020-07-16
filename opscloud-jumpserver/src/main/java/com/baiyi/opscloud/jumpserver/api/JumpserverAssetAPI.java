package com.baiyi.opscloud.jumpserver.api;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;
import com.baiyi.opscloud.jumpserver.api.bo.CreateAssetResult;
import com.baiyi.opscloud.jumpserver.config.JumpserverConfig;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/14 5:37 下午
 * @Version 1.0
 */
@Component
public class JumpserverAssetAPI {

    @Resource
    private JumpserverConfig jumpserverConfig;

    private static final String API_ASSETS_CREATE = "/api/v1//assets/assets/";

    @Resource
    private JumpserverAPI jumpserverAPI;

    /**
     * 新增资产
     * @param assetsAsset
     * @return
     */
    public boolean createAsset(AssetsAsset assetsAsset, String nodeId) {
        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put("id",assetsAsset.getId());
        paramsMap.put("hostname", assetsAsset.getHostname());
        paramsMap.put("ip", assetsAsset.getIp());
        paramsMap.put("protocol", assetsAsset.getProtocol());
        paramsMap.put("port", assetsAsset.getPort().toString());
        paramsMap.put("is_active", assetsAsset.getIsActive().toString());
        paramsMap.put("platform", "Linux");
        paramsMap.put("admin_user", assetsAsset.getAdminUserId());
        paramsMap.put("comment", assetsAsset.getComment());
        paramsMap.put("created_by", assetsAsset.getCreatedBy());
        paramsMap.put("nodes", new String[]{nodeId});
        String token = jumpserverAPI.getAdminToken();
        if (StringUtils.isEmpty(token))
            return false;
        String responses = httpPostWithForm(Joiner.on("").join(jumpserverConfig.getUrl(), API_ASSETS_CREATE), JSON.toJSONString(paramsMap), token);
        if (StringUtils.isEmpty(responses))
            return false;
        try {
            CreateAssetResult result = new GsonBuilder().create().fromJson(responses, CreateAssetResult.class);
            if (!StringUtils.isEmpty(result.getId()))
                return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static String httpPostWithForm(String url, String jsonParam, String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (!StringUtils.isEmpty(token)) {
                String bearer = Joiner.on(" ").join("Bearer", token);
                method.addHeader("Authorization", bearer);
            }
            EntityBuilder entityBuilder = EntityBuilder.create();
            entityBuilder.setText(jsonParam);
            entityBuilder.setContentType(ContentType.APPLICATION_JSON);
            entityBuilder.setContentEncoding("UTF-8");
            HttpEntity requestEntity = entityBuilder.build();
            method.setEntity(requestEntity);
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
