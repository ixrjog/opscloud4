package com.baiyi.opscloud.core.provider.base.asset;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:46 上午
 * @Version 1.0
 */
public interface IAssetProvider<T> {

    /**
     * 拉取资产
     * @param dsInstanceId
     */
    void pullAsset(int dsInstanceId);

    /**
     * 推送资产
     * @param dsInstanceId
     */
    void pushAsset(int dsInstanceId);

    /**
     * 拉取单个资产
     * @param dsInstanceId
     * @param entity
     * @return
     */
    DatasourceInstanceAsset pullAsset(int dsInstanceId, T entity);

}