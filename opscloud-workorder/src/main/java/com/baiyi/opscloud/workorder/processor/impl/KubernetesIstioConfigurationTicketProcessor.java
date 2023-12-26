package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesDeploymentException;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.KubernetesDeploymentIstioEntry;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.baiyi.opscloud.datasource.kubernetes.converter.DeploymentAssetConverter.SIDECAR_ISTIO_IO_INJECT;

/**
 * @Author baiyi
 * @Date 2023/12/7 13:28
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesIstioConfigurationTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<KubernetesDeploymentIstioEntry.KubernetesDeployment, KubernetesConfig> {

    @Resource
    protected DsInstanceFacade<Deployment> dsInstanceFacade;

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        KubernetesDeploymentIstioEntry.KubernetesDeployment entry = this.toEntry(ticketEntry.getContent());
        KubernetesConfig.Kubernetes config = getDsConfig(ticketEntry, KubernetesConfig.class).getKubernetes();
        if (StringUtils.isEmpty(entry.getNamespace())) {
            throw new TicketVerifyException("Verification of work order entry failed: No namespace specified！");
        }

        if (StringUtils.isEmpty(entry.getDeploymentName())) {
            throw new TicketVerifyException("Verification of work order entry failed: Deployment name not specified！");
        }

        Deployment deployment = KubernetesDeploymentDriver.get(config, entry.getNamespace(), entry.getDeploymentName());
        if (deployment == null) {
            throw new TicketVerifyException("Verification of work order entry failed: Deployment does not exist！");
        }
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.KUBERNETES_ISTIO_CONFIGURATION.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    protected Class<KubernetesDeploymentIstioEntry.KubernetesDeployment> getEntryClassT() {
        return KubernetesDeploymentIstioEntry.KubernetesDeployment.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, KubernetesDeploymentIstioEntry.KubernetesDeployment entry) throws TicketProcessException {
        processHandle(ticketEntry, entry);
        KubernetesConfig.Kubernetes config = getDsConfig(ticketEntry, KubernetesConfig.class).getKubernetes();
        try {
            // 录入资产
            Deployment deployment = KubernetesDeploymentDriver.get(config, entry.getNamespace(), entry.getDeploymentName());
            dsInstanceFacade.pullAsset(ticketEntry.getInstanceUuid(), getAssetType(), deployment);
        } catch (Exception e) {
            throw new TicketProcessException("Failed to change container JVM specification: {}", e.getMessage());
        }
    }

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, KubernetesDeploymentIstioEntry.KubernetesDeployment entry) throws TicketProcessException {
        KubernetesConfig.Kubernetes config = getDsConfig(ticketEntry, KubernetesConfig.class).getKubernetes();
        try {
            Deployment deployment = KubernetesDeploymentDriver.get(config, entry.getNamespace(), entry.getDeploymentName());
            Map<String, String> labels = Optional.ofNullable(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getTemplate)
                    .map(PodTemplateSpec::getMetadata)
                    .map(ObjectMeta::getLabels)
                    .orElseThrow(() -> new TicketVerifyException("Not found: deployment->spec->template->metadata->labels"));
            final String istioInject = Boolean.toString(!BooleanUtils.toBoolean(entry.getIstioInject()));
            labels.put(SIDECAR_ISTIO_IO_INJECT, istioInject);
            KubernetesDeploymentDriver.update(config, entry.getNamespace(), deployment);
            log.info("应用服务网格配置: instanceUuid={}, inject={}", ticketEntry.getInstanceUuid(), istioInject);
        } catch (KubernetesDeploymentException e) {
            throw new TicketProcessException("Failed to change Kubernetes istio configuration: {}", e.getMessage());
        }
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name();
    }

}