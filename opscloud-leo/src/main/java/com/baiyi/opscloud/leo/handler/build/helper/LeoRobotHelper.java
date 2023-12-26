package com.baiyi.opscloud.leo.handler.build.helper;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.message.notice.DingtalkSendHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/15 17:16
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoRobotHelper {

    private final DsInstanceService dsInstanceService;

    private final DsConfigManager dsConfigManager;

    private final DingtalkSendHelper dingtalkSendHelper;

    public DatasourceInstance getRobotInstance(String name) {
        List<DatasourceInstance> instances = dsInstanceService.listByInstanceType(DsTypeEnum.DINGTALK_ROBOT.getName());
        return instances.stream().filter(instance -> name.equals(instance.getInstanceName())).findFirst()
                .orElseThrow(() -> new LeoBuildException("未找到DingtalkRobot配置！"));
    }

    public void send(DatasourceInstance dsInstance, String msg) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigById(dsInstance.getConfigId());
        DingtalkConfig dingtalkConfig = dsConfigManager.build(dsConfig, DingtalkConfig.class);
        final String token = Optional.of(dingtalkConfig)
                .map(DingtalkConfig::getRobot)
                .map(DingtalkConfig.Robot::getToken)
                .orElseThrow(() -> new LeoBuildException("未找到DingtalkRobot Token配置！"));
        dingtalkSendHelper.send(token, msg);
    }

}