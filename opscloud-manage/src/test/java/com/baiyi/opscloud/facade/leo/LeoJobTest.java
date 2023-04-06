package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.service.leo.LeoJobService;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/2/1 16:01
 * @Version 1.0
 */
public class LeoJobTest extends BaseUnit {

    @Resource
    private LeoJobService leoJobService;

    @Test
    public void test() {
        List<LeoJob> leoJobs = leoJobService.queryAll();

        leoJobs.forEach(job->{
            String destStr =  job.getEnvType() == 4 ? "robot-leo-prod" : "robot-leo-non-prod";
            String config = job.getJobConfig().replace("robot-leo-test",destStr);
            LeoJob saveLeoJob = LeoJob.builder()
                    .id(job.getId())
                    .jobConfig(config)
                    .build();
            leoJobService.updateByPrimaryKeySelective(saveLeoJob);
        });

    }

}
