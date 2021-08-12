package com.baiyi.opscloud.datasource.account;

import com.baiyi.opscloud.datasource.provider.base.common.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;

/**
 * @Author baiyi
 * @Date 2021/8/11 1:41 下午
 * @Version 1.0
 */
public interface IAccount extends IInstanceType {

    void create(DatasourceInstance dsInstance, User user);

    void update(DatasourceInstance dsInstance, User user);

    void delete(DatasourceInstance dsInstance, User user);

}