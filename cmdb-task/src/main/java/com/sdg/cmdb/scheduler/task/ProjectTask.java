package com.sdg.cmdb.scheduler.task;


import com.sdg.cmdb.service.CacheKeyService;
import com.sdg.cmdb.service.ProjectService;
import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectTask  implements BaseJob, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ProjectTask.class);

    @Value("#{cmdb['task.open']}")
    private String taskOpen;
    

    /**
     * 10:00 执行任务 (发送项目管理邮件通知)
     */
    private static final String taskCorn = "0 0 10 * * ?";

    @Resource
    private ProjectService projectService;

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private CacheKeyService cacheKeyService;
    /**
     * 数据同步
     */
    @Override
    public void execute() {
        if (taskOpen.equalsIgnoreCase("true")) {

            if (cacheKeyService.checkRunning(CacheKeyService.PROJECT_TASK_LOCK_KEY)) {
                logger.info("Project : task is running");
                return ;
            }
            cacheKeyService.setRunning(CacheKeyService.PROJECT_TASK_LOCK_KEY,true);
            logger.info("Project : task start");
            projectService.task();
            cacheKeyService.setRunning(CacheKeyService.PROJECT_TASK_LOCK_KEY,false);
            logger.info("Project : end task");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, taskCorn, this.getClass().getSimpleName());
    }

}
