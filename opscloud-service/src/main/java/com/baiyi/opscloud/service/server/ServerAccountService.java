package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.param.server.ServerAccountParam;
import com.baiyi.opscloud.factory.credential.base.ICredentialCustomer;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:15 上午
 * @Version 1.0
 */
public interface ServerAccountService extends ICredentialCustomer {

    ServerAccount getById(Integer id);

    void add(ServerAccount serverAccount);

    void update(ServerAccount serverAccount);

    DataTable<ServerAccount> queryPageByParam(ServerAccountParam.ServerAccountPageQuery pageQuery);

    List<ServerAccount> getPermissionServerAccountByTypeAndProtocol(Integer serverId, Integer accountType, String protocol);

    default List<ServerAccount> getPermissionServerAccountByTypeAndProtocol(Integer serverId, String protocol) {
        return getPermissionServerAccountByTypeAndProtocol(serverId, null, protocol);
    }

    ServerAccount getPermissionServerAccountByUsernameAndProtocol(Integer serverId,
                                                                  String username,
                                                                  String protocol);

    void deleteById(Integer id);

}