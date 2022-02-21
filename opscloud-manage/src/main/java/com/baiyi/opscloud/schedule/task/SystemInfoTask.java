package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import com.baiyi.opscloud.schedule.task.base.AbstractTask;
import com.baiyi.opscloud.util.SystemInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.UnknownHostException;

/**
 * @Author baiyi
 * @Date 2021/9/6 2:27 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class SystemInfoTask extends AbstractTask {

    @Resource
    private InstanceFacade instanceFacade;

    // key = "system_info_instance_ip_

    @InstanceHealth // 实例健康检查，高优先级
    @Scheduled(initialDelay = 10000, fixedRate = 30 * 1000)
    public void refreshSystemInfoTask() {
        try {
            Instance instance = instanceFacade.getInstance();
            if (instance == null) return;
            caching(SystemInfoUtil.buildKey(instance));
        } catch (UnknownHostException ignored) {
            log.error("查询实例信息错误！");
        }
    }

    private void caching(String key){
        redisUtil.set(key, SystemInfoUtil.buildInfo(), TimeUtil.dayTime / 1000);
    }

}
