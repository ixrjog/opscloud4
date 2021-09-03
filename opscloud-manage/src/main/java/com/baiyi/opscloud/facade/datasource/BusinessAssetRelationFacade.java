package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;

/**
 * @Author baiyi
 * @Date 2021/8/2 11:28 上午
 * @Version 1.0
 */
public interface BusinessAssetRelationFacade {

    /**
     * 业务对象绑定资产
     *
     * @param iBusinessAssetRelation
     */
    void bindAsset(BusinessAssetRelationVO.IBusinessAssetRelation iBusinessAssetRelation);

    /**
     * 解除业务对象与资产绑定
     * @param iBusiness
     */
    void unbindAsset(BaseBusiness.IBusiness iBusiness);

}
