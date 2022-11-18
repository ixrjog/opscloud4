package com.baiyi.opscloud.leo.build.concrete.pre;

import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.leo.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.message.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/15 09:32
 * @Version 1.0
 */
@Slf4j
@Component
public class StartBuildNotificationConcreteHandler extends BaseBuildHandler {

    @Resource
    private MessageTemplateService msgTemplateService;

    @Resource
    private LeoRobotHelper leoRobotHelper;

    @Resource
    private UserService userService;

    private static final String LEO_START_BUILD = "LEO_START_BUILD";

    /**
     * 构建前通知
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
        String dingtalkRobot = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getNotify)
                .map(LeoBaseModel.Notify::getName)
                .orElseThrow(() -> new LeoBuildException("发送消息失败: DingtalkRobot未配置！"));

        DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
        MessageTemplate messageTemplate = msgTemplateService.getByUniqueKey(LEO_START_BUILD, "DINGTALK_ROBOT", "markdown");
        if (messageTemplate == null)
            throw new LeoBuildException("发送消息失败: 消息模板未配置！");

        User user = userService.getByUsername(leoBuild.getUsername());

        Map<String, Object> contentMap = Maps.newHashMap(buildConfig.getBuild().getDict());
        contentMap.put("buildPhase", "构建开始");
        contentMap.put("nowDate", TimeUtil.nowDate());
        contentMap.put("users", Lists.newArrayList(user));

        final String msg = renderTemplate(messageTemplate, contentMap);
        leoRobotHelper.send(dsInstance, msg);
    }

    private String renderTemplate(MessageTemplate messageTemplate, Map<String, Object> contentMap) {
        try {
            return BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);
        } catch (IOException e) {
            throw new LeoBuildException("渲染Dingtalk模板错误: err={}", e.getMessage());
        }
    }

}
