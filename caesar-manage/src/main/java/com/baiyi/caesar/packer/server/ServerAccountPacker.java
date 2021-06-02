package com.baiyi.caesar.packer.server;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.packer.sys.CredentialPacker;
import com.baiyi.caesar.service.server.ServerAccountPermissionService;
import com.baiyi.caesar.service.server.ServerAccountService;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.domain.vo.server.ServerAccountVO;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:36 上午
 * @Version 1.0
 */
@Component
public class ServerAccountPacker {

    @Resource
    private CredentialPacker credentialPacker;

    @Resource
    private ServerAccountPermissionService accountPermissionService;

    @Resource
    private ServerAccountService accountService;

    public void wrap(ServerAccountVO.IAccount iAccount) {
        List<ServerAccountVO.Account> accounts = accountPermissionService.queryByServerId(iAccount.getServerId()).stream().map(e -> {
            ServerAccount serverAccount = accountService.getById(e.getServerAccountId());
            ServerAccountVO.Account account = BeanCopierUtil.copyProperties(serverAccount, ServerAccountVO.Account.class);
            wrap(account);
            return account;
        }).collect(Collectors.toList());
        iAccount.setAccounts(accounts);
    }

    public List<ServerAccountVO.Account> wrapVOList(List<ServerAccount> data) {
        return BeanCopierUtil.copyListProperties(data, ServerAccountVO.Account.class);
    }

    public List<ServerAccountVO.Account> wrapVOList(List<ServerAccount> data, IExtend iExtend) {
        List<ServerAccountVO.Account> voList = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;
        return voList.stream().peek(this::wrap).collect(Collectors.toList());
    }

    public void wrap(ServerAccountVO.Account account) {
        account.setDisplayName(Joiner.on("").join(account.getUsername(), "[", account.getProtocol(), "]"));
        credentialPacker.wrap(account);
        account.setServerSize(accountPermissionService.countByServerAccountId(account.getId()));
    }
}
