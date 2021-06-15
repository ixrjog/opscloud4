package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountRelation;
import com.baiyi.caesar.mapper.caesar.DatasourceAccountRelationMapper;
import com.baiyi.caesar.service.datasource.DsAccountRelationService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/15 1:13 下午
 * @Version 1.0
 */
@Service
public class DsAccountRelationServiceImpl implements DsAccountRelationService {

    @Resource
    private DatasourceAccountRelationMapper dsAccountRelationMapper;

    @Override
    public void add(DatasourceAccountRelation relation) {
        dsAccountRelationMapper.insert(relation);
    }

    @Override
    public void deleteById(Integer id) {
        dsAccountRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DatasourceAccountRelation getByUniqueKey(DatasourceAccountRelation relation) {
        Example example = new Example(DatasourceAccountRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", relation.getAccountUid())
                .andEqualTo("relationType", relation.getRelationType())
                .andEqualTo("targetId", relation.getTargetId())
                .andEqualTo("datasourceAccountId", relation.getDatasourceAccountId());
        return dsAccountRelationMapper.selectOneByExample(example);
    }

    @Override
    public List<DatasourceAccountRelation> queryRelationshipsByTarget(DatasourceAccountRelation relation) {
        Example example = new Example(DatasourceAccountRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", relation.getAccountUid())
                .andEqualTo("relationType", relation.getRelationType())
                .andEqualTo("targetId", relation.getTargetId());
        return dsAccountRelationMapper.selectByExample(example);
    }

}
