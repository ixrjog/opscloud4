package com.baiyi.opscloud.datasource.factory;


import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import com.google.common.collect.ArrayListMultimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.CastUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/6/18 11:06 上午
 * @Version 1.0
 */
@Slf4j
public class AssetProviderFactory {

    private AssetProviderFactory() {
    }

    /**
     * Map<String instanceType, ArrayListMultimap<String assetType, SimpleAssetProvider>>
     */
    private static Map<String, ArrayListMultimap<String, SimpleAssetProvider>> context = new ConcurrentHashMap<>();

    public static <T extends SimpleAssetProvider> T getProvider(String instanceType, String assetType) {
        if (context.containsKey(instanceType))
            return CastUtils.cast(context.get(instanceType).get(assetType).get(0));
        return null;
    }

    public static <T extends SimpleAssetProvider> List<T> getProviders(String instanceType, String assetType) {
        if (context.containsKey(instanceType))
            return CastUtils.cast(context.get(instanceType).get(assetType));
        return null;
    }

    public static <T extends SimpleAssetProvider> void register(T bean) {
        log.info("AssetProviderFactory注册: beanName = {} , instanceType = {} , assetType = {}", bean.getClass().getSimpleName(), bean.getInstanceType(), bean.getAssetType());
        if (context.containsKey(bean.getInstanceType())) {
            context.get(bean.getInstanceType()).put(bean.getAssetType(), bean);
        } else {
            ArrayListMultimap<String, SimpleAssetProvider> multimap = ArrayListMultimap.create();
            multimap.put(bean.getAssetType(), bean);
            context.put(bean.getInstanceType(), multimap);
        }
    }
}
