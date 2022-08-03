package com.baiyi.opscloud.datasource.aliyun;

import com.baiyi.opscloud.alert.notify.impl.VmsNotifyActivity;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.message.driver.AliyunVoiceNotifyDriver;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2022/8/2 14:04
 * @Version 1.0
 */
public class AliyunMsTest extends BaseAliyunTest {

    @Resource
    private AliyunVoiceNotifyDriver aliyunVoiceNotifyDriver;

    @Resource
    private VmsNotifyActivity vmsNotifyActivity;

    @Test
    void xx() throws InterruptedException {
        Long date = System.currentTimeMillis();
        AliyunConfig config = getConfig();
        String phone = "13456768044";
        String ttsCode = "TTS_246450043";
        String callId = aliyunVoiceNotifyDriver.singleCallByTts("eu-west-1", config.getAliyun(), phone, ttsCode);
//        String callId = "130094535587^116873080590";
        aliyunVoiceNotifyDriver.queryCallDetailByCallId("eu-west-1", config.getAliyun(), callId, date);
        TimeUnit.SECONDS.sleep(10L);
        aliyunVoiceNotifyDriver.queryCallDetailByCallId("eu-west-1", config.getAliyun(), callId, date);
        TimeUnit.SECONDS.sleep(10L);
        aliyunVoiceNotifyDriver.queryCallDetailByCallId("eu-west-1", config.getAliyun(), callId, date);
    }

    @Test
    void xxx() {
        String phone = "15067127069";
        String ttsCode = "TTS_246450043";
        AliyunConfig config = getConfig();
        vmsNotifyActivity.singleCall(config.getAliyun(), phone, ttsCode);
    }

}
