package com.baiyi.opscloud.configuration;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import com.baiyi.opscloud.util.SystemInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.net.UnknownHostException;

/**
 * @Author baiyi
 * @Date 2023/1/29 09:54
 * @Version 1.0
 */
@Slf4j
@Component
public class InitialInstanceSystemInfo {

    @Resource
    private InstanceFacade instanceFacade;

    @Resource
    private RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        try {
            Instance instance = instanceFacade.getInstance();
            if (instance == null) {
                return;
            }
            redisUtil.set(SystemInfoUtil.buildKey(instance), SystemInfoUtil.buildInfo(), NewTimeUtil.DAY_TIME / 1000 * 365);
            log.info("Initialize instance system information");
        } catch (UnknownHostException ignored) {
            log.warn("Error querying instance information");
        }
    }

}
