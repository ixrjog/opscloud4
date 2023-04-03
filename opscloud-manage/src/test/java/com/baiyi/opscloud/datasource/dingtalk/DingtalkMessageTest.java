package com.baiyi.opscloud.datasource.dingtalk;

import com.baiyi.opscloud.datasource.dingtalk.base.BaseDingtalkTest;
import com.baiyi.opscloud.datasource.dingtalk.driver.DingtalkMessageDriver;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkMessage;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkMessageParam;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/1 5:15 下午
 * @Version 1.0
 */
public class DingtalkMessageTest extends BaseDingtalkTest {

    @Resource
    private DingtalkMessageDriver dingtalkMessageDrive;

    @Test
    void asyncSendTest() {
        DingtalkMessageParam.Markdown markdown = DingtalkMessageParam.Markdown.builder()
                .title("OPSCLOUD消息通知")
                .text("### OPSCLOUD测试消息  \n  测试: `测试`")
                .build();

        DingtalkMessageParam.Msg msg = DingtalkMessageParam.Msg.builder()
                .markdown(markdown)
                .build();

        DingtalkMessageParam.AsyncSendMessage message = DingtalkMessageParam.AsyncSendMessage.builder()
                .msg(msg)
                .useridList("165924831")
                .build();

        DingtalkMessage.MessageResponse messageResponse = dingtalkMessageDrive.asyncSend(getConfig().getDingtalk(), message);
        print(messageResponse);
    }

}
