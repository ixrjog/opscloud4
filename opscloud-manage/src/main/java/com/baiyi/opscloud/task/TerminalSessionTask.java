package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.TerminalFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
    private TerminalFacade terminalFacade;


    /**
     * 关闭无效会话
     */
    @Scheduled(initialDelay = 5000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "closeInvalidSessionTask", lockAtMostFor = "2m", lockAtLeastFor = "2m")
    public void closeInvalidSessionTask() {
        log.info("任务启动: 关闭无效的终端会话！");
        terminalFacade.closeInvalidSessionTask();
    }


}
