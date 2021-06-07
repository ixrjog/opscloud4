package com.baiyi.caesar.sshcore.account;

import com.baiyi.caesar.common.type.ProtocolEnum;
import com.baiyi.caesar.common.util.ServerAccoutUtil;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.service.server.ServerAccountService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/7 3:46 下午
 * @Version 1.0
 */
@Component
public class SshAccount {

    @Resource
    private ServerAccountService serverAccountService;

    public Map<Integer, List<ServerAccount>> getServerAccountCatMap(Integer serverId) {
        List<ServerAccount> accounts = serverAccountService.getPermissionServerAccountByTypeAndProtocol(serverId, null, ProtocolEnum.SSH.getType());
        if (CollectionUtils.isEmpty(accounts)) return null;
        return ServerAccoutUtil.catByType(accounts);
    }
}
