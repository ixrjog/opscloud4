package com.baiyi.opscloud.datasource.aliyun;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/6/18 10:20 上午
 * @Version 1.0
 */
@Slf4j
public class AliyunTest extends BaseUnit {

    @Test
    void pullAssetTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeConstants.ECS.name());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullVpc() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeConstants.VPC.name());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullVSwitch() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeConstants.V_SWITCH.name());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullEcsImage() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeConstants.ECS_IMAGE.name());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullEcsSg() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeConstants.ECS_SG.name());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }
}
