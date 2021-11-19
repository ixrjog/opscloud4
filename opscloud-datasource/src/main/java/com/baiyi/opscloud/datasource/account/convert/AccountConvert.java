package com.baiyi.opscloud.datasource.account.convert;

import com.baiyi.opscloud.datasource.ldap.entity.Person;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/8/11 2:56 下午
 * @Version 1.0
 */
public class AccountConvert {

    public static Person toLdapPerson(User user){
        return Person.builder()
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .userPassword(user.getPassword())
                .mobile(user.getPhone())
                .build();
    }

    public static com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User toZabbixUser(User user){
        return  com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User.builder()
                .alias(user.getUsername())
                .name(StringUtils.isEmpty(user.getDisplayName())? user.getUsername(): user.getDisplayName())
                .build();
    }
}
