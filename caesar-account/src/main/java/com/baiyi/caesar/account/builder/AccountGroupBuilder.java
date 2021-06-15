package com.baiyi.caesar.account.builder;

import com.baiyi.caesar.common.base.AccountType;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.ldap.entry.Group;

/**
 * @Author baiyi
 * @Date 2021/6/15 10:59 上午
 * @Version 1.0
 */
public class AccountGroupBuilder {

    public static DatasourceAccountGroup build(Group group) {
        return DatasourceAccountGroup.builder()
                .name(group.getGroupName())
                .displayName(group.getGroupName())
                .accountType(AccountType.LDAP.getType())
                .build();
    }
}
