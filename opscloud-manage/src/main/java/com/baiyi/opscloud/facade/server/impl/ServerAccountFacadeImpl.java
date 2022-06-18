package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccountPermission;
import com.baiyi.opscloud.domain.param.server.ServerAccountParam;
import com.baiyi.opscloud.domain.vo.server.ServerAccountVO;
import com.baiyi.opscloud.facade.server.ServerAccountFacade;
import com.baiyi.opscloud.packer.server.ServerAccountPacker;
import com.baiyi.opscloud.service.server.ServerAccountPermissionService;
import com.baiyi.opscloud.service.server.ServerAccountService;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:26 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ServerAccountFacadeImpl implements ServerAccountFacade {

    private final ServerAccountService accountService;

    private final ServerAccountPermissionService accountPermissionService;

    private final ServerAccountPacker accountPacker;

    private final CredentialService credentialService;

    @Override
    public DataTable<ServerAccountVO.Account> queryServerAccountPage(ServerAccountParam.ServerAccountPageQuery pageQuery) {
        DataTable<ServerAccount> table = accountService.queryPageByParam(pageQuery);
        List<ServerAccountVO.Account> data = BeanCopierUtil.copyListProperties(table.getData(), ServerAccountVO.Account.class).stream()
                .peek(e -> accountPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addServerAccount(ServerAccountVO.Account account) {
        ServerAccount serverAccount = toServerAccount(account);
        try {
            accountService.add(serverAccount);
        } catch (Exception e) {
            throw new CommonRuntimeException("新增账户错误: 请确认账户名称和类型是否重复！");
        }
    }

    @Override
    public void updateServerAccount(ServerAccountVO.Account account) {
        ServerAccount serverAccount = toServerAccount(account);
        accountService.update(serverAccount);
    }

    private ServerAccount toServerAccount(ServerAccountVO.Account account) {
        ServerAccount serverAccount = BeanCopierUtil.copyProperties(account, ServerAccount.class);
        if (StringUtils.isEmpty(serverAccount.getUsername())) {
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            serverAccount.setUsername(credential.getUsername());
        }
        return serverAccount;
    }

    @Override
    public void updateServerAccountPermission(ServerAccountParam.UpdateServerAccountPermission updatePermission) {
        List<ServerAccountPermission> permissions = accountPermissionService.queryByServerId(updatePermission.getServerId());
        updatePermission.getAccountIds().forEach(id -> {
            if (!isAuthorized(permissions, id)) {
                ServerAccountPermission permission = ServerAccountPermission.builder()
                        .serverAccountId(id)
                        .serverId(updatePermission.getServerId())
                        .build();
                accountPermissionService.add(permission);
            }
        });
        permissions.forEach(e -> accountPermissionService.deleteById(e.getId()));
    }

    private boolean isAuthorized(List<ServerAccountPermission> permissions, Integer accountId) {
        Iterator<ServerAccountPermission> iter = permissions.iterator();
        while (iter.hasNext()) {
            ServerAccountPermission permission = iter.next();
            if (permission.getServerAccountId().equals(accountId)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteServerAccountById(Integer id) {
        if (accountPermissionService.countByServerAccountId(id) != 0) {
            throw new CommonRuntimeException("服务器账户删除错误: 账户使用中！");
        }
        accountService.deleteById(id);
    }

}
