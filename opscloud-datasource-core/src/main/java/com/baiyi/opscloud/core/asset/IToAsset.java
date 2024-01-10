package com.baiyi.opscloud.core.asset;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;

/**
 * @Author baiyi
 * @Date 2021/12/21 7:45 PM
 * @Version 1.0
 */
public interface IToAsset {

    AssetContainer toAssetContainer(DatasourceInstance dsInstance);

}