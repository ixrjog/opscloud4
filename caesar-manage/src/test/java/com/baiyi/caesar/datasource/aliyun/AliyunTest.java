package com.baiyi.caesar.datasource.aliyun;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.datasource.common.SimpleAssetProvider;
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
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider("ALIYUN", "ECS");
        assert assetProvider != null;
        assetProvider.pullAsset(3);
    }
    
}
