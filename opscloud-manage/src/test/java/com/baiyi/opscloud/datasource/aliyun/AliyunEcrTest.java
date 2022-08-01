package com.baiyi.opscloud.datasource.aliyun;

import com.aliyuncs.cr.model.v20181201.ListRepositoryResponse;
import com.baiyi.opscloud.alert.notify.impl.VmsNotifyActivity;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrRepositoryDriver;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.message.driver.AliyunVoiceNotifyDriver;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2022/7/12 11:31
 * @Version 1.0
 */
public class AliyunEcrTest extends BaseAliyunTest {

    @Resource
    private AliyunAcrRepositoryDriver aliyunAcrRepositoryDriver;

    @Resource
    private AliyunVoiceNotifyDriver aliyunVoiceNotifyDriver;

    @Resource
    private VmsNotifyActivity vmsNotifyActivity;

    private final static String instanceId = "cri-4v9b8l2gc3en0x34";

    @Test
    void listRepositoriesTest() {
        AliyunConfig config = getConfig();
        List<ListRepositoryResponse.RepositoriesItem> repositoriesItems = aliyunAcrRepositoryDriver.listRepositories("eu-west-1", config.getAliyun(), instanceId);
        print(repositoriesItems);
    }

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
