package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.loop.kubernetes.KubernetesDeploymentResponse;

/**
 * @Author baiyi
 * @Date 2023/5/22 17:01
 * @Version 1.0
 */
public interface KubernetesTerminalFacade {

    KubernetesDeploymentResponse<ApplicationVO.Kubernetes> getKubernetesDeployment(int applicationId, int envType);

}
