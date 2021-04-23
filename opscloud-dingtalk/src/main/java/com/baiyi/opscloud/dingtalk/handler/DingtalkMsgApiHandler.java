package com.baiyi.opscloud.dingtalk.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.dingtalk.config.DingtalkConfig;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.google.common.base.Joiner;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/18 9:49 上午
 * @Since 1.0
 */
@Slf4j
@Component
public class DingtalkMsgApiHandler extends DingtalkApiHandler {

    private interface MsgApiUrls {
        String sendAsyncMsg = "topapi/message/corpconversation/asyncsend_v2";
    }

    public Long sendAsyncMsg(DingtalkParam.SendAsyncMsg param) {
        DingTalkClient client = getDingTalkClient(MsgApiUrls.sendAsyncMsg);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        DingtalkConfig.DingtalkAccount account = getAccountByUid(param.getUid());
        request.setAgentId(account.getAgentId());
        String useridList = Joiner.on(",").join(param.getUseridList());
        request.setUseridList(useridList);
        request.setMsg(JSON.toJSONString(param.getMsg()));
        try {
            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, param.getAccessToken());
            return checkResponse(response) ? response.getTaskId() : -1L;
        } catch (ApiException e) {
            log.error("发送企业会话消息失败", e);
            return -1L;
        }
    }
}
