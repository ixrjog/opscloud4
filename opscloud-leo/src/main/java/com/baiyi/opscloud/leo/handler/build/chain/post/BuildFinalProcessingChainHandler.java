package com.baiyi.opscloud.leo.handler.build.chain.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
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
 * 构建最终处理
 * @Author baiyi
 * @Date 2023/5/16 11:41
 * @Version 1.0
 */
@Slf4j
@Component
public class BuildFinalProcessingChainHandler extends BaseBuildChainHandler {

    @Resource
    private MeterSphereBuildEventPublisher meterSphereBuildEventPublisher;

    @Resource
    private UserService userService;

    @Resource
    private LeoJobService leoJobService;

    /**
     * 构建最终处理
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        sendMeterSphereEvent(leoBuild, buildConfig);
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
                    .envName(dict.getOrDefault(BuildDictConstants.ENV.getKey(), ""))
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