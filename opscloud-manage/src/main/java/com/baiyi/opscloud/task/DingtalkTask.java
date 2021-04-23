package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.dingtalk.DingtalkDeptFacade;
import com.baiyi.opscloud.facade.dingtalk.DingtalkUserFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/24 11:15 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class DingtalkTask {

    @Resource
    private DingtalkDeptFacade dingtalkDeptFacade;

    @Resource
    private DingtalkUserFacade dingtalkUserFacade;

    public static final String DINGTALK_UID = "xincheng";

    /**
     * 钉钉部门同步任务
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @SchedulerLock(name = "dingtalkDeptSyncTask", lockAtMostFor = "30m", lockAtLeastFor = "2m")
    public void dingtalkDeptSyncTask() {
        log.info("任务启动: 钉钉部门同步任务！");
        dingtalkDeptFacade.syncDept(DINGTALK_UID);
    }

    /**
     * 钉钉用户同步任务
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @SchedulerLock(name = "dingtalkUserSyncTask", lockAtMostFor = "30m", lockAtLeastFor = "2m")
    public void dingtalkUserSyncTask() {
        log.info("任务启动: 钉钉用户同步任务！");
        dingtalkUserFacade.syncUser();
    }


}
