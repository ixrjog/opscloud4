package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountRelation;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/15 1:13 下午
 * @Version 1.0
 */
public interface DsAccountRelationService {

    void add(DatasourceAccountRelation relation);

    void deleteById(Integer id);

    DatasourceAccountRelation getByUniqueKey(DatasourceAccountRelation relation);

    List<DatasourceAccountRelation> queryRelationshipsByTarget(DatasourceAccountRelation relation);
}
