package com.baiyi.opscloud.quartz;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.util.CronUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.vo.datasource.ScheduleVO;
import com.baiyi.opscloud.schedule.quartz.job.AssetPullJob;
import com.baiyi.opscloud.scheduler.QuartzService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.scheduling.support.CronExpression;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/2/21 10:14 AM
 * @Version 1.0
 */
public class QuartzTaskTest extends BaseUnit {

    @Resource
    private QuartzService quartzService;

    @Resource
    private DsInstanceService dsInstanceService;

    @Test
    void addDingtalkUserTask() {
        dsInstanceService.listByInstanceType(DsTypeEnum.DINGTALK_APP.getName()).forEach(dsInstance -> {
            Map<String, Object> map = ImmutableMap.<String, Object>builder()
                    .put("assetType", DsAssetTypeConstants.DINGTALK_USER.name())
                    .put("instanceId", dsInstance.getId())
                    .build();
            quartzService.addJob(
                    AssetPullJob.class,
                    dsInstance.getUuid(),
                    DsAssetTypeConstants.DINGTALK_USER.name(),
                    "0 0 10,11,19 * * ?",
                    "同步钉钉用户（每天10、11、19点开始任务）",
                    map);
        });
    }

    @Test
    void addDingtalkDeptTask() {
        dsInstanceService.listByInstanceType(DsTypeEnum.DINGTALK_APP.getName()).forEach(dsInstance -> {
            Map<String, Object> map = ImmutableMap.<String, Object>builder()
                    .put("assetType", DsAssetTypeConstants.DINGTALK_DEPARTMENT.name())
                    .put("instanceId", dsInstance.getId())
                    .build();
            quartzService.addJob(
                    AssetPullJob.class,
                    dsInstance.getUuid(),
                    DsAssetTypeConstants.DINGTALK_DEPARTMENT.name(),
                    "0 0 9,18 * * ?",
                    "同步钉钉组织架构（每天9、18点开始任务）",
                    map);
        });
    }

    @Test
    void queryJobTest() throws SchedulerException {
        String group = "e9f2acfe1d2945dd91262ba49df26984";
        List<ScheduleVO.Job> scheduleJobs = quartzService.queryJob(group);
        print(scheduleJobs);
    }

    @Test
    void aaaTest() {
        // 0 0 10,11,12,13,19 * * ?
        final CronExpression cronExpression = CronExpression.parse("0 0 10,11,12,13,19 * * ?");
        //  final LocalDateTime dateTime = cronExpression.next(LocalDateTime.now());
        LocalDateTime dateTime = LocalDateTime.now();
        for (int i = 1; i <= 5; i++) {
            dateTime = cronExpression.next(dateTime);
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String strDate2 = dtf2.format(dateTime);
            print(strDate2);
        }
    }

    @Test
    void aaa2Test() {
        print(CronUtil.recentTime("0 0 10,11,12,13,19 * * ?", 5));
    }

    @Test
    void taskTest() throws SchedulerException {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("assetType", "KUBERNETES_NAMESPACE");
        map.put("instanceId", 10);
//        quartzService.deleteJob("job", "test");
//        quartzService.pauseJob("job", "test");
//        quartzService.addJob(ExampleJob.class, "test", "JOB_1", "0/10 * * * * ?","测试任务"， map);
        List<ScheduleVO.Job> list = quartzService.getAllJob();
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
        quartzService.addJob(AssetPullJob.class, "Asset", "ASSET_KUBERNETES_NAMESPACE_1", "*/1 * * * * ?", "测试任务", map);
        List<ScheduleVO.Job> list = quartzService.getAllJob();
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
