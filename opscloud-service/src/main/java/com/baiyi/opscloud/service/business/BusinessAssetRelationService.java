package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/30 3:05 下午
 * @Version 1.0
 */
public interface BusinessAssetRelationService {

    /**
     * 联合主键查询
     *
     * @param businessAssetRelation
     * @return
     */
    BusinessAssetRelation getByUniqueKey(BusinessAssetRelation businessAssetRelation);

    default BusinessAssetRelation getByUniqueKey(BusinessAssetRelationVO.Relation relation) {
        return getByUniqueKey(BeanCopierUtil.copyProperties(relation, BusinessAssetRelation.class));
    }

    void add(BusinessAssetRelation businessAssetRelation);

    //  void deleteById(Integer id);

    void delete(BusinessAssetRelation businessAssetRelation);

    /**
     * 查询资产的绑定列表
     *
     * @param businessType
     * @param datasourceInstanceAssetId
     * @return
     */
    List<BusinessAssetRelation> queryAssetRelations(int businessType, int datasourceInstanceAssetId);

    default List<BusinessAssetRelation> queryAssetRelations(int datasourceInstanceAssetId) {
        return queryAssetRelations(-1, datasourceInstanceAssetId);
    }

    /**
     * 查询业务对象的绑定列表
     *
     * @param businessType
     * @param businessId
     * @return
     */
    List<BusinessAssetRelation> queryBusinessRelations(int businessType, int businessId);

    default List<BusinessAssetRelation> queryBusinessRelations(BaseBusiness.IBusiness iBusiness) {
        return queryBusinessRelations(iBusiness.getBusinessType(), iBusiness.getBusinessId());
    }

    List<BusinessAssetRelation> queryBusinessRelations(BaseBusiness.IBusiness iBusiness, String assetType);

    BusinessAssetRelation getById(Integer id);

}