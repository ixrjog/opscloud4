package com.baiyi.opscloud.account;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.domain.generator.OcUser;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/1/8 6:46 下午
 * @Version 1.0
 */
public class LdapAccountTest extends BaseUnit {

    private static final String key = "LdapAccount";

    @Test
    void testRsync() {
        Account account = AccountFactory.getAccountByKey(key);
        account.sync();
    }

    @Test
    void testCreateUser() {
        Account account = AccountFactory.getAccountByKey(key);
        account.create(getOcUser());
    }

    @Test
    void testDeleteUser() {
        Account account = AccountFactory.getAccountByKey(key);
        account.delete(getOcUser());
    }

    /**
     * 更新用户属性
     */
    @Test
    void testUpdateUser() {
        Account account = AccountFactory.getAccountByKey(key);
        OcUser ocUser = getOcUser();
        String password = PasswordUtils.getPW(16);
        ocUser.setPassword(password);
        System.err.println(password);
        account.update(ocUser);
    }

    private OcUser getOcUser() {
        OcUser user = new OcUser();
        user.setUsername("oc3-test");
        user.setDisplayName("oc3测试用户2");
        user.setEmail("oc3-test2@gegejia.com");
        user.setPhone("13456768043");
        return user;
    }

}
