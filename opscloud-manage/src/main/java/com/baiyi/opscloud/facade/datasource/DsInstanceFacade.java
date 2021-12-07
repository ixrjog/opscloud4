package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:18 上午
 * @Version 1.0
 */
public interface DsInstanceFacade<T> {

    DataTable<DsAssetVO.Asset> queryAssetPage(DsAssetParam.AssetPageQuery pageQuery);

    void pullAsset(DsAssetParam.PullAsset pullAsset);

    List<DatasourceInstanceAsset> pullAsset(String instanceUuid, String assetType, T entity);

    void deleteAssetById(Integer id);

    void setDsInstanceConfig(DsAssetParam.SetDsInstanceConfig setDsInstanceConfig);

    void scanAssetBusiness(DsAssetParam.ScanAssetBusiness scanAssetBusiness);
}
