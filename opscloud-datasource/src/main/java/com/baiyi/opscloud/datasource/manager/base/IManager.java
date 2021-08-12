package com.baiyi.opscloud.datasource.manager.base;

/**
 * @Author baiyi
 * @Date 2021/8/11 11:47 上午
 * @Version 1.0
 */
public interface IManager<T> {

    void create(T t);

    void update(T t);

    void delete(T t);

    //void revoke(T t,String obj);
}
