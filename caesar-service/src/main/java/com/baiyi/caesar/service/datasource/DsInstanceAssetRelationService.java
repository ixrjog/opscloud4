package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetRelation;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:58 下午
 * @Version 1.0
 */
public interface DsInstanceAssetRelationService {

    void deleteById(Integer id);

    void add(DatasourceInstanceAssetRelation relation);

    void save(DatasourceInstanceAssetRelation relation);

    List<DatasourceInstanceAssetRelation> queryTargetAsset(String instanceUuid, Integer sourceAssetId);

    /**
     * 按资产id查询双向关系
     *
     * @param assetId
     * @return
     */
    List<DatasourceInstanceAssetRelation> queryByAssetId(Integer assetId);

}
