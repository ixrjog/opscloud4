package com.baiyi.caesar.datasource.factory;


import com.baiyi.caesar.datasource.common.SimpleAssetProvider;
import com.google.common.collect.Maps;
import org.springframework.data.util.CastUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/6/18 11:06 上午
 * @Version 1.0
 */

public class AssetProviderFactory {

    private AssetProviderFactory() {}

    //         instanceType & key
    static private Map<String, Map<String, SimpleAssetProvider>> context = new ConcurrentHashMap<>();

    public static <T extends SimpleAssetProvider> T getProvider(String instanceType, String assetType) {
        if (context.containsKey(instanceType))
            return CastUtils.cast(context.get(instanceType).get(assetType));
        return null;
    }

    public static <T extends SimpleAssetProvider> void register(T bean) {
        if (context.containsKey(bean.getInstanceType())) {
            context.get(bean.getInstanceType()).put(bean.getAssetType(), bean);
        } else {
            Map<String, SimpleAssetProvider> elasticComputeContext = Maps.newConcurrentMap();
            elasticComputeContext.put(bean.getAssetType(), bean);
            context.put(bean.getInstanceType(), elasticComputeContext);
        }
    }
}
