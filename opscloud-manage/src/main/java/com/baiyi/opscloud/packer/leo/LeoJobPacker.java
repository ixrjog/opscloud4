package com.baiyi.opscloud.packer.leo;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/4 15:17
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoJobPacker implements IWrapper<LeoJobVO.Job> {

    private final ApplicationService applicationService;

    private final LeoBuildService buildService;

    private final LeoDeployService deployService;

    private final LeoTemplateService leoTemplateService;

    private final LeoTemplatePacker leoTemplatePacker;

    private static final String DISPLAY_VERSION = "T{}/{}";

    @Override
    @TagsWrapper
    @EnvWrapper
    public void wrap(LeoJobVO.Job job, IExtend iExtend) {
        if (iExtend.getExtend()) {
            Application application = applicationService.getById(job.getApplicationId());
            if (application != null) {
                job.setApplication(BeanCopierUtil.copyProperties(application, ApplicationVO.Application.class));
            }
            // 注入模板:版本号
            LeoTemplate leoTemplate = leoTemplateService.getById(job.getTemplateId());
            if (leoTemplate != null) {
                LeoTemplateVO.Template template = BeanCopierUtil.copyProperties(leoTemplate, LeoTemplateVO.Template.class);
                leoTemplatePacker.wrap(template, SimpleExtend.NOT_EXTEND);
                job.setTemplate(template);
                // 校验版本号
                LeoJobVO.VerifyTemplateVersion verifyTemplateVersion;
                if (template.getVersion().equals(job.getTemplateVersion())) {
                    verifyTemplateVersion = LeoJobVO.VerifyTemplateVersion.builder()
                            .displayVersion(template.getVersion())
                            .build();
                } else {
                    verifyTemplateVersion = LeoJobVO.VerifyTemplateVersion.builder()
                            .type("warning")
                            .isIdentical(false)
                            .displayVersion(StringFormatter.arrayFormat(DISPLAY_VERSION, template.getVersion(), job.getTemplateVersion()))
                            .build();
                }
                job.setVerifyTemplateVersion(verifyTemplateVersion);
            }
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(job.getJobConfig());
            job.setConfigDetails(jobConfig);
            job.setBuildSize(buildService.countWithJobId(job.getId()));
            job.setDeploySize(deployService.countWithJobId(job.getId()));
        }
    }

}
