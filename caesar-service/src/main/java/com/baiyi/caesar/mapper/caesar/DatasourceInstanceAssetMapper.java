package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DatasourceInstanceAssetMapper extends Mapper<DatasourceInstanceAsset> {

    List<String> queryInstanceAssetTypes(String instanceUuid);
}