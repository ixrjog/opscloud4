package com.baiyi.opscloud.leo.util;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2022/11/9 10:33
 * @Version 1.0
 */
public class JobNameUtil {

    private JobNameUtil() {
    }

    public static String generateBuildJobName(Application application, LeoJob leoJob, int buildNumber) {
        return generateBuildJobName(application.getName(), leoJob.getName(), buildNumber);
    }

    public static String generateBuildJobName(String applicationName, String jobKey, int buildNumber) {
        return Joiner.on("_").join(applicationName, jobKey, buildNumber);
    }

}
