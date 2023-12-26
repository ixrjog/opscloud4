package com.baiyi.opscloud.leo.util;

import com.baiyi.opscloud.common.util.TemplateUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

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

    public static String generateVersionName(LeoBuildParam.DoBuild doBuild, LeoJobModel.JobConfig jobConfig, LeoBaseModel.GitLab gitLab, Application application, Env env, int buildNumber) {
        // 用户输入版本号, 优先级最高
        if (StringUtils.isNotBlank(doBuild.getVersionName())) {
            return doBuild.getVersionName();
        }
        // ${version.prefix}-${project}-${version.suffix}
        // 中间注入变量 ${env} + ${buildNumber}
        final String prefix = ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getBuild)
                .map(LeoJobModel.Build::getVersion)
                .map(LeoJobModel.Version::getPrefix)
                .orElse(null);

        final String suffix = ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getBuild)
                .map(LeoJobModel.Build::getVersion)
                .map(LeoJobModel.Version::getSuffix)
                .orElse(null);

        final String project = ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getCr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getName)
                .orElse(application.getName());


        if (StringUtils.isNotBlank(suffix)) {
            Map<String, String> dict = Maps.newHashMap();
            dict.put(BuildDictConstants.BUILD_NUMBER.getKey(), String.valueOf(buildNumber));
            dict.put(BuildDictConstants.ENV.getKey(), env.getEnvName());
            dict.put(BuildDictConstants.COMMIT_ID.getKey(), gitLab.getProject().getCommit().getId().substring(0, 8));
            return Joiner.on("-").skipNulls().join(prefix, project, TemplateUtil.render(suffix, dict));
        }
        return Joiner.on("-").skipNulls().join(prefix, project, buildNumber).replaceAll("-{2,}", "-");
    }

}