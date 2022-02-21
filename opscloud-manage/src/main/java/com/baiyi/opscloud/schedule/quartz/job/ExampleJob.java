package com.baiyi.opscloud.schedule.quartz.job;

import com.baiyi.opscloud.common.util.InstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.Instant;

/**
 * @Author 修远
 * @Date 2022/1/18 3:00 PM
 * @Since 1.0
 */

@Slf4j
public class ExampleJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取参数
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        // 业务逻辑 ...
      //  log.error("---task执行" + jobDataMap.get("name").toString() + "######" + jobExecutionContext.getTrigger());
        //任务开始时间
        Instant instant = Instant.now();
        try {
            log.info("11111 instanceId = {}", jobDataMap.get("instanceId"));
            //任务执行总时长
            log.error("任务执行完毕，任务ID：" + jobExecutionContext.getJobDetail() + "  总共耗时：" + InstantUtil.timerSeconds(instant) + "毫秒");
        } catch (Exception e) {
            log.error("任务执行失败，任务ID：" + jobExecutionContext.getJobDetail() + "  总共耗时：" + InstantUtil.timerSeconds(instant) + "毫秒", e);

        }
    }
}
