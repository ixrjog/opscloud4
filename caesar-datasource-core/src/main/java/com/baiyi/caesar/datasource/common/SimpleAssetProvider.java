package com.baiyi.caesar.datasource.common;

/**
 * @Author baiyi
 * @Date 2021/6/19 3:51 下午
 * @Version 1.0
 */
public interface SimpleAssetProvider extends IRegisterProvider {

    void pullAsset(int dsInstanceId);
}
