package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/9 1:29 下午
 * @Version 1.0
 */
public interface AssetsAssetService {

    DataTable<AssetsAsset> queryAssetsAssetPage(AssetsAssetPageParam.PageQuery pageQuery);

    AssetsAsset queryAssetsAssetByIp(String ip);

    AssetsAsset queryAssetsAssetByHostname(String hostname);

    List<AssetsAsset> queryAll();

    void updateAssetsAsset(AssetsAsset assetsAsset);

    void addAssetsAsset(AssetsAsset assetsAsset);

    void deleteAssetsAssetById(String id);

}
