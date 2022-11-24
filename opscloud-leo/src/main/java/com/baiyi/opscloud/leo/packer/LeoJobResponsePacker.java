package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/11/23 18:51
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoJobResponsePacker {

    @Resource
    private LeoBuildService leoBuildService;

    @Resource
    private ApplicationService applicationService;


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
    }


}
