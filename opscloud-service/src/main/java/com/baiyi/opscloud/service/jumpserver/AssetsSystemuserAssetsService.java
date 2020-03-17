package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsSystemuserAssets;

/**
 * @Author baiyi
 * @Date 2020/3/9 5:29 下午
 * @Version 1.0
 */
public interface AssetsSystemuserAssetsService {

    AssetsSystemuserAssets queryAssetsSystemuserAssetsByUniqueKey(AssetsSystemuserAssets assetsSystemuserAssets);

    void addAssetsSystemuserAssets(AssetsSystemuserAssets assetsSystemuserAssets);
}
