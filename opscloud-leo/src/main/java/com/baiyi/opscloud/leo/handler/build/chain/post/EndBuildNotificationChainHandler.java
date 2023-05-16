package com.baiyi.opscloud.leo.handler.build.chain.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.handler.build.chain.post.event.MeterSphereBuildEventPublisher;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/17 10:34
 * @Version 1.0
 */
@Slf4j
@Component
public class EndBuildNotificationChainHandler extends BaseBuildChainHandler {

    private static final String LEO_BUILD_END = "LEO_BUILD_END";

    @Resource
    private MeterSphereBuildEventPublisher meterSphereBuildEventPublisher;

    @Resource
    private UserService userService;

    @Resource
    private LeoJobService leoJobService;

    /**
     * 构建结束通知
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
                    .buildStatus("结束构建通知阶段: 发送消息成功")
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            logHelper.info(leoBuild, "结束构建通知成功: jobName={}", leoBuild.getBuildJobName());
            // ms消息
            sendMeterSphereEvent(leoBuild, buildConfig);
        } catch (LeoBuildException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("结束构建通知阶段: 发送消息失败")
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            // 忽略异常，只记录日志
            logHelper.warn(leoBuild, e.getMessage());
        }
    }

    private void sendMessage(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        Map<String, Object> contentMap = Maps.newHashMap();
        contentMap.put("buildPhase", "构建结束");
        contentMap.put("buildResult", leoBuild.getBuildResult());
        sendMessage(leoBuild, buildConfig, LEO_BUILD_END, contentMap);
    }

    private void sendMeterSphereEvent(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        if (!leoBuild.getIsActive()) {
            return;
        }
        try {
            Map<String, String> dict = Optional.ofNullable(buildConfig)
                    .map(LeoBuildModel.BuildConfig::getBuild)
                    .map(LeoBuildModel.Build::getDict)
                    .orElse(Maps.newHashMap());

            User dbUser = userService.getByUsername(leoBuild.getUsername());
            LeoHook.User user = LeoHook.User.builder()
                    .username(leoBuild.getUsername())
                    .email(dbUser != null ? dbUser.getEmail() : "")
                    .displayName(dict.getOrDefault(BuildDictConstants.DISPLAY_NAME.getKey(), ""))
                    .build();

            LeoHook.Commit commit = LeoHook.Commit.builder()
                    .id(dict.getOrDefault(BuildDictConstants.COMMIT_ID.getKey(), ""))
                    .build();

            LeoHook.GitLab gitLab = LeoHook.GitLab.builder()

                    .sshUrl(dict.getOrDefault(BuildDictConstants.SSH_URL.getKey(), ""))
                    .branch(dict.getOrDefault(BuildDictConstants.BRANCH.getKey(), ""))
                    .commit(commit)
                    .build();

            LeoJob leoJob = leoJobService.getById(leoBuild.getJobId());

            LeoHook.BuildHook hook = LeoHook.BuildHook.builder()
                    .id(leoBuild.getId())
                    .name(leoBuild.getJobName())
                    .buildType(leoJob.getBuildType())
                    .appName(dict.getOrDefault(BuildDictConstants.APPLICATION_NAME.getKey(), ""))
                    .gitLab(gitLab)
                    .user(user)
                    .projectId(-1)
                    .gmtModified(leoBuild.getEndTime())
                    .build();

            meterSphereBuildEventPublisher.publish(hook);
        } catch (Exception ignored) {
        }
    }

}