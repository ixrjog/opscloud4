package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;

/**
 * @Author baiyi
 * @Date 2020/3/9 9:25 上午
 * @Version 1.0
 */
public interface AssetsNodeService {

    DataTable<AssetsNode> queryAssetsNodePage(AssetsNodePageParam.PageQuery pageQuery);

    AssetsNode queryAssetsNodeByValue(String value);

    AssetsNode queryAssetsNodeLastOne();

    AssetsNode queryAssetsNodeByKey(String key);

    void addAssetsNode(AssetsNode assetsNode);

    /**
     * 查询根节点
     * @return
     */
    AssetsNode queryAssetsNodeRoot();
}
