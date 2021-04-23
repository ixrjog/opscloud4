package com.baiyi.opscloud.dubbo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.DubboMappingFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/9 3:03 下午
 * @Version 1.0
 */
public class DubboMappingTest extends BaseUnit {

    @Resource
    private DubboMappingFacade dubboMappingFacade;

    @Test
    void testSyncDubboProviderByEnv() {
        dubboMappingFacade.syncDubboProviderByEnv("daily");
    }

    @Test
    void testBindDubboMappingPortByEnv() {
        dubboMappingFacade.bindDubboMappingPortByEnv("daily");
    }

    @Test
    void testBuildDubboResolveByEnv() {
        String body = dubboMappingFacade.buildDubboResolveByEnv("dev");
        System.err.println(body);
    }

    @Test
    void testWriteDubboResolvePropertiesByEnv() {
        dubboMappingFacade.writeDubboResolvePropertiesByEnv("dev");
    }

    @Test
    void testRefreshDubboProviderByEnv() {
        dubboMappingFacade.refreshDubboProviderByEnv("daily");
    }
}
