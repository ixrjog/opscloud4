package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.vo.finops.KubernetesFinOpsVO;

/**
 * @Author baiyi
 * @Date 2023/8/29 11:34
 * @Version 1.0
 */
public interface KubernetesFinOpsReportFacade {

    KubernetesFinOpsVO.FinOpsReport getKubernetesFinOps(int instanceId);

}
