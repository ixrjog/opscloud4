package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ServerAccountMapper extends Mapper<ServerAccount> {

    List<ServerAccount> getPermissionServerAccountByTypeAndProtocol(@Param("serverId") Integer serverId,
                                                                    @Param("accountType") Integer accountType,
                                                                    @Param("protocol") String protocol);

    ServerAccount getPermissionServerAccountByUsernameAndProtocol(@Param("serverId") Integer serverId,
                                             @Param("username") String username,
                                             @Param("protocol") String protocol);
}