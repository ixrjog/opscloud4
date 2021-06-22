package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.param.datasource.DsAssetParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/17 1:40 下午
 * @Version 1.0
 */
public interface DsInstanceAssetService {

    void deleteById(Integer id);

    DatasourceInstanceAsset getById(Integer id);

    void add(DatasourceInstanceAsset asset);

    void update(DatasourceInstanceAsset asset);

    DatasourceInstanceAsset getByUniqueKey(DatasourceInstanceAsset asset);

    DataTable<DatasourceInstanceAsset> queryPageByParam(DsAssetParam.AssetPageQuery pageQuery);

    List<String> queryInstanceAssetTypes(String instanceUuid);

    int countByInstanceAssetType(String instanceUuid, String assetType);

}
