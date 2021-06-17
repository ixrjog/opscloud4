package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;

/**
 * @Author baiyi
 * @Date 2021/6/17 1:40 下午
 * @Version 1.0
 */
public interface DsInstanceAssetService {

    void add(DatasourceInstanceAsset asset);

    void update(DatasourceInstanceAsset asset);
}
