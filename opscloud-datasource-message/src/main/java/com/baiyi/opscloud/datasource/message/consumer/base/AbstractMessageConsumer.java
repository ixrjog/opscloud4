package com.baiyi.opscloud.datasource.message.consumer.base;

import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.message.consumer.IMessageConsumer;
import com.baiyi.opscloud.datasource.message.consumer.MessageConsumerFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/2 2:59 PM
 * @Version 1.0
 */
public abstract class AbstractMessageConsumer<T> implements IMessageConsumer, InitializingBean {

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    protected BusinessAssetRelationService businessAssetRelationService;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    protected abstract T buildConfig(DatasourceInstance instance);

    protected DatasourceConfig getConfig(DatasourceInstance instance) {
        return dsConfigManager.getConfigById(instance.getConfigId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageConsumerFactory.register(this);
    }

}