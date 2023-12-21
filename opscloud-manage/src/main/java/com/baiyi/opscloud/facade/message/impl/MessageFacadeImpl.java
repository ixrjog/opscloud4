package com.baiyi.opscloud.facade.message.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.LXHLConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.baiyi.opscloud.datasource.message.driver.LXHLMessageDriver;
import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatform;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.PlatformNotifyHistory;
import com.baiyi.opscloud.domain.param.message.MessageParam;
import com.baiyi.opscloud.facade.auth.PlatformAuthValidator;
import com.baiyi.opscloud.facade.message.MessageFacade;
import com.baiyi.opscloud.service.auth.PlatformNotifyHistoryService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author 修远
 * @Date 2022/9/7 8:03 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class MessageFacadeImpl implements MessageFacade {

    private final LXHLMessageDriver lxhlMessageDriver;

    private final InstanceHelper instanceHelper;

    private final DsConfigManager dsConfigManager;

    private final PlatformNotifyHistoryService platformNotifyHistoryService;

    private final PlatformAuthValidator platformAuthHelper;

    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.LXHL};
    private static final String SIGN_NAME = "PalmPay";

    @Override
    public LXHLMessageResponse.SendMessage sendMessage(MessageParam.SendMessage param) {
        AuthPlatform authPlatform = platformAuthHelper.verify(param);
        List<DatasourceInstance> instances = instanceHelper.listInstance(FILTER_INSTANCE_TYPES, param.getMedia());
        if (CollectionUtils.isEmpty(instances))
            throw new OCException("无可用实例，请联系运维");
        String mobiles = Joiner.on(",").join(Sets.newHashSet(param.getMobile()));
        LXHLMessageResponse.SendMessage sendMessage = lxhlMessageDriver.sendMessage(getInstanceConfig(instances), mobiles, param.getContent(), SIGN_NAME);
        param.setPlatformToken(StringUtils.EMPTY);
        PlatformNotifyHistory history = PlatformNotifyHistory.builder()
                .platformName(authPlatform.getName())
                .param(JSONUtil.writeValueAsString(param))
                .code(sendMessage.getCode())
                .requestId(sendMessage.getRequestId())
                .build();
        platformNotifyHistoryService.add(history);
        return sendMessage;
    }

    @Override
    public LXHLMessageResponse.SendMessage sendMessage(String media, String mobiles, String platform, String platformToken, MessageParam.GrafanaMessage parma) {
        List<String> mobile = Lists.newArrayList(mobiles.split(","));
        MessageParam.SendMessage param = MessageParam.SendMessage.builder()
                .content(parma.getMessage())
                .media(media)
                .platform(platform)
                .platformToken(platformToken)
                .mobile(mobile)
                .build();
        return sendMessage(param);
    }

    /**
     * 随机取一个实例的配置
     */
    private LXHLConfig.Account getInstanceConfig(List<DatasourceInstance> instances) {
        Collections.shuffle(instances);
        DatasourceInstance instance = instances.getFirst();
        return dsConfigManager.build(dsConfigManager.getConfigById(instance.getConfigId()), LXHLConfig.class).getAccount();
    }

}