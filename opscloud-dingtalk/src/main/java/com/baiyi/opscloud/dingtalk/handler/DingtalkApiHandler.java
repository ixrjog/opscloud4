package com.baiyi.opscloud.dingtalk.handler;

import com.baiyi.opscloud.dingtalk.config.DingtalkConfig;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.google.common.base.Joiner;
import com.taobao.api.TaobaoResponse;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 9:45 上午
 * @Since 1.0
 */
public class DingtalkApiHandler {

    @Resource
    private DingtalkConfig dingtalkConfig;

    public DingTalkClient getDingTalkClient(String apiUrl) {
        String url = Joiner.on("/").join(dingtalkConfig.getApiUrl(), apiUrl);
        return new DefaultDingTalkClient(url);
    }

    public Boolean checkResponse(TaobaoResponse response) {
        return "0".equals(response.getErrorCode());
    }

    public DingtalkConfig.DingtalkAccount getMasterAccount() {
        for (DingtalkConfig.DingtalkAccount account : dingtalkConfig.getAccounts())
            if (account.getMaster())
                return account;
        return null;
    }

    public DingtalkConfig.DingtalkAccount getAccountByUid(String uid) {
        for (DingtalkConfig.DingtalkAccount account : dingtalkConfig.getAccounts())
            if (account.getUid().equals(uid))
                return account;
        return null;
    }
}

