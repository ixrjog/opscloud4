package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;
import com.baiyi.opscloud.mapper.DatasourceInstanceAssetRelationMapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:58 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DsInstanceAssetRelationServiceImpl implements DsInstanceAssetRelationService {

    private final DatasourceInstanceAssetRelationMapper dsInstanceAssetRelationMapper;

    @Override
    public void deleteById(Integer id) {
        dsInstanceAssetRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void add(DatasourceInstanceAssetRelation relation) {
        dsInstanceAssetRelationMapper.insert(relation);
    }

    @Override
    public DatasourceInstanceAssetRelation save(DatasourceInstanceAssetRelation relation) {
        Example example = new Example(DatasourceInstanceAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", relation.getInstanceUuid())
                .andEqualTo("relationType", relation.getRelationType())
                .andEqualTo("sourceAssetId", relation.getSourceAssetId())
                .andEqualTo("targetAssetId", relation.getTargetAssetId());
        DatasourceInstanceAssetRelation pre = dsInstanceAssetRelationMapper.selectOneByExample(example);
        if (pre != null) {
            return pre;
        }
        add(relation);
        return relation;
    }

    @Override
    public List<DatasourceInstanceAssetRelation> queryTargetAsset(String instanceUuid, Integer sourceAssetId) {
        Example example = new Example(DatasourceInstanceAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", instanceUuid)
                .andEqualTo("sourceAssetId", sourceAssetId);
        return dsInstanceAssetRelationMapper.selectByExample(example);
    }

    @Override
    public List<DatasourceInstanceAssetRelation> queryTargetAsset(Integer sourceAssetId) {
        Example example = new Example(DatasourceInstanceAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sourceAssetId", sourceAssetId);
        return dsInstanceAssetRelationMapper.selectByExample(example);
    }

    @Override
    public List<DatasourceInstanceAssetRelation> queryByAssetId(Integer assetId) {
        Example example = new Example(DatasourceInstanceAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sourceAssetId", assetId)
                .orEqualTo("targetAssetId", assetId);
        return dsInstanceAssetRelationMapper.selectByExample(example);
    }

}