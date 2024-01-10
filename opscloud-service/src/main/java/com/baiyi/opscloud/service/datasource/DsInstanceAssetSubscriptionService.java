package com.baiyi.opscloud.service.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/27 2:50 下午
 * @Version 1.0
 */
public interface DsInstanceAssetSubscriptionService {

    void add(DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription);

    void update(DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription);

    void deleteById(int id);

    DatasourceInstanceAssetSubscription getById(int id);

    List<DatasourceInstanceAssetSubscription> queryByAssetId(int assetId);

    DataTable<DatasourceInstanceAssetSubscription> queryPageByParam(DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery);

}