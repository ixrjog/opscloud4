package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.alert.notify.NotifyStatusEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.message.driver.AliyunVmsDriver;
import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertNotifyMedia;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyEvent;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Author 修远
 * @Date 2022/7/21 12:19 AM
 * @Since 1.0
 */
@Slf4j
@Component
public class VmsNotifyActivity extends AbstractNotifyActivity {

    @Resource
    private AliyunVmsDriver aliyunVmsDriver;

    @Resource
    private DsConfigManager dsConfigManager;

    private static final String MAIN_ALIYUN_INSTANCE_UUID = "75cde081a08646e6b8568b3d34f203a3";

    private static final Integer NO_CALL_TIME = 3;

    private static final long sleepTime = 90L;

    private AliyunConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByInstanceUuid(MAIN_ALIYUN_INSTANCE_UUID), AliyunConfig.class);
    }

    @Override
    public void doNotify(AlertNotifyMedia media, AlertContext context) {
        if (CollectionUtils.isEmpty(media.getUsers())) {
            return;
        }
        AliyunConfig.Aliyun config = getConfig().getAliyun();
        AlertNotifyEvent event = alertNotifyEventService.getByUuid(context.getEventUuid());

        media.getUsers().forEach(
                // JDK21 VirtualThreads
                user -> Thread.ofVirtual().start(
                        () -> {
                            AlertNotifyHistory alertNotifyHistory = buildAlertNotifyHistory();
                            alertNotifyHistory.setUsername(user.getUsername());
                            alertNotifyHistory.setAlertNotifyEventId(event.getId());
                            if (singleCall(config, user.getPhone(), media.getTtsCode())) {
                                alertNotifyHistory.setAlertNotifyStatus(NotifyStatusEnum.CALL_OK.getName());
                            } else {
                                alertNotifyHistory.setAlertNotifyStatus(NotifyStatusEnum.CALL_ERR.getName());
                            }
                            alertNotifyHistoryService.add(alertNotifyHistory);
                        }
                )
        );
    }

    public Boolean singleCall(AliyunConfig.Aliyun config, String phone, String ttsCode) {
        long callTime = System.currentTimeMillis();
        int time = 1;
        String callId = aliyunVmsDriver.singleCallByTts(config.getRegionId(), config, phone, ttsCode);
        do {
            NewTimeUtil.sleep(sleepTime);
            if (aliyunVmsDriver.queryCallDetailByCallId(config.getRegionId(), config, callId, callTime)) {
                return true;
            }
            log.error("电话 {} 未接通，{}(秒)后继续拨打，当前第 {} 次", phone, sleepTime, time);
            callTime = System.currentTimeMillis();
            callId = aliyunVmsDriver.singleCallByTts(config.getRegionId(), config, phone, ttsCode);
            time++;
        } while (time <= NO_CALL_TIME);
        return false;
    }

    @Override
    public String getKey() {
        return NotifyMediaEnum.VMS.name();
    }

}