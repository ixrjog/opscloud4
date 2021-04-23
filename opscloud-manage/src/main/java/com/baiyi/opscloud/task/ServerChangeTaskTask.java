package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.ServerChangeFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
public class ServerChangeTaskTask  {

    @Resource
    private ServerChangeFacade serverChangeFacade;

    /**
     * 关闭超时的子任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @SchedulerLock(name = "closeInvalidSessionTask", lockAtMostFor = "2m", lockAtLeastFor = "2m")
    public void closeInvalidSessionTask() {
        log.info("任务启动: 关闭超时的服务器变更任务！");
        serverChangeFacade.checkServerChangeTask();
    }

}

