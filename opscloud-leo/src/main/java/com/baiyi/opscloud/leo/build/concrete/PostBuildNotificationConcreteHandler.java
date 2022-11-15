package com.baiyi.opscloud.leo.build.concrete;

import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.leo.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.build.LeoRobotHelper;
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
public class PostBuildNotificationConcreteHandler extends BaseBuildHandler {

    @Resource
    private MessageTemplateService megTemplateService;

    @Resource
    private LeoRobotHelper leoRobotHelper;

    @Resource
    private UserService userService;

    private static final String LEO_POST_BUILD = "LEO_POST_BUILD";

    /**
     * 构建后通知
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
                    .buildStatus("构建后通知阶段: 发送消息成功")
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            logHelper.info(leoBuild, "构建后通知成功: jobName={}", leoBuild.getBuildJobName());
        } catch (LeoBuildException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("构建后通知阶段: 发送消息失败")
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            // 忽略异常，只记录日志
            logHelper.warn(leoBuild, e.getMessage());
        }
    }

    private void sendMessage(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        String dingtalkRobot = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getNotify)
                .map(LeoBaseModel.Notify::getName)
                .orElseThrow(() -> new LeoBuildException("发送消息失败: DingtalkRobot未配置！"));

        DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
        MessageTemplate messageTemplate = megTemplateService.getByUniqueKey(LEO_POST_BUILD, "DINGTALK_ROBOT", "markdown");
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
