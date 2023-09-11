package com.baiyi.opscloud.finops;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.vo.finops.KubernetesFinOpsVO;
import com.baiyi.opscloud.facade.kubernetes.KubernetesFinOpsReportFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/8/29 14:37
 * @Version 1.0
 */
public class KubernetesFinOpsReportFacadeTest extends BaseUnit {

    @Resource
    private KubernetesFinOpsReportFacade kubernetesFinOpsReportFacade;


    @Test
    void reportTest() {
        // ack-frankfurt-prod 84
        KubernetesFinOpsVO.FinOpsReport report = kubernetesFinOpsReportFacade.getKubernetesFinOps(84);
    }
}
