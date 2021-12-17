package com.baiyi.opscloud.core.provider.base.asset;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:46 上午
 * @Version 1.0
 */
public interface IAssetProvider<T> {

    void pullAsset(int dsInstanceId);

    void pushAsset(int dsInstanceId);

    DatasourceInstanceAsset pullAsset(int dsInstanceId, T entity);

}
