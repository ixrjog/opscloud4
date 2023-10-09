package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.message.driver.AliyunSmsDriver;
import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertNotifyMedia;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2022/8/1 8:06 PM
 * @Since 1.0
 */

@Slf4j
@Component
public class SmsNotifyActivity extends AbstractNotifyActivity {

    @Resource
    private AliyunSmsDriver aliyunSmsDriver;

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    private RedisUtil redisUtil;

    private static final String MAIN_ALIYUN_INSTANCE_UUID = "75cde081a08646e6b8568b3d34f203a3";
    private static final String PREFIX = "sms_notify";

    @Override
    public void doNotify(AlertNotifyMedia media, AlertContext context) {
        if (CollectionUtils.isEmpty(media.getUsers())) {
            return;
        }
        Set<String> phones = media.getUsers().stream().map(User::getPhone).collect(Collectors.toSet());
        String cacheKey = getCacheKeyPrefix(context);
        if (redisUtil.hasKey(cacheKey)) {
            log.debug("短信发送静默中，key={}", cacheKey);
            return;
        }
        aliyunSmsDriver.sendBatchSms(getConfig().getAliyun(), phones, media.getTemplateCode());
        saveAlertNotify(context, buildAlertNotifyHistoryList(media));
        redisUtil.set(cacheKey, true, 60 * 60);
    }

    private AliyunConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByInstanceUuid(MAIN_ALIYUN_INSTANCE_UUID), AliyunConfig.class);
    }

    private List<AlertNotifyHistory> buildAlertNotifyHistoryList(AlertNotifyMedia media) {
        return media.getUsers().stream().map(user -> {
            AlertNotifyHistory alertNotifyHistory = buildAlertNotifyHistory();
            alertNotifyHistory.setUsername(user.getUsername());
            return alertNotifyHistory;
        }).collect(Collectors.toList());

    }

    private String getCacheKeyPrefix(AlertContext context) {
        return Joiner.on("#").join(PREFIX, getKey().toLowerCase(), context.getService());
    }

    @Override
    public String getKey() {
        return NotifyMediaEnum.SMS.name();
    }

}
