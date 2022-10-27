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
@Slf4j
public class SetDsInstanceConfigFactory {

    private SetDsInstanceConfigFactory() {
    }

    private static final Map<String, AbstractSetDsInstanceConfigProvider> context = new ConcurrentHashMap<>();

    public static AbstractSetDsInstanceConfigProvider getProvider(String instanceType) {
        return context.get(instanceType);
    }

    public static void register(AbstractSetDsInstanceConfigProvider bean) {
        context.put(bean.getInstanceType(), bean);
        log.info("SetDsInstanceConfigFactory Registered: instanceType={}", bean.getInstanceType());
    }

}
