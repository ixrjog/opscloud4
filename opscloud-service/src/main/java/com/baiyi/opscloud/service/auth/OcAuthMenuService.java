package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthMenu;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:27 上午
 * @Version 1.0
 */
public interface OcAuthMenuService {

    OcAuthMenu queryOcAuthMenuByRoleId(int roleId, int menuType);

    void addOcAuthMenu(OcAuthMenu ocAuthMenu);

    void updateOcAuthMenu(OcAuthMenu ocAuthMenu);

    void deleteOcAuthMenuById(OcAuthMenu ocAuthMenu);
}
