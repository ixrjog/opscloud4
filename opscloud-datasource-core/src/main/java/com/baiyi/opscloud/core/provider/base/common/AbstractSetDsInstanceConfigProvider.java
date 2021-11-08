package com.baiyi.opscloud.core.provider.base.common;

import com.baiyi.opscloud.core.factory.SetDsInstanceConfigFactory;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/24 7:09 下午
 * @Version 1.0
 */
public abstract class AbstractSetDsInstanceConfigProvider<T> extends SimpleDsInstanceProvider implements ISetDsInstanceConfig, IInstanceType, InitializingBean {

    @Resource
    protected DsConfigHelper dsFactory;

    @Resource
    protected StringEncryptor stringEncryptor;

    @Override
    public void setConfig(int dsInstanceId) {
        doSet(buildDsInstanceContext(dsInstanceId));
    }

    protected abstract void doSet(DsInstanceContext dsInstanceContext);

    protected abstract T buildConfig(DatasourceConfig dsConfig);

    @Override
    public void afterPropertiesSet() {
        SetDsInstanceConfigFactory.register(this);
    }
}
