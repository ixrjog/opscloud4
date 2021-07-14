package com.baiyi.opscloud.datasource.provider.base.asset;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.datasource.provider.base.param.AssetFilterParam;
import com.baiyi.opscloud.datasource.provider.base.param.UniqueAssetParam;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:46 上午
 * @Version 1.0
 */
public interface IAssetProvider {

    void pullAsset(int dsInstanceId);

    DatasourceInstanceAsset pullAsset(int dsInstanceId, UniqueAssetParam param);

    void pullAsset(int dsInstanceId, AssetFilterParam param);

    List<AssetContainer> queryAssets(int dsInstanceId, Map<String, String> params);
}
