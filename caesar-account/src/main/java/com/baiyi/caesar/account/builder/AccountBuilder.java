package com.baiyi.caesar.account.builder;

import com.baiyi.caesar.common.base.AccountType;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.ldap.entry.Person;

/**
 * @Author baiyi
 * @Date 2020/1/15 9:22 上午
 * @Version 1.0
 */
public class AccountBuilder {

    public static DatasourceAccount build(Person person) {
        return DatasourceAccount.builder()
                .username(person.getUsername())
                .displayName(person.getDisplayName())
                .email(person.getEmail())
                .phone(person.getMobile())
                .accountType(AccountType.LDAP.getType())
                .build();
    }

//    public static OcUser build(ZabbixUser zabbixUser) {
//        UserBO bo = UserBO.builder()
//                .username(zabbixUser.getAlias())
//                .displayName(zabbixUser.getName())
//                .source("zabbix")
//                .build();
//        return convert(bo);
//    }
//
//    public static OcUser build(AuthConfig.Admin admin) {
//        UserBO bo = UserBO.builder()
//                .username(admin.getUsername())
//                .displayName(admin.getUsername())
//                .source("local")
//                .build();
//        return convert(bo);
//    }
//
//    public static OcUser build(GitlabUser gitlabUser) {
//        UserBO bo = UserBO.builder()
//                .username(gitlabUser.getUsername())
//                .displayName(gitlabUser.getName())
//                .email(gitlabUser.getEmail())
//                .isActive("active".equals(gitlabUser.getState()))
//                .source("gitlab")
//                .build();
//        return convert(bo);
//    }


}
