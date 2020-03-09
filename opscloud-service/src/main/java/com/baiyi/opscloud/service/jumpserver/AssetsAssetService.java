package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;

/**
 * @Author baiyi
 * @Date 2020/3/9 1:29 下午
 * @Version 1.0
 */
public interface AssetsAssetService {

    AssetsAsset queryAssetsAssetByIp(String ip);

    AssetsAsset queryAssetsAssetByHostname(String hostname);

    void updateAssetsAsset(AssetsAsset assetsAsset);

    void addAssetsAsset(AssetsAsset assetsAsset);

}
