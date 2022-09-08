package com.baiyi.opscloud.facade.message.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.LXHLConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.baiyi.opscloud.datasource.message.driver.LXHLMessageDriver;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.message.MessageParam;
import com.baiyi.opscloud.facade.message.MessageFacade;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
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
    private final DsConfigHelper dsConfigHelper;
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.LXHL};
    private static final String SIGN_NAME = "PalmPay";

    @Override
    public LXHLMessageResponse.SendMessage sendMessage(MessageParam.SendMessage param) {
        List<DatasourceInstance> instances = instanceHelper.listInstance(FILTER_INSTANCE_TYPES, param.getMedia());
        if (CollectionUtils.isEmpty(instances))
            throw new CommonRuntimeException("无可用实例，请联系运维");
        String mobiles = Joiner.on(",").join(Sets.newHashSet(param.getMobile()));
        lxhlMessageDriver.sendMessage(getInstanceConfig(instances), mobiles, param.getContent(), SIGN_NAME);
        return null;
    }

    /**
     * 随机取一个实例的配置
     */
    private LXHLConfig.Account getInstanceConfig(List<DatasourceInstance> instances) {
        Collections.shuffle(instances);
        DatasourceInstance instance = instances.get(0);
        return dsConfigHelper.build(dsConfigHelper.getConfigById(instance.getConfigId()), LXHLConfig.class).getAccount();
    }
}
