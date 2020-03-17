package com.baiyi.opscloud.mapper.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AssetsNodeMapper extends Mapper<AssetsNode> {

    AssetsNode queryAssetsNodeLastOne();

    List<AssetsNode> queryAssetsAssetPage(AssetsNodePageParam.PageQuery pageQuery);

    AssetsNode  queryAssetsNodeRoot();
}