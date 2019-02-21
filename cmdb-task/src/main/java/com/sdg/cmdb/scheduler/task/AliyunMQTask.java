package com.sdg.cmdb.scheduler.task;


import com.sdg.cmdb.service.AliyunMQService;
import com.sdg.cmdb.util.schedule.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;

@Component
@Slf4j
public class AliyunMQTask implements BaseJob {

    @Resource
    private AliyunMQService aliyunMQService;

    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    @Override
    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() {
        if (!StringUtils.isEmpty(taskOpen) && taskOpen.equalsIgnoreCase("true"))
            aliyunMQService.task();
    }
}
