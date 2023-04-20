package com.baiyi.opscloud.leo.handler.build.strategy.verification.base;

import com.baiyi.opscloud.leo.constants.BuildStepConstants;
import com.baiyi.opscloud.leo.handler.build.base.BaseBuildStrategy;

/**
 * @Author baiyi
 * @Date 2023/4/20 14:42
 * @Version 1.0
 */
public abstract class BasePostBuildVerificationStrategy extends BaseBuildStrategy {

    @Override
    public String getStep() {
        return BuildStepConstants.POST_BUILD_VERIFICATION.name();
    }

}