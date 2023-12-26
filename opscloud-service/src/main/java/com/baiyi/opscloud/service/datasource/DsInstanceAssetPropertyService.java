package com.baiyi.opscloud.service.datasource;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:23 上午
 * @Version 1.0
 */
public interface DsInstanceAssetPropertyService {

    void add(DatasourceInstanceAssetProperty property);

    void deleteById(Integer id);

    List<DatasourceInstanceAssetProperty> queryByAssetId(Integer assetId);

    void saveAssetProperties(int assetId, Map<String, String> properties);

    DatasourceInstanceAssetProperty getByUniqueKey(Integer assetId, String name);

}