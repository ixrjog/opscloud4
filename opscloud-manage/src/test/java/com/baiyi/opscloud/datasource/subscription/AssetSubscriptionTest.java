package com.baiyi.opscloud.datasource.subscription;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/31 2:36 下午
 * @Version 1.0
 */
public class AssetSubscriptionTest extends BaseUnit {

    @Resource
    private DsInstanceAssetSubscriptionFacade dsInstanceAssetSubscriptionFacade;

    @Test
    void publish() {
        dsInstanceAssetSubscriptionFacade.publishAssetSubscriptionById(1);
    }

}
