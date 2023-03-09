package com.baiyi.opscloud.packer.server;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.server.ServerAccountVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.sys.CredentialPacker;
import com.baiyi.opscloud.service.server.ServerAccountPermissionService;
import com.baiyi.opscloud.service.server.ServerAccountService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:36 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServerAccountPacker implements IWrapper<ServerAccountVO.Account> {

    private final CredentialPacker credentialPacker;

    private final ServerAccountPermissionService accountPermissionService;

    private final ServerAccountService accountService;

    public void wrap(ServerAccountVO.IAccount iAccount) {
        List<ServerAccountVO.Account> accounts = accountPermissionService.queryByServerId(iAccount.getServerId()).stream().map(e -> {
            ServerAccount serverAccount = accountService.getById(e.getServerAccountId());
            ServerAccountVO.Account account = BeanCopierUtil.copyProperties(serverAccount, ServerAccountVO.Account.class);
            wrap(account, SimpleExtend.EXTEND);
            return account;
        }).collect(Collectors.toList());
        iAccount.setAccounts(accounts);
    }

    @Override
    public void wrap(ServerAccountVO.Account account, IExtend iExtend) {
        if (account == null) {
            return;
        }
        String displayName = Joiner.on("").skipNulls().join( "[", account.getProtocol(), "|",account.getUsername(),"]",account.getComment());
        account.setDisplayName(displayName);
                credentialPacker.wrap(account);
        account.setServerSize(accountPermissionService.countByServerAccountId(account.getId()));
    }

}
