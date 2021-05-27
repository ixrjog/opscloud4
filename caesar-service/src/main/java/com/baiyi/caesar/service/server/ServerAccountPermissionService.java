package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.generator.caesar.ServerAccountPermission;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/26 2:18 下午
 * @Version 1.0
 */
public interface ServerAccountPermissionService {

    void add(ServerAccountPermission permission);

    void deleteById(Integer id);

    List<ServerAccountPermission> queryByServerId(Integer serverId);

   int  countByServerAccountId(Integer serverAccountId);
}
