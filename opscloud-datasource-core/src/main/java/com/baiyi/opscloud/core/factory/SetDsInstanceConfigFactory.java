package com.baiyi.opscloud.core.factory;

import com.baiyi.opscloud.core.provider.base.common.AbstractSetDsInstanceConfigProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设置配置文件工厂
 *
 * @Author baiyi
 * @Date 2021/6/24 7:16 下午
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
public class SetDsInstanceConfigFactory {

    private SetDsInstanceConfigFactory() {
    }

    private static final Map<String, AbstractSetDsInstanceConfigProvider> CONTEXT = new ConcurrentHashMap<>();

    public static AbstractSetDsInstanceConfigProvider getProvider(String instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static void register(AbstractSetDsInstanceConfigProvider bean) {
        CONTEXT.put(bean.getInstanceType(), bean);
        log.debug("SetDsInstanceConfigFactory Registered: instanceType={}", bean.getInstanceType());
    }

}