package com.baiyi.opscloud.leo.action.build;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.leo.action.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.helper.BuildingLogHelper;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/14 17:06
 * @Version 1.0
 */
public abstract class BaseBuildHandler {

    public static final String RESULT_ERROR = "ERROR";

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected BuildingLogHelper logHelper;

    @Resource
    protected LeoBuildService leoBuildService;

    @Resource
    private MessageTemplateService msgTemplateService;

    @Resource
    private LeoRobotHelper leoRobotHelper;

    @Resource
    private UserService userService;

    private BaseBuildHandler next;

    protected JenkinsConfig getJenkinsConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(uuid);
        return dsConfigHelper.build(dsConfig, JenkinsConfig.class);
    }

    public BaseBuildHandler setNextHandler(BaseBuildHandler next) {
        this.next = next;
        return this.next;
    }

    public BaseBuildHandler getNext() {
        return next;
    }

    public void handleRequest(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        try {
            this.handle(leoBuild, buildConfig);
        } catch (LeoBuildException e) {
            // 记录日志
            logHelper.error(leoBuild, e.getMessage());
            leoBuild.setBuildResult(RESULT_ERROR);
            leoBuild.setEndTime(new Date());
            leoBuild.setIsFinish(true);
            leoBuild.setBuildStatus(e.getMessage());
            leoBuild.setIsActive(false);
            save(leoBuild);
            throw e;
        }
        if (getNext() != null) {
            getNext().handleRequest(leoBuildService.getById(leoBuild.getId()), buildConfig);
        }
    }

    /**
     * 抽象方法，具体实现
     *
     * @param leoBuild
     * @param buildConfig
     */
    protected abstract void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig);

    protected void save(LeoBuild saveLeoBuild) {
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
    }

    protected void save(LeoBuild saveLeoBuild, String log, Object... var2) {
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
        logHelper.info(saveLeoBuild, log, var2);
    }

    protected void sendMessage(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, String messageKey, Map<String, Object> contentMap) {
        String dingtalkRobot = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getNotify)
                .map(LeoBaseModel.Notify::getName)
                .orElseThrow(() -> new LeoBuildException("发送消息失败: DingtalkRobot未配置！"));

        DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
        MessageTemplate messageTemplate = msgTemplateService.getByUniqueKey(messageKey, "DINGTALK_ROBOT", "markdown");
        if (messageTemplate == null) {
            throw new LeoBuildException("发送消息失败: 消息模板未配置！");
        }
        User user = userService.getByUsername(leoBuild.getUsername());

        contentMap.putAll(buildConfig.getBuild().getDict());
        contentMap.put("nowDate", NewTimeUtil.nowDate());
        contentMap.put("users", Lists.newArrayList(user));
        final String msg = renderTemplate(messageTemplate, contentMap);
        leoRobotHelper.send(dsInstance, msg);
    }

    protected String renderTemplate(MessageTemplate messageTemplate, Map<String, Object> contentMap) {
        try {
            return BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);
        } catch (IOException e) {
            throw new LeoBuildException("渲染Dingtalk模板错误: {}", e.getMessage());
        }
    }

}
