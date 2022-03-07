package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.facade.datasource.SimpleDsAssetFacade;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/29 4:36 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SimpleDsAssetFacadeImpl implements SimpleDsAssetFacade {

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceAssetRelationService dsInstanceAssetRelationService;

    private final DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private final ApplicationResourceService applicationResourceService;

    private final BusinessAssetRelationService businessAssetRelationService;

    private final BusinessTemplateService businessTemplateService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteAssetById(Integer id) {
        // 删除业务对象绑定关系
        List<BusinessAssetRelation> businessAssetRelations = businessAssetRelationService.queryAssetRelations(id);
        if (!CollectionUtils.isEmpty(businessAssetRelations)) {
            businessAssetRelations.forEach(e -> businessAssetRelationService.delete(e));
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
        if (!CollectionUtils.isEmpty(assetList)) {
            assetList.forEach(x -> deleteAssetById(x.getId()));
        }
        // 删除模板关联资产
        List<BusinessTemplate> businessTemplates = businessTemplateService.queryByBusinessId(id);
        if (!CollectionUtils.isEmpty(businessTemplates)) {
            businessTemplates.forEach(e -> {
                e.setBusinessId(0);
                businessTemplateService.update(e);
            });
        }
        // 删除自己
        dsInstanceAssetService.deleteById(id);
    }
}
