package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.opscloud.ServerAccountPermission;

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