package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;

/**
 * @Author baiyi
 * @Date 2021/8/2 11:28 上午
 * @Version 1.0
 */
public interface BusinessAssetRelationFacade {

    void bindAsset(BusinessAssetRelationVO.IBusinessAssetRelation iBusinessAssetRelation);
}
