package com.baiyi.opscloud.dingtalk.impl;

import com.baiyi.opscloud.dingtalk.DingtalkService;
import com.baiyi.opscloud.dingtalk.content.DingtalkContent;
import com.baiyi.opscloud.dingtalk.handler.DingtalkHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/16 1:35 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class DingTalkServiceImpl implements DingtalkService {

    @Resource
    private DingtalkHandler dingtalkHandler;

    @Override
    public Boolean doNotify(DingtalkContent content) {
        try {
            return dingtalkHandler.doNotify(content);
        } catch (IOException e) {
            log.error("发送钉钉消息失败", e);
            return false;
        }
    }

    @Override
    public DingtalkContent getDingtalkContent(String dingtalkToken, String msg) {
        return dingtalkHandler.getDingtalkContent(dingtalkToken, msg);
    }

}
