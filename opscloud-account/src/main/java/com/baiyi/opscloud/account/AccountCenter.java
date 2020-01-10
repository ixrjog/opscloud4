package com.baiyi.opscloud.account;

import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.domain.generator.OcUser;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 8:06 下午
 * @Version 1.0
 */
@Component("AccountCenter")
public class AccountCenter {

    public static final String LDAP_ACCOUNT_KEY = "LdapAccount";

    public Boolean create(String key, OcUser user) {
        Account account = AccountFactory.getAccountByKey(key);
        return account.create(user);
    }

    public Boolean create(OcUser user) {
        Boolean result = create(LDAP_ACCOUNT_KEY, user);
        if (result) {
            Map<String, Account> accountContainer = AccountFactory.getAccountContainer();
            for (String key : accountContainer.keySet()) {
                if (key.equals(LDAP_ACCOUNT_KEY)) continue;
                Account account = accountContainer.get(key);
                if (!account.create(user))
                    return Boolean.FALSE;
            }

        }
        return Boolean.TRUE;
    }

}
