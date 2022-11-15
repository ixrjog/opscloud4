package com.baiyi.opscloud.leo.build.concrete;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/15 09:32
 * @Version 1.0
 */
@Slf4j
@Component
public class PostBuildNotificationConcreteHandler extends BaseBuildHandler {

    /**
     * 构建后通知
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
    }

}
