package com.baiyi.opscloud.datasource.message.customer.base;

import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.message.customer.IMessageCustomer;
import com.baiyi.opscloud.datasource.message.customer.MessageCustomerFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/2 2:59 PM
 * @Version 1.0
 */
public abstract class AbstractMessageCustomer<T> implements IMessageCustomer, InitializingBean {

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected BusinessAssetRelationService businessAssetRelationService;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    protected abstract T buildConfig(DatasourceInstance instance);

    protected DatasourceConfig getConfig(DatasourceInstance instance) {
        return dsConfigHelper.getConfigById(instance.getConfigId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageCustomerFactory.register(this);
    }
}
