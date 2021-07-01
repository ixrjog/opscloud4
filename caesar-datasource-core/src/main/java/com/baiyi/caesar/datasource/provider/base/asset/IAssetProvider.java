package com.baiyi.caesar.datasource.provider.base.asset;

import com.baiyi.caesar.datasource.provider.base.param.AssetFilterParam;
import com.baiyi.caesar.datasource.provider.base.param.UniqueAssetParam;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:46 上午
 * @Version 1.0
 */
public interface IAssetProvider {

    void pullAsset(int dsInstanceId);

    DatasourceInstanceAsset pullAsset(int dsInstanceId, UniqueAssetParam param);

    void pullAsset(int dsInstanceId, AssetFilterParam param);
}
