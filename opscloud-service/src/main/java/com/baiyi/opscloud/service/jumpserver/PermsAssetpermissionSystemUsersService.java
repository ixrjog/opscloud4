package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermissionSystemUsers;

/**
 * @Author baiyi
 * @Date 2020/3/9 12:03 下午
 * @Version 1.0
 */
public interface PermsAssetpermissionSystemUsersService {

    PermsAssetpermissionSystemUsers queryPermsAssetpermissionSystemUsersByUniqueKey(PermsAssetpermissionSystemUsers permsAssetpermissionSystemUsers);
    void addPermsAssetpermissionSystemUsers(PermsAssetpermissionSystemUsers permsAssetpermissionSystemUsers);
}
