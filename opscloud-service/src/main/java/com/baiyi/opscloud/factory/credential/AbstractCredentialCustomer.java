package com.baiyi.opscloud.factory.credential;

import com.baiyi.opscloud.factory.credential.base.ICredentialCustomer;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2021/10/27 10:47 上午
 * @Version 1.0
 */
public abstract class AbstractCredentialCustomer implements ICredentialCustomer, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        CredentialCustomerFactory.register(this);
    }

}