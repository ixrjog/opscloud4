package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.param.server.ServerAccountParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:15 上午
 * @Version 1.0
 */
public interface ServerAccountService {

    ServerAccount getById(Integer id);

    void add(ServerAccount serverAccount);

    void update(ServerAccount serverAccount);

    DataTable<ServerAccount> queryPageByParam(ServerAccountParam.ServerAccountPageQuery pageQuery);

    List<ServerAccount> getPermissionServerAccountByTypeAndProtocol(Integer serverId, Integer accountType, String protocol);
}
