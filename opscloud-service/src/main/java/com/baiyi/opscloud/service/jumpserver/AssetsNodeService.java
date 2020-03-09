package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;

/**
 * @Author baiyi
 * @Date 2020/3/9 9:25 上午
 * @Version 1.0
 */
public interface AssetsNodeService {

    AssetsNode queryAssetsNodeByValue(String value);

    AssetsNode queryAssetsNodeLastOne();

    AssetsNode queryAssetsNodeByKey(String key);

    void addAssetsNode(AssetsNode assetsNode);
}
