package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.facade.datasource.BusinessAssetRelationFacade;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/2 11:29 上午
 * @Version 1.0
 */
@Service
public class BusinessAssetRelationFacadeImpl implements BusinessAssetRelationFacade {

    @Resource
    private BusinessAssetRelationService businessAssetRelationService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;


    /**
     * 业务对象绑定资产
     *
     * @param iBusinessAssetRelation
     */
    @Override
    public void bindAsset(BusinessAssetRelationVO.IBusinessAssetRelation iBusinessAssetRelation) {
        BusinessAssetRelationVO.Relation relation = iBusinessAssetRelation.toBusinessAssetRelation();
        if (businessAssetRelationService.getByUniquekey(relation) != null) return;
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(iBusinessAssetRelation.getAssetId());
        if (asset == null) return;
        relation.setAssetType(asset.getAssetType());
        businessAssetRelationService.add(BeanCopierUtil.copyProperties(relation, BusinessAssetRelation.class));
    }


}
