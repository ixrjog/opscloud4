package com.baiyi.caesar.datasource.common;

/**
 * @Author baiyi
 * @Date 2021/6/18 10:17 上午
 * @Version 1.0
 */
public interface IElasticComputeProvider extends IDsProvider {

    void pullAsset(int dsInstanceId);

}
