package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/30 3:05 下午
 * @Version 1.0
 */
public interface BusinessAssetRelationService {

    /**
     * 联合主键查询
     * @param businessAssetRelation
     * @return
     */
    BusinessAssetRelation getByUniquekey(BusinessAssetRelation businessAssetRelation);

    /**
     * 查询资产的绑定列表
     * @param businessType
     * @param datasourceInstanceAssetId
     * @return
     */
    List<BusinessAssetRelation> queryAssetRelations(int businessType, int datasourceInstanceAssetId);
}
