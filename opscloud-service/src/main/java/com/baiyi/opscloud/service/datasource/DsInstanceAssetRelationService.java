package com.baiyi.opscloud.service.datasource;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:58 下午
 * @Version 1.0
 */
public interface DsInstanceAssetRelationService {

    void deleteById(Integer id);

    void add(DatasourceInstanceAssetRelation relation);

    DatasourceInstanceAssetRelation save(DatasourceInstanceAssetRelation relation);

    List<DatasourceInstanceAssetRelation> queryTargetAsset(String instanceUuid, Integer sourceAssetId);

    List<DatasourceInstanceAssetRelation> queryTargetAsset(Integer sourceAssetId);

    /**
     * 按资产id查询双向关系
     *
     * @param assetId
     * @return
     */
    List<DatasourceInstanceAssetRelation> queryByAssetId(Integer assetId);

}