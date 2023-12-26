package com.baiyi.opscloud.sshcore;

import com.baiyi.opscloud.common.constants.enums.ProtocolEnum;
import com.baiyi.opscloud.common.util.ServerAccountUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.service.server.ServerAccountService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/7 3:46 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SshAccountHelper {

    private final ServerAccountService serverAccountService;

    public Map<Integer, List<ServerAccount>> getServerAccountCatMap(Integer serverId) {
        List<ServerAccount> accounts = serverAccountService.getPermissionServerAccountByTypeAndProtocol(serverId, ProtocolEnum.SSH.getType());
        if (CollectionUtils.isEmpty(accounts)) {
            return Maps.newHashMap();
        }
        return ServerAccountUtil.catByType(accounts);
    }

}