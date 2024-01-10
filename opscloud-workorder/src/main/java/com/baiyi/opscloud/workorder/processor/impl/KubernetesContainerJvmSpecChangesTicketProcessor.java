package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesDeploymentException;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.KubernetesContainerJvmSpecEntry;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.helper.ContainerJvmSpecHelper;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/11/2 17:16
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesContainerJvmSpecChangesTicketProcessor
        extends AbstractDsAssetExtendedBaseTicketProcessor<KubernetesContainerJvmSpecEntry.KubernetesDeployment, KubernetesConfig> {

    private static final String SPEC = "spec";

    @Resource
    protected DsInstanceFacade<Deployment> dsInstanceFacade;

    @Override
    public void update(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        WorkOrderTicket ticket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!OrderTicketPhaseCodeConstants.NEW.name().equals(ticket.getTicketPhase())) {
            throw new TicketProcessException("工单进度不是新建，无法更新配置条目！");
        }
        WorkOrderTicketEntry preTicketEntry = ticketEntryService.getById(ticketEntry.getId());
        Map<String, String> properties = ticketEntry.getProperties();
        if (properties == null) {
            return;
        }
        if (!properties.containsKey(SPEC)) {
            return;
        }
        KubernetesContainerJvmSpecEntry.KubernetesDeployment deployment = toEntry(preTicketEntry.getContent());
        deployment.setSpec(properties.get(SPEC));
        preTicketEntry.setContent(deployment.toString());
        updateTicketEntry(preTicketEntry);
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        KubernetesContainerJvmSpecEntry.KubernetesDeployment entry = this.toEntry(ticketEntry.getContent());
        KubernetesConfig.Kubernetes config = getDsConfig(ticketEntry, KubernetesConfig.class).getKubernetes();
        if (StringUtils.isEmpty(entry.getNamespace())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定Namespace命名空间！");
        }

        if (StringUtils.isEmpty(entry.getDeploymentName())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定Deployment(无状态)名称！");
        }

        Deployment deployment = KubernetesDeploymentDriver.get(config, entry.getNamespace(), entry.getDeploymentName());
        Container container = getContainer(entry, deployment);
        container.getEnv()
                .stream()
                .filter(e -> "JAVA_OPTS".equals(e.getName()))
                .findFirst()
                .orElseThrow(() -> new TicketVerifyException("Kubernetes deployment 未找到容器环境变量 JAVA_OPTS !"));
    }

    private Container getContainer(KubernetesContainerJvmSpecEntry.KubernetesDeployment entry, Deployment deployment) {
        return Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getSpec)
                .map(PodSpec::getContainers)
                .orElseThrow(() -> new TicketVerifyException("Kubernetes deployment 未找到容器配置！"))
                .stream()
                .filter(e -> entry.getDeploymentName().startsWith(e.getName()))
                .findFirst()
                .orElseThrow(() -> new TicketVerifyException("Kubernetes deployment 未找到容器: {}！", entry.getDeploymentName()));
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.KUBERNETES_CONTAINER_JVM_SPEC_CHANGES.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    protected Class<KubernetesContainerJvmSpecEntry.KubernetesDeployment> getEntryClassT() {
        return KubernetesContainerJvmSpecEntry.KubernetesDeployment.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, KubernetesContainerJvmSpecEntry.KubernetesDeployment entry) throws TicketProcessException {
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
    protected void processHandle(WorkOrderTicketEntry ticketEntry, KubernetesContainerJvmSpecEntry.KubernetesDeployment entry) throws TicketProcessException {
        KubernetesConfig.Kubernetes config = getDsConfig(ticketEntry, KubernetesConfig.class).getKubernetes();
        try {
            Deployment deployment = KubernetesDeploymentDriver.get(config, entry.getNamespace(), entry.getDeploymentName());
            // application container
            Container container = getContainer(entry, deployment);
            // 修改JAVA_OPTS环境变量
            EnvVar envVar = container.getEnv()
                    .stream()
                    .filter(e -> "JAVA_OPTS".equals(e.getName()))
                    .findFirst()
                    .orElseThrow(() -> new TicketVerifyException("Kubernetes deployment 未找到容器环境变量 JAVA_OPTS !"));
            envVar.setValue(ContainerJvmSpecHelper.format(ContainerJvmSpecHelper.parse(entry.getSpec(), envVar.getValue())));
            // 修改resource
            modifyResource(entry.getSpec(), container.getResources());
            KubernetesDeploymentDriver.update(config, entry.getNamespace(), deployment);
            log.info("变更Kubernetes容器JVM规格: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), entry);
        } catch (KubernetesDeploymentException e) {
            throw new TicketProcessException("Failed to change container JVM specification: {}", e.getMessage());
        }
    }

    private void modifyResource(String spec, ResourceRequirements resource) {
        switch (spec) {
            case "SMALL" -> modifyResource(resource, "500m", "1Gi", "1", "2Gi");
            case "LARGE" -> modifyResource(resource, "500m", "3Gi", "2", "4Gi");
            case "XLARGE" -> modifyResource(resource, "2", "4Gi", "4", "8Gi");
            case "XLARGE2" -> modifyResource(resource, "4", "10Gi", "8", "16Gi");
            default -> throw new TicketProcessException("Invalid container specification: {}", spec);
        }
    }

    private void modifyResource(ResourceRequirements resource, String cpuRequest, String memRequest, String cpuLimit, String memLimit) {
        Map<String, Quantity> requests = Maps.newHashMap();
        requests.put("cpu", new Quantity(cpuRequest));
        requests.put("memory", new Quantity(memRequest));
        resource.setRequests(requests);
        Map<String, Quantity> limits = Maps.newHashMap();
        limits.put("cpu", new Quantity(cpuLimit));
        limits.put("memory", new Quantity(memLimit));
        resource.setLimits(limits);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name();
    }

}