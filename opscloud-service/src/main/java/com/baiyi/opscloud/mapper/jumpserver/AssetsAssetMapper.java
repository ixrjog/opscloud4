package com.baiyi.opscloud.mapper.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AssetsAssetMapper extends Mapper<AssetsAsset> {

    List<AssetsAsset> queryAssetsAssetPage(AssetsAssetPageParam.PageQuery pageQuery);

}