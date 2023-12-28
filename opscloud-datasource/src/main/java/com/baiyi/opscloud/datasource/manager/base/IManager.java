package com.baiyi.opscloud.datasource.manager.base;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.base.BaseBusiness;

/**
 * @Author baiyi
 * @Date 2021/8/11 11:47 上午
 * @Version 1.0
 */
public interface IManager<T> {

    /**
     * 业务对象创建资产(User -> Account)
     *
     * @param t
     */
    void create(T t);

    /**
     * 业务对象更新资产(User -> Account)
     *
     * @param t
     */
    void update(T t);

    /**
     * 业务对象删除资产(User -> Account)
     *
     * @param t
     */
    void delete(T t);

    void grant(User user, BaseBusiness.IBusiness businessResource);

    void revoke(User user, BaseBusiness.IBusiness businessResource);

}