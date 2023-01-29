package com.baiyi.opscloud.schedule.quartz.example;

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
        //任务开始时间
        Instant instant = Instant.now();
        try {
            log.info("instanceId={}", jobDataMap.get("instanceId"));
            //任务执行总时长
            log.error("任务执行完毕: 任务ID={}, 总共耗时={}毫秒", jobExecutionContext.getJobDetail(), InstantUtil.timerSeconds(instant));
        } catch (Exception e) {
            log.error("任务执行失败，任务ID={}, 耗时={}毫秒", jobExecutionContext.getJobDetail(), InstantUtil.timerSeconds(instant), e);
        }
    }

}
