package com.baiyi.opscloud.account;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.service.user.OcUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/8 6:46 下午
 * @Version 1.0
 */
public class LdapAccountTest extends BaseUnit {

    private static final String key = "LdapAccount";

    private IAccount getAccount() {
        return AccountFactory.getAccountByKey(key);
    }

    @Resource
    private OcUserService ocUserService;

    @Test
    void testRsync() {
        getAccount().sync();
    }

    @Test
    void testCreateUser() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("tutu");

        getAccount().create(ocUser );
    }

    @Test
    void testDeleteUser() {
        getAccount().delete(getOcUser());
    }


    /**
     * 更新用户属性
     */
    @Test
    void testUpdateUser() {
        OcUser ocUser = getOcUser();
        String password = PasswordUtils.getPW(16);
        ocUser.setPassword(password);
        System.err.println(password);
        getAccount().update(ocUser);
    }

    private OcUser getOcUser() {
        OcUser user = new OcUser();
        user.setUsername("oc3-test");
        user.setDisplayName("oc3测试用户2");
        user.setEmail("oc3-test2@gegejia.com");
        user.setPhone("13456768043");
        return user;
    }

    /**
     * 更新用户属性
     */
    @Test
    void testIsActive() {
        OcUser user = new OcUser();
        user.setUsername("baiyi");
        user.setIsActive(Boolean.TRUE);
        IAccount iAccount = getAccount();
        iAccount.update(user);
    }


}
