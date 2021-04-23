package com.baiyi.opscloud.facade;

/**
 * @Author baiyi
 * @Date 2020/10/9 3:02 下午
 * @Version 1.0
 */
public interface DubboMappingFacade {

    /**
     * 对外接口刷新直连配置
     * @param env
     */
    void refreshDubboProviderByEnv(String env);

    void syncDubboProviderByEnv(String env);

    void bindDubboMappingPortByEnv(String env);

    void writeDubboResolvePropertiesByEnv(String env);

    String buildDubboResolveByEnv(String env);
}
