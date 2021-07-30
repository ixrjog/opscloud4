package com.baiyi.opscloud.datasource.aliyun;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
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
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeEnum.ECS.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullVpc() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeEnum.VPC.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullVSwitch() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeEnum.V_SWITCH.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullEcsImage() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeEnum.ECS_IMAGE.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }

    @Test
    void pullEcsSg() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ALIYUN.getName(), DsAssetTypeEnum.ECS_SG.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }
}
