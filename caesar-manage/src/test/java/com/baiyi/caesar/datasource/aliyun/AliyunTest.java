package com.baiyi.caesar.datasource.aliyun;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.base.asset.SimpleAssetProvider;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
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
