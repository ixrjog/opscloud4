package com.baiyi.opscloud.datasource.factory;

import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetRelationProvider;
import com.google.common.collect.Maps;
import org.springframework.data.util.CastUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/6/19 7:44 下午
 * @Version 1.0
 */
public class AssetTargetProviderFactory {

    private AssetTargetProviderFactory() {
    }

    //         instanceType & key
    static private Map<String, Map<String, AbstractAssetRelationProvider<?,?>>> context = new ConcurrentHashMap<>();


    public static <T extends AbstractAssetRelationProvider<?,?>> T getProvider(String instanceType, String key) {
        if (context.containsKey(instanceType))
            return CastUtils.cast(context.get(instanceType).get(key));
        return null;
    }

    public static <T extends AbstractAssetRelationProvider<?,?>> void register(T bean) {
        if (context.containsKey(bean.getInstanceType()))
            context.get(bean.getInstanceType()).put(bean.getAssetType(), bean);
        Map<String, AbstractAssetRelationProvider<?,?>> elasticComputeContext = Maps.newConcurrentMap();
        elasticComputeContext.put(bean.getAssetType(), bean);
        context.put(bean.getInstanceType(), elasticComputeContext);
    }
}
