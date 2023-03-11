package com.baiyi.opscloud.datasource.facade;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:18 上午
 * @Version 1.0
 */
public interface DsInstanceFacade<T> {

    /**
     * 拉取资产
     * @param pullAsset
     */
    void pullAsset(DsAssetParam.PullAsset pullAsset);

    /**
     * 推送资产
     * @param pushAsset
     */
    void pushAsset(DsAssetParam.PushAsset pushAsset);

    /**
     * 拉取资产
     * @param instanceUuid
     * @param assetType
     * @param entity
     * @return
     */
    List<DatasourceInstanceAsset> pullAsset(String instanceUuid, String assetType, T entity);

    void setDsInstanceConfig(DsAssetParam.SetDsInstanceConfig setDsInstanceConfig);

    void scanAssetBusiness(DsAssetParam.ScanAssetBusiness scanAssetBusiness);

}
