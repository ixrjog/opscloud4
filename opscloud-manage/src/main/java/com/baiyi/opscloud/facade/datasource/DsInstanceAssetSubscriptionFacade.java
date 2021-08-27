package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:04 下午
 * @Version 1.0
 */
public interface DsInstanceAssetSubscriptionFacade {

    DataTable<DsAssetSubscriptionVO.AssetSubscription> queryAssetSubscriptionPage(DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery);
}
