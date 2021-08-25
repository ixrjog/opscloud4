package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.facade.datasource.SimpleDsAssetFacade;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/29 4:36 下午
 * @Version 1.0
 */
@BusinessType(BusinessTypeEnum.ASSET)
@Service
public class SimpleDsAssetFacadeImpl implements SimpleDsAssetFacade {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetRelationService dsInstanceAssetRelationService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private BusinessAssetRelationService businessAssetRelationService;

    @Override
    @TagClear
    @Transactional(rollbackFor = {Exception.class})
    public void deleteAssetById(Integer id) {
        // 删除业务对象绑定关系
        List<BusinessAssetRelation> businessAssetRelations = businessAssetRelationService.queryAssetRelations(id);
        if (!CollectionUtils.isEmpty(businessAssetRelations)) {
            businessAssetRelations.forEach(e -> businessAssetRelationService.deleteById(e.getId()));
        }
        // 删除资产间关系
        List<DatasourceInstanceAssetRelation> datasourceInstanceAssetRelations = dsInstanceAssetRelationService.queryByAssetId(id);
        if (!CollectionUtils.isEmpty(datasourceInstanceAssetRelations)) {
            datasourceInstanceAssetRelations.forEach(relation -> dsInstanceAssetRelationService.deleteById(relation.getId()));
        }
        // 删除资产属性
        List<DatasourceInstanceAssetProperty> properties = dsInstanceAssetPropertyService.queryByAssetId(id);
        if (!CollectionUtils.isEmpty(properties)) {
            properties.forEach(property -> dsInstanceAssetPropertyService.deleteById(property.getId()));
        }
        // 删除应用绑定关系
        List<ApplicationResource> resourceList = applicationResourceService.queryByBusiness(BusinessTypeEnum.ASSET.getType(), id);
        if (!CollectionUtils.isEmpty(resourceList)) {
            resourceList.forEach(resource -> applicationResourceService.delete(resource.getId()));
        }
        // 删除children
        List<DatasourceInstanceAsset> assetList = dsInstanceAssetService.listByParentId(id);
        if (!CollectionUtils.isEmpty(datasourceInstanceAssetRelations)) {
            assetList.parallelStream().forEach(x -> deleteAssetById(x.getId()));
        }
        // 删除自己
        dsInstanceAssetService.deleteById(id);
    }
}
