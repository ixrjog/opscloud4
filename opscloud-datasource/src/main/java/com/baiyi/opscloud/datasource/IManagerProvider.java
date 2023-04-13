package com.baiyi.opscloud.datasource;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.base.BaseBusiness;

/**
 * @Author baiyi
 * @Date 2021/8/24 10:47 上午
 * @Version 1.0
 */
public interface IManagerProvider<T> {

    void create(DatasourceInstance dsInstance, T t);

    void update(DatasourceInstance dsInstance, T t);

    void delete(DatasourceInstance dsInstance, T t);

    void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource);

    void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource);

}
