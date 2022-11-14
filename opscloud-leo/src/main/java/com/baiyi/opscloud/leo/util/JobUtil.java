package com.baiyi.opscloud.leo.util;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import static java.util.Optional.ofNullable;

/**
 * @Author baiyi
 * @Date 2022/11/9 10:33
 * @Version 1.0
 */
public class JobUtil {

    private JobUtil() {
    }

    public static String generateJobName(Application application, LeoJob leoJob) {
        return Joiner.on("_").join(application.getApplicationKey(), leoJob.getJobKey());
    }

    public static String generateBuildJobName(Application application, LeoJob leoJob, int buildNumber) {
        return generateBuildJobName(application.getApplicationKey(), leoJob.getJobKey(), buildNumber);
    }

    public static String generateBuildJobName(String applicationKey, String jobKey, int buildNumber) {
        return Joiner.on("_").join(applicationKey, jobKey, buildNumber);
    }

    public static String generateVersionName(LeoBuildParam.DoBuild doBuild, LeoJobModel.JobConfig jobConfig) {
        // 用户输入版本号, 优先级最高
        // ${prefix} + ${application} + ${env} + ${buildNumber} + ${suffix}
        if (StringUtils.isNotBlank(doBuild.getVersionName()))
            return doBuild.getVersionName();

        return ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getBuild)
                .map(LeoJobModel.Build::getVersion)
                .map(LeoJobModel.Version::getPrefix)
                .orElse("");
    }

}
