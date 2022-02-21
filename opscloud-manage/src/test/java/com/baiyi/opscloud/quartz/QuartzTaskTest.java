package com.baiyi.opscloud.quartz;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.schedule.quartz.ScheduleJob;
import com.baiyi.opscloud.schedule.quartz.job.AssetPullJob;
import com.baiyi.opscloud.schedule.quartz.job.ExampleJob;
import com.baiyi.opscloud.schedule.quartz.service.QuartzService;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/21 10:14 AM
 * @Version 1.0
 */
public class QuartzTaskTest extends BaseUnit {

    @Resource
    private QuartzService quartzService;

    @Test
    void taskTest() throws SchedulerException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("assetType", "KUBERNETES_NAMESPACE");
        map.put("instanceId", 10);
//        quartzService.deleteJob("job", "test");
//        quartzService.pauseJob("job", "test");
        quartzService.addJob(ExampleJob.class, "test", "JOB_1", "*/1 * * * * ?", map);
        List<ScheduleJob> list = quartzService.getAllJob();
        print(list);
        while (true) {
            System.err.println("------");
            try {
                Thread.sleep(1000 * 30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void deleteJob1Test() {
        quartzService.pauseJob("test", "JOB_1");
        quartzService.deleteJob("test", "JOB_1");
    }


    @Test
    void task2Test() throws SchedulerException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("assetType", "KUBERNETES_NAMESPACE");
        map.put("instanceId", 10);
        quartzService.addJob(AssetPullJob.class, "Asset", "ASSET_KUBERNETES_NAMESPACE_1", "*/1 * * * * ?", map);
        List<ScheduleJob> list = quartzService.getAllJob();
        print(list);
        while (true) {
            System.err.println("------");
            try {
                Thread.sleep(1000 * 30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void deleteJobTest() {
        quartzService.pauseJob("Asset", "ASSET_KUBERNETES_NAMESPACE_1");
        quartzService.deleteJob("Asset", "ASSET_KUBERNETES_NAMESPACE_1");
    }

}
