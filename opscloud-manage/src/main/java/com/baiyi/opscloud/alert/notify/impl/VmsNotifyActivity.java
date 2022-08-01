package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.message.driver.AliyunVoiceNotifyDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author 修远
 * @Date 2022/7/21 12:19 AM
 * @Since 1.0
 */
@Slf4j
@Component
public class SmsNotifyActivity extends AbstractNotifyActivity {

    @Autowired
    private ThreadPoolTaskExecutor commonExecutor;

    @Resource
    private AliyunVoiceNotifyDriver aliyunVoiceNotifyDriver;

    @Resource
    private DsConfigHelper dsConfigHelper;

    private static final String MAIN_ALIYUN_INSTANCE_UUID = "75cde081a08646e6b8568b3d34f203a3";

    private static final Integer NO_CALL_TIME = 3;

    private AliyunConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByInstanceUuid(MAIN_ALIYUN_INSTANCE_UUID), AliyunConfig.class);
    }

    @Override
    public void doNotify(AlertNotifyMedia media, AlertContext context) {
        AliyunConfig.Aliyun config = getConfig().getAliyun();
        media.getUsers().forEach(
                user -> commonExecutor.submit(() -> singleCall(config, user.getPhone(), media.getTtsCode()))
        );
    }

    public void singleCall(AliyunConfig.Aliyun config, String phone, String ttsCode) {
        try {
            long callTime = System.currentTimeMillis();
            int time = 1;
            String callId = aliyunVoiceNotifyDriver.singleCallByTts(config.getRegionId(), config, phone, ttsCode);
            do {
                TimeUnit.SECONDS.sleep(90L);
                if (aliyunVoiceNotifyDriver.queryCallDetailByCallId(config.getRegionId(), config, callId, callTime)) {
                    break;
                }
                log.error("电话 = {} , 未接通，90秒后继续拨打，当前第 {} 次", phone, time);
                time++;
                callTime = System.currentTimeMillis();
                callId = aliyunVoiceNotifyDriver.singleCallByTts(config.getRegionId(), config, phone, ttsCode);
            } while (time <= NO_CALL_TIME);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String getKey() {
        return NotifyMediaEnum.SMS.name();
    }
}
