package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetRelation;
import com.baiyi.caesar.mapper.caesar.DatasourceInstanceAssetRelationMapper;
import com.baiyi.caesar.service.datasource.DsInstanceAssetRelationService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:58 下午
 * @Version 1.0
 */
@Service
public class DsInstanceAssetRelationServiceImpl implements DsInstanceAssetRelationService {

    @Resource
    private DatasourceInstanceAssetRelationMapper dsInstanceAssetRelationMapper;

    @Override
    public void add(DatasourceInstanceAssetRelation relation) {
        dsInstanceAssetRelationMapper.insert(relation);
    }

    @Override
    public void save(DatasourceInstanceAssetRelation relation) {
        Example example = new Example(DatasourceInstanceAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", relation.getInstanceUuid())
                .andEqualTo("relationType", relation.getRelationType())
                .andEqualTo("sourceAssetId",relation.getSourceAssetId())
                .andEqualTo("targetAssetId",relation.getTargetAssetId());
        if(dsInstanceAssetRelationMapper.selectOneByExample(example) == null)
            add(relation);
    }
}
