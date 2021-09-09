package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.domain.annotation.ApplicationResType;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.types.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/9 10:51 上午
 * @Version 1.0
 */
@ApplicationResType(ApplicationResTypeEnum.KUBERNETES_DEPLOYMENT)
@BusinessType(BusinessTypeEnum.ASSET)
@Component
public class KubernetesDeploymentAssetResourceQuery extends AbstractAssetResourceQuery {

}
