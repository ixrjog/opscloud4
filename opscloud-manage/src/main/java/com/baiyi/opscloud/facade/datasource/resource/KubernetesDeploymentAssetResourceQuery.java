package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.domain.annotation.ApplicationResType;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.types.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
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

    @Override
    protected ApplicationResourceVO.Resource toResource(DsAssetVO.Asset asset) {
        ApplicationResourceVO.Resource resource = super.toResource(asset);
        resource.setName(asset.getAssetId());
        return resource;
    }

}
