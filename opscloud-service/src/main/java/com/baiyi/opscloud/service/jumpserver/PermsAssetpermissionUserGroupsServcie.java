package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermissionUserGroups;

/**
 * @Author baiyi
 * @Date 2020/3/9 12:12 下午
 * @Version 1.0
 */
public interface PermsAssetpermissionUserGroupsServcie {
    PermsAssetpermissionUserGroups queryPermsAssetpermissionUserGroupsByUniqueKey(PermsAssetpermissionUserGroups permsAssetpermissionUserGroups);

    void addPermsAssetpermissionUserGroups(PermsAssetpermissionUserGroups permsAssetpermissionUserGroups);
}
