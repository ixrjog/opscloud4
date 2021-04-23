package com.baiyi.opscloud.dingtalk.handler;

import com.baiyi.opscloud.dingtalk.config.DingtalkConfig;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 9:49 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class DingtalkTokenApiHandler extends DingtalkApiHandler {

    private interface TokenApiUrls {
        String getToken = "gettoken";
    }

    public OapiGettokenResponse getToken(String uid) {
        DingTalkClient client = getDingTalkClient(TokenApiUrls.getToken);
        OapiGettokenRequest request = new OapiGettokenRequest();
        DingtalkConfig.DingtalkAccount account = getAccountByUid(uid);
        request.setAppkey(account.getAppKey());
        request.setAppsecret(account.getAppSecret());
        try {
            OapiGettokenResponse response = client.execute(request);
            return checkResponse(response) ? response : null;
        } catch (ApiException e) {
            log.error("获取dingtalk Token失败", e);
            return null;
        }
    }
}