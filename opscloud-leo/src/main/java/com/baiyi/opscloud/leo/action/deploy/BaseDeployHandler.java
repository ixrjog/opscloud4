package com.baiyi.opscloud.leo.action.deploy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.leo.action.build.helper.LeoRobotHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.helper.DeployingLogHelper;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.message.MessageTemplateService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/5 19:55
 * @Version 1.0
 */
public abstract class BaseDeployHandler {

    public static final String RESULT_ERROR = "ERROR";

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected DeployingLogHelper logHelper;

    @Resource
    protected LeoDeployService leoDeployService;

    @Resource
    private MessageTemplateService msgTemplateService;

    @Resource
    private LeoRobotHelper leoRobotHelper;

    @Resource
    private UserService userService;

    private BaseDeployHandler next;

    protected KubernetesConfig getKubernetesConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(uuid);
        return dsConfigHelper.build(dsConfig, KubernetesConfig.class);
    }

    public BaseDeployHandler setNextHandler(BaseDeployHandler next) {
        this.next = next;
        return this.next;
    }

    public BaseDeployHandler getNext() {
        return next;
    }

    public void handleRequest(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        try {
            this.handle(leoDeploy, deployConfig);
        } catch (LeoDeployException e) {
            // 记录日志
            logHelper.error(leoDeploy, e.getMessage());
            leoDeploy.setDeployResult(RESULT_ERROR);
            leoDeploy.setEndTime(new Date());
            leoDeploy.setIsFinish(true);
            leoDeploy.setDeployStatus(e.getMessage());
            leoDeploy.setIsActive(false);
            save(leoDeploy);
            throw e;
        }
        getNext().handleRequest(leoDeployService.getById(leoDeploy.getId()), deployConfig);
    }

    /**
     * 抽象方法，具体实现
     *
     * @param leoDeploy
     * @param deployConfig
     */
    protected abstract void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig);

    protected void save(LeoDeploy saveLeoDeploy) {
        leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
    }

    protected void save(LeoDeploy saveLeoDeploy, String log, Object... var2) {
        leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
        logHelper.info(saveLeoDeploy, log, var2);
    }

    protected void sendMessage(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig, String messageKey, Map<String, Object> contentMap) {
        String dingtalkRobot = Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getNotify)
                .map(LeoBaseModel.Notify::getName)
                .orElseThrow(() -> new LeoBuildException("发送消息失败: DingtalkRobot未配置！"));

        DatasourceInstance dsInstance = leoRobotHelper.getRobotInstance(dingtalkRobot);
        MessageTemplate messageTemplate = msgTemplateService.getByUniqueKey(messageKey, "DINGTALK_ROBOT", "markdown");
        if (messageTemplate == null)
            throw new LeoBuildException("发送消息失败: 消息模板未配置！");

        User user = userService.getByUsername(leoDeploy.getUsername());

        Map<String, Object> messageMap = Maps.newHashMap(deployConfig.getDeploy().getDict());
        messageMap.putAll(contentMap);
        messageMap.put("nowDate", TimeUtil.nowDate());
        messageMap.put("users", Lists.newArrayList(user));

        final String msg = renderTemplate(messageTemplate, messageMap);
        leoRobotHelper.send(dsInstance, msg);
    }

    protected String renderTemplate(MessageTemplate messageTemplate, Map<String, Object> contentMap) {
        try {
            return BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);
        } catch (IOException e) {
            throw new LeoDeployException("渲染Dingtalk模板错误: err={}", e.getMessage());
        }
    }

}
