package com.baiyi.opscloud.datasource.factory;

import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetRelationProvider;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.springframework.data.util.CastUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/6/19 7:44 下午
 * @Version 1.0
 */
@Deprecated
public class AssetTargetProviderFactory {

    private AssetTargetProviderFactory() {
    }

    static private Map<String, Map<String, AbstractAssetRelationProvider<?, ?>>> context = new ConcurrentHashMap<>();

    public static <T extends AbstractAssetRelationProvider<?, ?>> T getProvider(String instanceType, String assetType, String targetType) {
        if (context.containsKey(instanceType))
            return CastUtils.cast(context.get(instanceType).get(buildKey(assetType, targetType)));
        return null;
    }

    public static <T extends AbstractAssetRelationProvider<?, ?>> void register(T bean) {
        String key = buildKey(bean);
        if (context.containsKey(bean.getInstanceType())) {
            context.get(bean.getInstanceType()).put(key, bean);
        } else {
            Map<String, AbstractAssetRelationProvider<?, ?>> target = Maps.newConcurrentMap();
            target.put(key, bean);
            context.put(bean.getInstanceType(), target);
        }
    }

    private static <T extends AbstractAssetRelationProvider<?, ?>> String buildKey(T bean) {
        return buildKey(bean.getAssetType(), bean.getTargetAssetKey());
    }

    private static String buildKey(String assetType, String targetType) {
        return Joiner.on(":").join(assetType, targetType);
    }

}
