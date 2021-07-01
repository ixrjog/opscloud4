package com.baiyi.opscloud.datasource.provider.base.common;

import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.factory.SetDsInstanceConfigFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/24 7:09 下午
 * @Version 1.0
 */
public abstract class AbstractSetDsInstanceConfigProvider extends SimpleDsInstanceProvider implements ISetDsInstanceConfig, IInstanceType, InitializingBean {

    @Resource
    protected DsConfigFactory dsFactory;

    @Resource
    protected StringEncryptor stringEncryptor;

    @Override
    public void setConfig(int dsInstanceId) {
        doSet(buildDsInstanceContext(dsInstanceId));
    }

    protected abstract void doSet(DsInstanceContext dsInstanceContext);

    @Override
    public void afterPropertiesSet() {
        SetDsInstanceConfigFactory.register(this);
    }
}
