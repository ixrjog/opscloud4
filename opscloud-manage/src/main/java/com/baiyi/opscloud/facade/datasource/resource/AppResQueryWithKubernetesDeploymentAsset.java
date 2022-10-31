package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.domain.annotation.ApplicationResType;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
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
public class AppResQueryWithKubernetesDeploymentAsset extends AbstractAppResQueryWithAsset {

    @Override
    protected String getResName(DsAssetVO.Asset asset){
        return asset.getAssetId();
    }

    @Override
    protected String getResComment(DsAssetVO.Asset asset) {
        return asset.getAssetId();
    }

}
