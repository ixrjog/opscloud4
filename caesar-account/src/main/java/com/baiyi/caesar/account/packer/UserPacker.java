package com.baiyi.caesar.account.packer;

import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.ldap.entry.Person;

/**
 * @Author baiyi
 * @Date 2021/6/16 9:55 上午
 * @Version 1.0
 */
public class UserPacker {

    private UserPacker() {
    }

    public static Person toPerson(User user) {
        return Person.builder()
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .mobile(user.getPhone())
                .userPassword(user.getPassword())
                .build();
    }
}
