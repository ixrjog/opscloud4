package com.baiyi.opscloud.datasource.aliyun;

import com.baiyi.opscloud.alert.notify.impl.VmsNotifyActivity;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.message.driver.AliyunSmsDriver;
import com.baiyi.opscloud.datasource.message.driver.AliyunVmsDriver;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2022/8/2 14:04
 * @Version 1.0
 */
public class AliyunMsTest extends BaseAliyunTest {

    @Resource
    private AliyunVmsDriver aliyunVmsDriver;

    @Resource
    private AliyunSmsDriver aliyunSmsDriver;

    @Resource
    private VmsNotifyActivity vmsNotifyActivity;

    @Test
    void xx() throws InterruptedException {
        Long date = System.currentTimeMillis();
        AliyunConfig config = getConfig();
        String phone = "13456768044";
        String ttsCode = "TTS_246450043";
        String callId = aliyunVmsDriver.singleCallByTts("eu-west-1", config.getAliyun(), phone, ttsCode);
//        String callId = "130094535587^116873080590";
        aliyunVmsDriver.queryCallDetailByCallId("eu-west-1", config.getAliyun(), callId, date);
        NewTimeUtil.sleep(10L);
        aliyunVmsDriver.queryCallDetailByCallId("eu-west-1", config.getAliyun(), callId, date);
        NewTimeUtil.sleep(10L);
        aliyunVmsDriver.queryCallDetailByCallId("eu-west-1", config.getAliyun(), callId, date);
    }

    @Test
    void xxx() {
        String phone = "15067127069";
        String ttsCode = "TTS_246450043";
        AliyunConfig config = getConfig();
        vmsNotifyActivity.singleCall(config.getAliyun(), phone, ttsCode);
    }

    @Test
    void xxxxx() {
        String phone1 = "15067127069";
        String phone2 = "15757185179";
        String templateCode = "SMS_247610164";
        Set<String> phones = Sets.newHashSet(phone1, phone2);
        AliyunConfig config = getConfig();
        aliyunSmsDriver.sendBatchSms(config.getAliyun(), phones, templateCode);
    }
}
