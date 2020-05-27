package com.baiyi.opscloud.task;

import com.baiyi.opscloud.config.OpscloudConfig;
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
public class TerminalSessionTask {

    @Resource
    private OpscloudConfig opscloudConfig;

    @Resource
    private TaskUtil taskUtil;

    @Resource
    private TerminalFacade terminalFacade;

    // Terminal
    public static final String TASK_TERMINAL_SESSION_KEY = "TASK_TERMINAL_SESSION_KEY";


    /**
     * 关闭无效会话
     */
    @Scheduled(cron = "* */1 * * * ?")
    public void closeInvalidSession() {
        if (!opscloudConfig.getOpenTask()) return;
        if (taskUtil.isTaskLock(TASK_TERMINAL_SESSION_KEY)) return;
        log.info("任务: closeInvalidSession 开始执行!");
        terminalFacade.closeInvalidSession();
        log.info("任务: closeInvalidSession 执行完成!");
    }
}
