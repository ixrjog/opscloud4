package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;

/**
 * @Author baiyi
 * @Date 2020/4/10 10:19 上午
 * @Version 1.0
 */
public interface ServerCacheFacade {

    void evictServerCache(OcServer ocServer);

    void evictServerGroupCache(OcServerGroup ocServerGroup);
}
