package com.baiyi.opscloud.mapper.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import tk.mybatis.mapper.common.Mapper;

public interface AssetsNodeMapper extends Mapper<AssetsNode> {

    AssetsNode queryAssetsNodeLastOne();
}