package com.baiyi.caesar.datasource.aliyun;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.datasource.common.IElasticComputeProvider;
import com.baiyi.caesar.datasource.factory.ElasticComputeProviderFactory;
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
        IElasticComputeProvider iComputeProvider = ElasticComputeProviderFactory.getProvider("ALIYUN", "ECS");
        assert iComputeProvider != null;
        iComputeProvider.pullAsset(3);
    }
    
}
