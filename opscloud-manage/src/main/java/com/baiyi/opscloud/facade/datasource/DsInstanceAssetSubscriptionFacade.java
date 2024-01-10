package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:04 下午
 * @Version 1.0
 */
public interface DsInstanceAssetSubscriptionFacade {

    DataTable<DsAssetSubscriptionVO.AssetSubscription> queryAssetSubscriptionPage(DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery);

    void updateAssetSubscription(DsAssetSubscriptionParam.AssetSubscription assetSubscription);

    void publishAssetSubscriptionById(int id);

    void publishAssetSubscription(DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription);

    void addAssetSubscription(DsAssetSubscriptionParam.AssetSubscription assetSubscription);

    void deleteAssetSubscriptionById(int id);

}
