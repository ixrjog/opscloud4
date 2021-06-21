package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetRelation;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:58 下午
 * @Version 1.0
 */
public interface DsInstanceAssetRelationService {

    void add(DatasourceInstanceAssetRelation relation);

    void save(DatasourceInstanceAssetRelation relation);

    List<DatasourceInstanceAssetRelation> queryTargetAsset(String instanceUuid, Integer sourceAssetId);

}
