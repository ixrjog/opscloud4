package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.ServerChangeFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xujian@gegejia.com">修远</a>
 * @Date 2020/8/5 4:44 PM
 * @Since 1.0
 */

@Slf4j
@Component
public class ServerChangeTaskTask extends BaseTask {

    @Resource
    private ServerChangeFacade serverChangeFacade;

    private static final String SERVER_CHANGE_TASK_TASK_KEY = "SERVER_CHANGE_TASK_TASK_KEY";

    private static final int LOCK_MINUTE = 2;

    /**
     * 关闭超时的子任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void closeInvalidSessionTask() {
        if (tryLock()) return;
        serverChangeFacade.checkServerChangeTask();
        unlock();
    }

    @Override
    protected String getLock() {
        return SERVER_CHANGE_TASK_TASK_KEY;
    }

    @Override
    protected String getTaskName() {
        return "关闭超时的服务器变更任务";
    }

    @Override
    protected int getLockMinute() {
        return LOCK_MINUTE;
    }

}

