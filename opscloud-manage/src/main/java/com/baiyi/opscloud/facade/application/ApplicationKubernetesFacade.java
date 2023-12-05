package com.baiyi.opscloud.facade.application;

import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;

/**
 * @Author baiyi
 * @Date 2023/3/27 17:19
 * @Version 1.0
 */
public interface ApplicationKubernetesFacade {

    ApplicationVO.Kubernetes getApplicationKubernetes(ApplicationParam.GetApplicationKubernetes getApplicationKubernetes, String username);

}
