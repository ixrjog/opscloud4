package com.baiyi.opscloud.leo.handler.build.strategy.build;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.handler.build.strategy.build.base.BaseDoBuildStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/6/21 15:05
 * @Version 1.0
 */
@Slf4j
@Component
public class DoBuildWithFrontEndStrategy extends BaseDoBuildStrategy {

    @Override
    protected void handlePostProcessDict(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob, Map<String, String> dict) {
    }

    @Override
    public String getBuildType() {
        return BuildTypeConstants.FRONT_END;
    }

}