package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermission;

/**
 * @Author baiyi
 * @Date 2020/3/9 11:18 上午
 * @Version 1.0
 */
public interface PermsAssetpermissionService {

    PermsAssetpermission queryPermsAssetpermissionByName(String name);

    void addPermsAssetpermission(PermsAssetpermission permsAssetpermission);
}
