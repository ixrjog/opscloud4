package com.baiyi.opscloud.factory.resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/9/8 3:23 下午
 * @Version 1.0
 */
@Slf4j
public class ApplicationResourceQueryFactory {

    private ApplicationResourceQueryFactory() {
    }

    private static final Map<ImmutablePair<String, Integer>, IApplicationResourceQuery> context = new ConcurrentHashMap<>();

    public static IApplicationResourceQuery getIApplicationResourceQuery(String resType, Integer businessType) {
        ImmutablePair<String, Integer> key = ImmutablePair.of(resType, businessType);
        return context.get(key);
    }

    public static void register(IApplicationResourceQuery bean) {
        ImmutablePair<String, Integer> key = ImmutablePair.of(bean.getApplicationResType(), bean.getBusinessType());
        context.put(key, bean);
        log.info("ApplicationResourceQueryFactory Registered: beanName={}, resourceType={}, businessType={}",
                bean.getClass().getSimpleName(), bean.getApplicationResType(), bean.getBusinessType());
    }

}
