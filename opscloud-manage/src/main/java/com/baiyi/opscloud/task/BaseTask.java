package com.baiyi.opscloud.task;

import com.baiyi.opscloud.config.OpscloudConfig;
import com.baiyi.opscloud.task.util.TaskUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/8 1:37 下午
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseTask {

    @Resource
    protected TaskUtil taskUtil;

    @Resource
    private OpscloudConfig opscloudConfig;

    protected boolean tryLock(int lockMinute) {
        if (!opscloudConfig.getOpenTask()) return false;
        if (taskUtil.tryLock(getLock())) return false;
        taskUtil.lock(getLock(), lockMinute);
        log.info(Joiner.on(": ").join(getTaskName(), "开始执行！"));
        return true;
    }

    protected void unlock() {
        taskUtil.unlock(getLock());
        log.info(Joiner.on(": ").join(getTaskName(), "执行结束！"));
    }

    abstract String getLock();

    abstract String getTaskName();
}
