package com.baiyi.opscloud.datasource.scan;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetBusinessRelationProvider;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/2 4:39 下午
 * @Version 1.0
 */
public class ScanAssetTest extends BaseUnit {

    @Test
    void scanTest(){
        List<SimpleAssetProvider> providers = AssetProviderFactory.getProviders("ALIYUN", "VPC");

        for (SimpleAssetProvider provider : providers) {
            if(provider instanceof AbstractAssetBusinessRelationProvider){
                System.err.println("AAAAAA");
                // ((AbstractAssetBusinessRelationProvider) provider).scan();
            }

        }


    }
}
