package com.sdg.cmdb.handler;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.dingtalk.DingTalkContent;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxxiao on 2017/6/29.
 */
@Service
public class DingTalkHandler {

    private static final Logger logger = LoggerFactory.getLogger(DingTalkHandler.class);

    private Map<String, List<String>> notifyMap = new ConcurrentHashMap<>();

    /**
     * 基于合适的上下文，发起叮叮的通知
     *
     * @param content
     * @return
     */
    public boolean doNotify(DingTalkContent content) throws IOException {
        if (StringUtils.isEmpty(content.getWebHook())) {
            return false;
        }

        List<String> notifyList = notifyMap.get(content.getWebHook());
        if (notifyList == null) {
            notifyList = new ArrayList<>();
            notifyMap.put(content.getWebHook(), notifyList);
        }
        notifyList.add(content.getMsg());


        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(content.getWebHook());
        //System.err.println(content.getWebHook());
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(content.getMsg(), "utf-8");
        httppost.setEntity(se);

        HttpResponse response = httpclient.execute(httppost);
        String result = EntityUtils.toString(response.getEntity());
        logger.warn("notify dingtalk content={} result={}", JSON.toJSONString(content), result);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return true;
        } else {
            return false;
        }
    }
}
