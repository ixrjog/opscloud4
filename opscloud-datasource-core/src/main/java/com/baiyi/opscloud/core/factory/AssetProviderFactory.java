package com.baiyi.opscloud.core.factory;


import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
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
@SuppressWarnings("rawtypes")
@Slf4j
public class AssetProviderFactory {

    private AssetProviderFactory() {
    }

    /**
     * Map<String instanceType, ArrayListMultimap<String assetType, SimpleAssetProvider>>
     */
    private static final Map<String, ArrayListMultimap<String, SimpleAssetProvider>> CONTEXT = new ConcurrentHashMap<>();

    public static <T extends SimpleAssetProvider> T getProvider(String instanceType, String assetType) {
        if (CONTEXT.containsKey(instanceType)) {
            return CastUtils.cast(CONTEXT.get(instanceType).get(assetType).getFirst());
        }
        return null;
    }

    public static <T extends SimpleAssetProvider> List<T> getProviders(String instanceType, String assetType) {
        if (CONTEXT.containsKey(instanceType)) {
            return CastUtils.cast(CONTEXT.get(instanceType).get(assetType));
        }
        return null;
    }

    public static <T extends SimpleAssetProvider> void register(T bean) {
        if (CONTEXT.containsKey(bean.getInstanceType())) {
            CONTEXT.get(bean.getInstanceType()).put(bean.getAssetType(), bean);
        } else {
            ArrayListMultimap<String, SimpleAssetProvider> map = ArrayListMultimap.create();
            map.put(bean.getAssetType(), bean);
            CONTEXT.put(bean.getInstanceType(), map);
        }
        log.debug("AssetProviderFactory Registered: beanName={}, instanceType={}, assetType={}", bean.getClass().getSimpleName(), bean.getInstanceType(), bean.getAssetType());
    }

}