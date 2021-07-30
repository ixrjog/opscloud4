package com.baiyi.opscloud.kubernetes.terminal.factory;

import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.config.TerminalConfig;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.message.kubernetes.BaseKubernetesMessage;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/7/15 10:08 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractKubernetesTerminalProcess<T extends BaseKubernetesMessage> implements ITerminalProcess, InitializingBean {

    @Resource
    protected TerminalSessionService terminalSessionService;

    @Resource
    protected TerminalSessionInstanceService terminalSessionInstanceService;

    @Resource
    protected RedisUtil redisUtil;

    @Resource
    private TerminalConfig terminalConfig;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    @Resource
    protected DsInstanceService dsInstanceService;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    abstract protected T getMessage(String message);

    protected KubernetesDsInstanceConfig buildConfig(KubernetesResource kubernetesResource) {
        DatasourceInstanceAsset asset = getAssetByResource(kubernetesResource);
        return buildConfig(asset.getInstanceUuid());
    }

    private KubernetesDsInstanceConfig buildConfig(String instanceUuid) {
        DatasourceInstance instance = dsInstanceService.getByUuid(instanceUuid);
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
        return dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);
    }

    private DatasourceInstanceAsset getAssetByResource(KubernetesResource kubernetesResource) {
        if (kubernetesResource.getBusinessType() == BusinessTypeEnum.ASSET.getType()) {
            DatasourceInstanceAsset asset = dsInstanceAssetService.getById(kubernetesResource.getBusinessId());
            if (DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.getType().equals(asset.getAssetType()))
                return asset;
        }
        throw new CommonRuntimeException("类型不符合");
    }

    protected Boolean isBatch(TerminalSession terminalSession) {
        Boolean isBatch = JSchSessionContainer.getBatchBySessionId(terminalSession.getSessionId());
        return isBatch == null ? false : isBatch;
    }

//    protected void closeSessionInstance(TerminalSession terminalSession, String instanceId) {
//        TerminalSessionInstance terminalSessionInstance = terminalSessionInstanceService.getByUniqueKey(terminalSession.getSessionId(), instanceId);
//        terminalSessionInstance.setCloseTime((new Date()));
//        terminalSessionInstance.setInstanceClosed(true);
//        terminalSessionInstance.setOutputSize(IOUtil.fileSize(terminalConfig.buildAuditLogPath(terminalSession.getSessionId(), instanceId)));
//        terminalSessionInstanceService.update(terminalSessionInstance);
//    }

//    protected void recordAuditLog(TerminalSession terminalSession, String instanceId) {
//        AuditRecordHandler.recordAuditLog(terminalSession.getSessionId(), instanceId);
//    }

    protected void heartbeat(String sessionId) {
       // redisUtil.set(TerminalKeyUtil.buildSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        KubernetesTerminalProcessFactory.register(this);
    }

}
