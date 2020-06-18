package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsSystemuserAssets;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/9 5:29 下午
 * @Version 1.0
 */
public interface AssetsSystemuserAssetsService {

    AssetsSystemuserAssets queryAssetsSystemuserAssetsByUniqueKey(AssetsSystemuserAssets assetsSystemuserAssets);

    List<AssetsSystemuserAssets> queryAssetsSystemuserAssetsByAssetId(String assetId);

    /**
     * 批量删除
     * @param assetId
     */
    void deleteAssetsSystemuserAssetsByAssetId(String assetId);

    void addAssetsSystemuserAssets(AssetsSystemuserAssets assetsSystemuserAssets);

    void deleteAssetsSystemuserAssetsById(int id);
}
