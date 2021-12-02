package com.baiyi.opscloud.datasource.message.customer;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;

/**
 * @Author baiyi
 * @Date 2021/12/2 2:52 PM
 * @Version 1.0
 */
public interface IMessageCustomer {

    String getInstanceType();

    void send(DatasourceInstance instance, User user, MessageTemplate mt, String text);
}
