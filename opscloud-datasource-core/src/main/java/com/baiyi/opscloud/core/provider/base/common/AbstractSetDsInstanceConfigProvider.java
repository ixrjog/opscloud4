package com.baiyi.opscloud.core.provider.base.common;

import com.baiyi.opscloud.core.factory.SetDsInstanceConfigFactory;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/24 7:09 下午
 * @Version 1.0
 */
public abstract class AbstractSetDsInstanceConfigProvider<T> extends SimpleDsInstanceProvider implements ISetDsInstanceConfig, IInstanceType, InitializingBean {

    @Resource
    protected DsConfigManager dsFactory;

    @Resource
    protected StringEncryptor stringEncryptor;

    @Override
    public void setConfig(int dsInstanceId) {
        doSet(buildDsInstanceContext(dsInstanceId));
    }

    /**
     * 设置配置
     * @param dsInstanceContext
     */
    protected abstract void doSet(DsInstanceContext dsInstanceContext);

    /**
     * 构建配置
     * @param dsConfig
     * @return
     */
    protected abstract T buildConfig(DatasourceConfig dsConfig);

    @Override
    public void afterPropertiesSet() {
        SetDsInstanceConfigFactory.register(this);
    }

}