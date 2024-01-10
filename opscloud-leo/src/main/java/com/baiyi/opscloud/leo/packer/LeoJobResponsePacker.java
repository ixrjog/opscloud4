package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/23 18:51
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoJobResponsePacker {

    private final LeoBuildService leoBuildService;

    private final ApplicationService applicationService;

    @TagsWrapper(extend = true)
    @EnvWrapper(extend = true)
    public void wrap(LeoJobVO.Job job) {
        Application application = applicationService.getById(job.getApplicationId());
        if (application != null) {
            job.setApplication(BeanCopierUtil.copyProperties(application, ApplicationVO.Application.class));
        }
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(job.getJobConfig());
        job.setConfigDetails(jobConfig);
        job.setBuildSize(leoBuildService.countWithJobId(job.getId()));

        // 最后构建信息
        List<LeoBuildVO.LatestBuildInfo> latestBuilds = leoBuildService.queryLatestBuildWithJobId(job.getId(), 3).stream().map(lb ->
                LeoBuildVO.LatestBuildInfo.builder()
                        .buildId(lb.getId())
                        .buildNumber(lb.getBuildNumber())
                        .running(StringUtils.isBlank(lb.getBuildResult()) || !lb.getIsFinish())
                        .color(toLatestBuildInfoColor(lb.getIsFinish(), lb.getBuildResult()))
                        .build()
        ).collect(Collectors.toList());
        job.setLatestBuildInfos(latestBuilds);
    }

    public interface Colors {
        String SUCCESS = "#17BA14";
        String FAILURE = "#DD3E03";
        String RUNNING = "#E07D06";
    }

    public static String toLatestBuildInfoColor(Boolean isFinish, String buildResult) {
        if (!isFinish) {
            return Colors.RUNNING;
        } else {
            if ("SUCCESS".equals(buildResult)) {
                return Colors.SUCCESS;
            } else {
                return Colors.FAILURE;
            }
        }
    }

}