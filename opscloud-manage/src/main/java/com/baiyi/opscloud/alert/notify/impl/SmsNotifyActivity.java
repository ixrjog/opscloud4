package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.message.driver.AliyunVoiceNotifyDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author 修远
 * @Date 2022/7/21 12:19 AM
 * @Since 1.0
 */
@Slf4j
@Component
public class SmsNotifyActivity extends AbstractNotifyActivity {

    @Resource
    private AliyunVoiceNotifyDriver aliyunVoiceNotifyDriver;

    @Resource
    private DsConfigHelper dsConfigHelper;

    private static final String MAIN_ALIYUN_INSTANCE_UUID = "75cde081a08646e6b8568b3d34f203a3";

    private AliyunConfig getConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByInstanceUuid(MAIN_ALIYUN_INSTANCE_UUID), AliyunConfig.class);
    }

    @Override
    public void doNotify(AlertNotifyMedia media, AlertContext context) {
        AliyunConfig.Aliyun config = getConfig().getAliyun();
        media.getUsers().forEach(user ->
                aliyunVoiceNotifyDriver.singleCallByTts(config.getRegionId(), config, user.getPhone(), media.getTtsCode())
        );
    }

    @Override
    public String getKey() {
        return NotifyMediaEnum.SMS.name();
    }
}
