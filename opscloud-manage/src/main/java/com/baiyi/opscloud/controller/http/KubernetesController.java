package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIngressParam;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIstioParam;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesParam;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.kubernetes.*;
import com.baiyi.opscloud.loop.kubernetes.KubernetesDeploymentResponse;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.EnvoyFilter;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/22 16:54
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/kubernetes")
@Tag(name = "Kubernetes")
@RequiredArgsConstructor
public class KubernetesController {

    private final KubernetesTerminalFacade kubernetesTerminalFacade;

    private final KubernetesIstioFacade istioFacade;

    private final KubernetesIngressFacade ingressFacade;

    private final KubernetesServiceFacade serviceFacade;

    private final KubernetesFacade kubernetesFacade;

    @Operation(summary = "按应用&环境查询无状态信息")
    @GetMapping(value = "/terminal/deployment/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesDeploymentResponse<ApplicationVO.Kubernetes>> getKubernetesDeployment(@RequestParam @Valid int applicationId, @RequestParam @Valid int envType) {
        return new HttpResult<>(kubernetesTerminalFacade.getKubernetesDeployment(applicationId, envType));
    }

    @Operation(summary = "查询VirtualService")
    @PostMapping(value = "/istio/virtualService/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<VirtualService> getIstioVirtualService(@RequestBody KubernetesIstioParam.GetResource getResource) {
        return new HttpResult<>(istioFacade.getVirtualService(getResource));
    }

    @Operation(summary = "更新VirtualService")
    @PutMapping(value = "/istio/virtualService/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<VirtualService> updateIstioVirtualService(@RequestBody KubernetesIstioParam.UpdateResource updateResource) {
        return new HttpResult<>(istioFacade.updateVirtualService(updateResource));
    }

    @Operation(summary = "删除VirtualService")
    @DeleteMapping(value = "/istio/virtualService/del", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<StatusDetails>> deleteIstioVirtualService(@RequestBody KubernetesIstioParam.DeleteResource deleteResource) {
        return new HttpResult<>(istioFacade.deleteVirtualService(deleteResource));
    }

    @Operation(summary = "创建VirtualService")
    @PostMapping(value = "/istio/virtualService/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<VirtualService> createIstioVirtualService(@RequestBody KubernetesIstioParam.CreateResource createResource) {
        return new HttpResult<>(istioFacade.createVirtualService(createResource));
    }

    @Operation(summary = "查询DestinationRule")
    @PostMapping(value = "/istio/destinationRule/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DestinationRule> getIstioDestinationRule(@RequestBody KubernetesIstioParam.GetResource getResource) {
        return new HttpResult<>(istioFacade.getDestinationRule(getResource));
    }

    @Operation(summary = "更新DestinationRule")
    @PutMapping(value = "/istio/destinationRule/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DestinationRule> updateIstioDestinationRule(@RequestBody KubernetesIstioParam.UpdateResource updateResource) {
        return new HttpResult<>(istioFacade.updateDestinationRule(updateResource));
    }

    @Operation(summary = "删除DestinationRule")
    @DeleteMapping(value = "/istio/destinationRule/del", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<StatusDetails>> deleteIstioDestinationRule(@RequestBody KubernetesIstioParam.DeleteResource deleteResource) {
        return new HttpResult<>(istioFacade.deleteDestinationRule(deleteResource));
    }

    @Operation(summary = "创建DestinationRule")
    @PostMapping(value = "/istio/destinationRule/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DestinationRule> createIstioDestinationRule(@RequestBody KubernetesIstioParam.CreateResource createResource) {
        return new HttpResult<>(istioFacade.createDestinationRule(createResource));
    }

    @Operation(summary = "查询Deployment")
    @PostMapping(value = "/deployment/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Deployment> getDeployment(@RequestBody KubernetesParam.GetResource getResource) {
        return new HttpResult<>(kubernetesFacade.getKubernetesDeployment(getResource));
    }

    // Ingress

    @Operation(summary = "查询Ingress")
    @PostMapping(value = "/ingress/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Ingress> getIngress(@RequestBody KubernetesIngressParam.GetResource getResource) {
        return new HttpResult<>(ingressFacade.get(getResource));
    }

    @Operation(summary = "更新Ingress")
    @PutMapping(value = "/ingress/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Ingress> updateIngress(@RequestBody KubernetesIngressParam.UpdateResource updateResource) {
        return new HttpResult<>(ingressFacade.update(updateResource));
    }

    @Operation(summary = "创建Ingress")
    @PostMapping(value = "/ingress/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Ingress> createIngress(@RequestBody KubernetesIngressParam.CreateResource createResource) {
        return new HttpResult<>(ingressFacade.create(createResource));
    }

    // Service

    @Operation(summary = "查询Service")
    @PostMapping(value = "/service/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Service> getService(@RequestBody KubernetesServiceParam.GetResource getResource) {
        return new HttpResult<>(serviceFacade.get(getResource));
    }

    @Operation(summary = "更新Service")
    @PutMapping(value = "/service/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Service> updateService(@RequestBody KubernetesServiceParam.UpdateResource updateResource) {
        return new HttpResult<>(serviceFacade.update(updateResource));
    }

    @Operation(summary = "删除Service")
    @DeleteMapping(value = "/service/del", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<StatusDetails>> deleteService(@RequestBody KubernetesServiceParam.DeleteResource deleteResource) {
        return new HttpResult<>(serviceFacade.delete(deleteResource));
    }

    @Operation(summary = "创建Service")
    @PostMapping(value = "/service/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Service> createService(@RequestBody KubernetesServiceParam.CreateResource createResource) {
        return new HttpResult<>(serviceFacade.create(createResource));
    }

    // EnvoyFilter

    @Operation(summary = "查询EnvoyFilter")
    @PostMapping(value = "/istio/envoyFilter/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EnvoyFilter> getIstioEnvoyFilter(@RequestBody KubernetesIstioParam.GetResource getResource) {
        return new HttpResult<>(istioFacade.getEnvoyFilter(getResource));
    }

    @Operation(summary = "更新EnvoyFilter")
    @PutMapping(value = "/istio/envoyFilter/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EnvoyFilter> updateIstioEnvoyFilter(@RequestBody KubernetesIstioParam.UpdateResource updateResource) {
        return new HttpResult<>(istioFacade.updateEnvoyFilter(updateResource));
    }

    @Operation(summary = "创建EnvoyFilter")
    @PostMapping(value = "/istio/envoyFilter/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EnvoyFilter> createIstioEnvoyFilter(@RequestBody KubernetesIstioParam.CreateResource createResource) {
        return new HttpResult<>(istioFacade.createEnvoyFilter(createResource));
    }

    @Operation(summary = "删除EnvoyFilter")
    @DeleteMapping(value = "/istio/envoyFilter/del", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<StatusDetails>> deleteIstioEnvoyFilter(@RequestBody KubernetesIstioParam.DeleteResource deleteResource) {
        return new HttpResult<>(istioFacade.deleteEnvoyFilter(deleteResource));
    }

}