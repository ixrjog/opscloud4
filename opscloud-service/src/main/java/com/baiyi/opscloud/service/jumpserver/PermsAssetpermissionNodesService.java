package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermissionNodes;

/**
 * @Author baiyi
 * @Date 2020/3/9 12:24 下午
 * @Version 1.0
 */
public interface PermsAssetpermissionNodesService {

    PermsAssetpermissionNodes queryPermsAssetpermissionNodesByUniqueKey(PermsAssetpermissionNodes permsAssetpermissionNodes);
    void addPermsAssetpermissionNodes(PermsAssetpermissionNodes permsAssetpermissionNodes);
}
