package com.baiyi.opscloud.factory.resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 修远
 * @Date 2023/5/19 2:06 PM
 * @Since 1.0
 */

@Slf4j
public class ProjectResQueryFactory {

    private ProjectResQueryFactory() {
    }

    private static final Map<ImmutablePair<String, Integer>, IProjectResQuery> context = new ConcurrentHashMap<>();

    public static IProjectResQuery getProjectResQuery(String resType, Integer businessType) {
        ImmutablePair<String, Integer> key = ImmutablePair.of(resType, businessType);
        return context.get(key);
    }

    public static void register(IProjectResQuery bean) {
        ImmutablePair<String, Integer> key = ImmutablePair.of(bean.getProjectResType(), bean.getBusinessType());
        context.put(key, bean);
        log.debug("AppResQueryFactory Registered: beanName={}, resourceType={}, businessType={}",
                bean.getClass().getSimpleName(), bean.getProjectResType(), bean.getBusinessType());
    }
}
