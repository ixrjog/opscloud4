package com.baiyi.opscloud.kubernetes.terminal.handler;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.kubernetes.terminal.factory.KubernetesTerminalMessageHandlerFactory;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/7/15 10:08 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractKubernetesTerminalMessageHandler<T extends KubernetesMessage.BaseMessage> implements ITerminalMessageHandler, InitializingBean {

    @Resource
    protected TerminalSessionService terminalSessionService;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsConfigManager dsConfigManager;

    /**
     * 转换消息
     * @param message
     * @return
     */
    abstract protected T toMessage(String message);

    protected KubernetesConfig buildConfig(KubernetesResource kubernetesResource) {
        DatasourceInstanceAsset asset = getAssetByResource(kubernetesResource);
        return buildConfig(asset.getInstanceUuid());
    }

    private KubernetesConfig buildConfig(String instanceUuid) {
        return dsConfigManager.buildKubernetesConfig(instanceUuid);
    }

    private DatasourceInstanceAsset getAssetByResource(KubernetesResource kubernetesResource) {
        if (kubernetesResource.getBusinessType() == BusinessTypeEnum.ASSET.getType()) {
            DatasourceInstanceAsset asset = dsInstanceAssetService.getById(kubernetesResource.getBusinessId());
            if (DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name().equals(asset.getAssetType())) {
                return asset;
            }
        }
        throw new OCException("类型不符合");
    }

    protected void heartbeat(String sessionId) {
        // redisUtil.set(TerminalKeyUtil.buildSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        KubernetesTerminalMessageHandlerFactory.register(this);
    }

}