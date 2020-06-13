package com.baiyi.opscloud.account.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.ldap.entry.Person;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;

/**
 * @Author baiyi
 * @Date 2020/1/15 9:22 上午
 * @Version 1.0
 */
public class OcUserBuilder {

    public static OcUser build(Person person) {
        OcUserBO ocUserBO = OcUserBO.builder()
                .username(person.getUsername())
                .displayName(person.getDisplayName())
                .email(person.getEmail())
                .phone(person.getMobile())
                .source("ldap")
                .build();
        return convert(ocUserBO);
    }

    public static OcUser build(ZabbixUser zabbixUser) {
        OcUserBO ocUserBO = OcUserBO.builder()
                .username(zabbixUser.getAlias())
                .displayName(zabbixUser.getName())
                .source("zabbix")
                .build();
        return convert(ocUserBO);
    }

    private static OcUser convert(OcUserBO ocUserBO){
        return BeanCopierUtils.copyProperties(ocUserBO, OcUser.class);
    }

}
