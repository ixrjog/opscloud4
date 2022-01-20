package com.baiyi.opscloud.config;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.config.properties.WhiteConfigurationProperties;
import com.baiyi.opscloud.task.ScheduleJob;
import com.baiyi.opscloud.task.service.QuartzService;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 11:38 上午
 * @Version 1.0
 */
class WhiteConfigTest extends BaseUnit {

    @Resource
    private WhiteConfigurationProperties whiteConfig;

    @Resource
    private QuartzService quartzService;

    @Test
    void whiteConfigTest() {
        print(whiteConfig);
    }


    @Test
    void taskTest() throws SchedulerException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", 1);
//        quartzService.deleteJob("job", "test");
//        quartzService.pauseJob("job", "test");
//        quartzService.addJob(JobTest.class, "job", "test", "*/5 * * * * ?", map);
        List<ScheduleJob> list = quartzService.getAllJob();
        System.err.println(list);
//        while (true) {
//            System.err.println("------");
//            try {
//                Thread.sleep(1000 * 30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}