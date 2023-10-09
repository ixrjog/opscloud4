package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.kubernetes.IstioParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.kubernetes.IstioFacade;
import com.baiyi.opscloud.facade.kubernetes.KubernetesTerminalFacade;
import com.baiyi.opscloud.loop.kubernetes.KubernetesDeploymentResponse;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    private final IstioFacade istioFacade;

    @Operation(summary = "按应用&环境查询无状态信息")
    @GetMapping(value = "/terminal/deployment/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesDeploymentResponse<ApplicationVO.Kubernetes>> getKubernetesDeployment(@RequestParam @Valid int applicationId, @RequestParam @Valid int envType) {
        return new HttpResult<>(kubernetesTerminalFacade.getKubernetesDeployment(applicationId, envType));
    }

    @Operation(summary = "查询VirtualService")
    @PostMapping(value = "/istio/virtualService/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<VirtualService> getIstioVirtualService(@RequestBody IstioParam.GetResource getResource) {
        return new HttpResult<>(istioFacade.getIstioVirtualService(getResource));
    }

    @Operation(summary = "更新VirtualService")
    @PutMapping(value = "/istio/virtualService/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<VirtualService> updateIstioVirtualService(@RequestBody IstioParam.UpdateResource updateResource) {
        return new HttpResult<>(istioFacade.updateIstioVirtualService(updateResource));
    }

    @Operation(summary = "创建VirtualService")
    @PostMapping(value = "/istio/virtualService/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<VirtualService> createIstioVirtualService(@RequestBody IstioParam.CreateResource createResource) {
        return new HttpResult<>(istioFacade.createIstioVirtualService(createResource));
    }

    @Operation(summary = "查询DestinationRule")
    @PostMapping(value = "/istio/destinationRule/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DestinationRule> getIstioDestinationRule(@RequestBody IstioParam.GetResource getResource) {
        return new HttpResult<>(istioFacade.getIstioDestinationRule(getResource));
    }

    @Operation(summary = "更新DestinationRule")
    @PutMapping(value = "/istio/destinationRule/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DestinationRule> updateIstioDestinationRule(@RequestBody IstioParam.UpdateResource updateResource) {
        return new HttpResult<>(istioFacade.updateIstioDestinationRule(updateResource));
    }

    @Operation(summary = "创建DestinationRule")
    @PostMapping(value = "/istio/destinationRule/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DestinationRule> createIstioDestinationRule(@RequestBody IstioParam.CreateResource createResource) {
        return new HttpResult<>(istioFacade.createIstioDestinationRule(createResource));
    }

}
