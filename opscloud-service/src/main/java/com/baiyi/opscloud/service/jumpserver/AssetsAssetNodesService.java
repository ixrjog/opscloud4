package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAssetNodes;

/**
 * @Author baiyi
 * @Date 2020/3/9 5:13 下午
 * @Version 1.0
 */
public interface AssetsAssetNodesService {


    AssetsAssetNodes queryAssetsAssetNodesByUniqueKey(AssetsAssetNodes assetsAssetNodes);

    AssetsAssetNodes queryAssetsAssetNodesByAssetId(String assetId);

    void addAssetsAssetNodes(AssetsAssetNodes assetsAssetNodes);

    void delAssetsAssetNodes(int id);
}
