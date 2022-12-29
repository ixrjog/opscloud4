package com.baiyi.opscloud.leo.action.deploy.strategy.boot;

import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.action.build.helper.ApplicationTagsHelper;
import com.baiyi.opscloud.leo.action.deploy.strategy.boot.base.BootDeployStrategy;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/15 19:59
 * @Version 1.0
 */
@Slf4j
@Component
public class BootDeployWithRedeployStrategy extends BootDeployStrategy {

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationTagsHelper applicationTagsHelper;

    @Resource
    private EnvService envService;

    @Override
    protected void preHandle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        LeoJob leoJob = leoJobService.getById(leoDeploy.getJobId());
        Application application = applicationService.getById(leoJob.getApplicationId());
        final String tags = applicationTagsHelper.getTagsStr(leoJob.getApplicationId());

        Env env = envService.getByEnvType(leoJob.getEnvType());

        Map<String, String> dict = SimpleDictBuilder.newBuilder()
                .putParam(BuildDictConstants.APPLICATION_NAME.getKey(), application.getApplicationKey())
                .putParam(BuildDictConstants.JOB_NAME.getKey(), leoJob.getJobKey())
                .putParam(BuildDictConstants.APPLICATION_TAGS.getKey(), tags)
                .putParam(BuildDictConstants.VERSION_NAME.getKey(),"-")
                .putParam(BuildDictConstants.ENV.getKey(), env.getEnvName())
                .putParam(BuildDictConstants.IMAGE.getKey(), "-")
                .build().getDict();
        deployConfig.getDeploy().setDict(dict);
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.REDEPLOY.name();
    }

}
