package com.baiyi.caesar.common.factory;

/**
 * @Author baiyi
 * @Date 2021/6/18 9:16 上午
 * @Version 1.0
 */
public interface BaseProviderFactory<T> {





     T getProviderByKey(String key) ;

    void register(T bean) ;
}
