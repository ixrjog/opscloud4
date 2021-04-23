package com.baiyi.opscloud.dingtalk.impl;

import com.baiyi.opscloud.dingtalk.DingtalkMsgApi;
import com.baiyi.opscloud.dingtalk.DingtalkTokenApi;
import com.baiyi.opscloud.dingtalk.handler.DingtalkMsgApiHandler;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/18 9:49 上午
 * @Since 1.0
 */

@Component
public class DingtalkMsgApiImpl implements DingtalkMsgApi {

    @Resource
    private DingtalkMsgApiHandler dingtalkMsgApiHandle;

    @Resource
    private DingtalkTokenApi dingtalkTokenApi;

    @Override
    @Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 1000))
    public Long sendAsyncMsg(DingtalkParam.SendAsyncMsg param) {
        String accessToken = dingtalkTokenApi.getDingtalkToken(param.getUid());
        param.setAccessToken(accessToken);
        Long taskId = dingtalkMsgApiHandle.sendAsyncMsg(param);
        if (taskId.equals(-1L))
            throw new RuntimeException("发送企业会话消息失败,retry");
        return taskId;
    }
}
