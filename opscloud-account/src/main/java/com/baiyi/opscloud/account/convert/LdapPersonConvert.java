package com.baiyi.opscloud.account.convert;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.ldap.entry.Person;

/**
 * @Author baiyi
 * @Date 2020/1/3 5:23 下午
 * @Version 1.0
 */
public class LdapPersonConvert {

    public static Person convertOcUser(OcUser ocUser, String password) {
        Person person = new Person();
        person.setUsername(ocUser.getUsername());
        person.setDisplayName(ocUser.getDisplayName());
        person.setEmail(ocUser.getEmail());
        person.setMobile(ocUser.getPhone());
        person.setUserPassword(password);
        person.setIsActive(ocUser.getIsActive()); // 有效账户
        return person;
    }


}
