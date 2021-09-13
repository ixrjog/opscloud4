package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DatasourceInstanceAssetMapper extends Mapper<DatasourceInstanceAsset> {

    List<String> queryInstanceAssetTypes(String instanceUuid);

    List<DatasourceInstanceAsset> queryUserPermissionAssetByParam(DsAssetParam.UserPermissionAssetPageQuery pageQuery);
}