package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.TerminalFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/26 6:01 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TerminalSessionTask extends BaseTask {

    @Resource
    private TerminalFacade terminalFacade;

    // Terminal
    public static final String TASK_TERMINAL_SESSION_KEY = "TASK_TERMINAL_SESSION_KEY";

    private static final int  LOCK_MINUTE = 2;

    /**
     * 关闭无效会话
     */
    @Scheduled(initialDelay = 5000, fixedRate = 60 * 1000)
    public void closeInvalidSessionTask() {
        if (tryLock()) return;
        terminalFacade.closeInvalidSessionTask();
        unlock();
    }

    @Override
    protected String getLock() {
        return TASK_TERMINAL_SESSION_KEY;
    }

    @Override
    protected String getTaskName() {
        return "WebXTerm会话关闭任务";
    }

    @Override
    protected int getLockMinute() {
        return LOCK_MINUTE;
    }

}
