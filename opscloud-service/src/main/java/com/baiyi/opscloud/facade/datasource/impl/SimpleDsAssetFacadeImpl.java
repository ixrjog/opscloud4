package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.facade.datasource.SimpleDsAssetFacade;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.project.ProjectResourceService;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/29 4:36 下午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleDsAssetFacadeImpl implements SimpleDsAssetFacade {

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceAssetRelationService dsInstanceAssetRelationService;

    private final DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private final ApplicationResourceService applicationResourceService;

    private final BusinessAssetRelationService businessAssetRelationService;

    private final BusinessTemplateService businessTemplateService;

    private final ProjectResourceService projectResourceService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteAssetById(Integer id) {
        List<BusinessAssetRelation> businessAssetRelations = businessAssetRelationService.queryAssetRelations(id);
        if (!CollectionUtils.isEmpty(businessAssetRelations)) {
            for (BusinessAssetRelation e : businessAssetRelations) {
                log.info("删除业务对象绑定关系: businessType={}, businessId={}, assetId={}", e.getBusinessType(), e.getBusinessId(), e.getDatasourceInstanceAssetId());
                businessAssetRelationService.delete(e);
            }
        }

        List<DatasourceInstanceAssetRelation> datasourceInstanceAssetRelations = dsInstanceAssetRelationService.queryByAssetId(id);
        if (!CollectionUtils.isEmpty(datasourceInstanceAssetRelations)) {
            for (DatasourceInstanceAssetRelation e : datasourceInstanceAssetRelations) {
                log.info("删除资产间关系: datasourceInstanceAssetRelationId={}", e.getId());
                dsInstanceAssetRelationService.deleteById(e.getId());
            }
        }

        List<DatasourceInstanceAssetProperty> properties = dsInstanceAssetPropertyService.queryByAssetId(id);
        if (!CollectionUtils.isEmpty(properties)) {
            for (DatasourceInstanceAssetProperty e : properties) {
                log.info("删除资产属性: datasourceInstanceAssetPropertyId={}", e.getId());
                dsInstanceAssetPropertyService.deleteById(e.getId());
            }
        }

        List<ApplicationResource> applicationResourceList = applicationResourceService.queryByBusiness(BusinessTypeEnum.ASSET.getType(), id);
        if (!CollectionUtils.isEmpty(applicationResourceList)) {
            for (ApplicationResource e : applicationResourceList) {
                log.info("删除应用绑定关系: applicationResourceId={}", e.getId());
                applicationResourceService.deleteById(e.getId());
            }
        }

        List<DatasourceInstanceAsset> assetList = dsInstanceAssetService.listByParentId(id);
        if (!CollectionUtils.isEmpty(assetList)) {
            for (DatasourceInstanceAsset e : assetList) {
                log.info("删除Children: datasourceInstanceAssetId={}", e.getId());
                deleteAssetById(e.getId());
            }
        }

        List<BusinessTemplate> businessTemplates = businessTemplateService.queryByBusinessId(id);
        if (!CollectionUtils.isEmpty(businessTemplates)) {
            for (BusinessTemplate e : businessTemplates) {
                log.info("删除模板关联资产: businessTemplateId={}", e.getId());
                e.setBusinessId(0);
                businessTemplateService.update(e);
            }
        }

        List<ProjectResource> projectResourceList = projectResourceService.queryByBusiness(BusinessTypeEnum.ASSET.getType(), id);
        if (!CollectionUtils.isEmpty(projectResourceList)) {
            for (ProjectResource e : projectResourceList) {
                log.info("删除项目绑定关系: projectResourceId={}", e.getId());
                projectResourceService.deleteById(e.getId());
            }
        }

        log.info("删除资产: assetId={}", id);
        dsInstanceAssetService.deleteById(id);
    }

}