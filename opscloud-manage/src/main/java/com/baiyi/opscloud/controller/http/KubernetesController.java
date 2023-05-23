package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.kubernetes.KubernetesTerminalFacade;
import com.baiyi.opscloud.loop.kubernetes.KubernetesDeploymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "按应用&环境查询无状态信息")
    @GetMapping(value = "/terminal/deployment/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesDeploymentResponse<ApplicationVO.Kubernetes>> getKubernetesDeployment(@RequestParam @Valid int applicationId, @RequestParam @Valid int envType) {
        return new HttpResult<>(kubernetesTerminalFacade.getKubernetesDeployment(applicationId, envType));
    }

}
