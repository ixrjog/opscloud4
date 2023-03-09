package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.facade.datasource.BusinessAssetRelationFacade;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/2 11:29 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class BusinessAssetRelationFacadeImpl implements BusinessAssetRelationFacade {

    private final BusinessAssetRelationService bizAssetRelationService;

    private final DsInstanceAssetService dsInstanceAssetService;

    @Override
    public void bindAsset(BusinessAssetRelationVO.IBusinessAssetRelation iBusinessAssetRelation) {
        BusinessAssetRelationVO.Relation relation = iBusinessAssetRelation.toBusinessAssetRelation();
        if (bizAssetRelationService.getByUniqueKey(relation) != null) {
            return;
        }
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(iBusinessAssetRelation.getAssetId());
        if (asset == null) {
            return;
        }
        relation.setAssetType(asset.getAssetType());
        bizAssetRelationService.add(BeanCopierUtil.copyProperties(relation, BusinessAssetRelation.class));
    }

    @Override
    public void unbindAsset(BaseBusiness.IBusiness iBusiness) {
        List<BusinessAssetRelation> businessAssetRelations = bizAssetRelationService.queryBusinessRelations(iBusiness);
        if (CollectionUtils.isEmpty(businessAssetRelations)) {
            return;
        }
        businessAssetRelations.forEach(bizAssetRelationService::delete);
    }

}
