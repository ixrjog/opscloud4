package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.OcServerGroupPermission;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 9:36 上午
 * @Version 1.0
 */
public interface OcServerGroupPermissionService {
    void addOcServerGroupPermission(OcServerGroupPermission ocServerGroupPermission);

    void delOcServerGroupPermission(int id);

    List<OcServerGroupPermission> queryUserServerGroupPermission(int userId);
}
