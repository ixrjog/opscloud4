package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.CronUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceScheduleParam;
import com.baiyi.opscloud.domain.vo.datasource.ScheduleVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceScheduleFacade;
import com.baiyi.opscloud.schedule.quartz.job.AssetPullJob;
import com.baiyi.opscloud.scheduler.QuartzService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/3/23 15:43
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DsInstanceScheduleFacadeImpl implements DsInstanceScheduleFacade {

    private final QuartzService quartzService;

    private final DsInstanceService instanceService;

    private static final Map<String, Class<? extends QuartzJobBean>> jobClassMap = ImmutableMap.<String, Class<? extends QuartzJobBean>>builder()
            .put("AssetPullJob", AssetPullJob.class)
            .build();

    @Override
    public List<ScheduleVO.Job> queryJob(int instanceId) {
        DatasourceInstance instance = instanceService.getById(instanceId);
        if (instance == null)
            throw new CommonRuntimeException("数据源实例不存在！");
        try {
            return quartzService.queryJob(instance.getUuid());
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            throw new CommonRuntimeException("查询数据源实例任务错误: " + e.getMessage());
        }
    }

    @Override
    public void addJob(DsInstanceScheduleParam.AddJob param) {
        if (!jobClassMap.containsKey(param.getJobType()))
            throw new CommonRuntimeException("任务类型不存在！");
        DatasourceInstance instance = instanceService.getById(param.getInstanceId());
        if (instance == null)
            throw new CommonRuntimeException("数据源实例不存在！");
        if (quartzService.checkJobExist(instance.getUuid(), param.getAssetType()))
            throw new CommonRuntimeException("任务已存在！");
        addJob(instance, param);
    }

    private void addJob(DatasourceInstance instance, DsInstanceScheduleParam.AddJob param) {
        Map<String, Object> map = ImmutableMap.<String, Object>builder()
                .put("assetType", param.getAssetType())
                .put("instanceId", instance.getId())
                .build();
        quartzService.addJob(
                jobClassMap.get(param.getJobType()),
                instance.getUuid(),
                param.getAssetType(),
                param.getJobTime(),
                param.getJobDescription(),
                map);
    }

    @Override
    public void pauseJob(DsInstanceScheduleParam.UpdateJob param) {
        quartzService.pauseJob(param.getGroup(), param.getName());
    }

    @Override
    public void resumeJob(DsInstanceScheduleParam.UpdateJob param) {
        quartzService.resumeJob(param.getGroup(), param.getName());
    }

    @Override
    public void deleteJob(DsInstanceScheduleParam.UpdateJob param) {
        quartzService.deleteJob(param.getGroup(), param.getName());
    }

    @Override
    public List<String> checkCron(DsInstanceScheduleParam.CheckCron param) {
        return CronUtil.recentTime(param.getJobTime(), 5);
    }

}
