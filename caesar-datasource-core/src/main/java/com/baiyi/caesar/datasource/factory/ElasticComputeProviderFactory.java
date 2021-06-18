package com.baiyi.caesar.datasource.factory;


import com.baiyi.caesar.datasource.common.IElasticComputeProvider;
import com.google.common.collect.Maps;
import org.springframework.data.util.CastUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/6/18 11:06 上午
 * @Version 1.0
 */

public class ElasticComputeProviderFactory {

    private ElasticComputeProviderFactory() {
    }

    //         instanceType & key
    static private Map<String, Map<String, IElasticComputeProvider>> context = new ConcurrentHashMap<>();


    public static <T extends IElasticComputeProvider> T getProvider(String instanceType, String key) {
        if (context.containsKey(instanceType))
            return CastUtils.cast(context.get(instanceType).get(key));
        return null;
    }

    public static <T extends IElasticComputeProvider> void register(T bean) {
        if (context.containsKey(bean.getInstanceType()))
            context.get(bean.getInstanceType()).put(bean.getKey(), bean);
        Map<String, IElasticComputeProvider> elasticComputeContext = Maps.newConcurrentMap();
        elasticComputeContext.put(bean.getKey(), bean);
        context.put(bean.getInstanceType(), elasticComputeContext);
    }
}
