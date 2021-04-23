package com.baiyi.opscloud.dingtalk.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.dingtalk.config.DingtalkConfig;
import com.baiyi.opscloud.dingtalk.content.DingtalkContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/16 11:10 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class DingtalkHandler {

    @Resource
    private DingtalkConfig dingtalkConfig;

    private Map<String, List<String>> notifyMap = new ConcurrentHashMap<>();

    /**
     * 基于合适的上下文，发起钉钉通知
     *
     * @param content
     * @return
     */
    public boolean doNotify(DingtalkContent content) throws IOException {
        if (StringUtils.isEmpty(content.getWebHook()))
            return false;
        List<String> notifyList = notifyMap.computeIfAbsent(content.getWebHook(), k -> new ArrayList<>());
        notifyList.add(content.getMsg());
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(content.getWebHook());
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(content.getMsg(), "utf-8");
        httppost.setEntity(se);
        HttpResponse response = httpclient.execute(httppost);
        String result = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            log.info("钉钉消息发送成功，content={}", JSON.toJSONString(content));
            return true;
        } else {
            log.info("钉钉消息发送失败，content={}", JSON.toJSONString(content));
            return false;
        }
    }

    public DingtalkContent getDingtalkContent(String dingtalkToken, String msg) {
        String webHook = dingtalkConfig.getWebHook(dingtalkToken);
        return DingtalkContent.builder()
                .webHook(webHook)
                .msg(msg)
                .build();
    }
}