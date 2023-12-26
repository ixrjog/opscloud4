package com.baiyi.opscloud.leo.handler.build.chain.pre;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.handler.build.BuildMessageCenter;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/15 09:32
 * @Version 1.0
 */
@Slf4j
@Component
public class StartBuildNotificationChainHandler extends BaseBuildChainHandler {

    private static final String LEO_BUILD_START = "LEO_BUILD_START";

    @Resource
    private BuildMessageCenter buildMessageCenter;

    /**
     * 构建开始通知
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        try {
            sendMessage(leoBuild, buildConfig);
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("启动构建通知阶段: 发送消息成功")
                    .build();
            save(saveLeoBuild, "启动构建通知成功: jobName={}", leoBuild.getBuildJobName());
        } catch (LeoBuildException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("启动构建通知阶段: 发送消息失败")
                    .build();
            save(saveLeoBuild, e.getMessage());
        }
    }

    private void sendMessage(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        Map<String, Object> contentMap = Maps.newHashMap();
        contentMap.put("buildPhase", "构建开始");
        buildMessageCenter.sendMessage(leoBuild, buildConfig, LEO_BUILD_START, contentMap);
    }

}