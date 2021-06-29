package com.baiyi.caesar.facade.datasource.impl;

import com.baiyi.caesar.domain.annotation.TagClear;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetProperty;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetRelation;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.facade.datasource.BaseDsAssetFacade;
import com.baiyi.caesar.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/29 4:36 下午
 * @Version 1.0
 */
@Service
public class BaseDsAssetFacadeImpl implements BaseDsAssetFacade {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetRelationService dsInstanceAssetRelationService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @TagClear(type = BusinessTypeEnum.ASSET)
    @Override
    public void deleteAssetById(Integer id) {
        // 删除关系
        List<DatasourceInstanceAssetRelation> relations = dsInstanceAssetRelationService.queryByAssetId(id);
        if (!CollectionUtils.isEmpty(relations))
            for (DatasourceInstanceAssetRelation relation : relations) {
                dsInstanceAssetRelationService.deleteById(relation.getId());
            }
        // 删除属性
        List<DatasourceInstanceAssetProperty> properties = dsInstanceAssetPropertyService.queryByAssetId(id);
        if (!CollectionUtils.isEmpty(properties))
            for (DatasourceInstanceAssetProperty property : properties) {
                dsInstanceAssetPropertyService.deleteById(property.getId());
            }
        // 删除自己
        dsInstanceAssetService.deleteById(id);
    }
}
