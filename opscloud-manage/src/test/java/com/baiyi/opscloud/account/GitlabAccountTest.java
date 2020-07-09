package com.baiyi.opscloud.account;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.gitlab.GitlabUserCenter;
import com.baiyi.opscloud.gitlab.handler.GitlabUserHandler;
import com.baiyi.opscloud.service.user.OcUserService;
import org.gitlab.api.models.GitlabUser;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/22 11:26 上午
 * @Version 1.0
 */
public class GitlabAccountTest extends BaseUnit {

    private static final String key = "GitlabAccount";

    @Resource
    private GitlabUserHandler gitlabUserHandler;

    @Resource
    private GitlabUserCenter gitlabUserCenter;

    @Resource
    private OcUserService ocUserService;

    @Test
    void testGetUsers() {
        List<GitlabUser> users = gitlabUserHandler.getUsers();
        System.err.println(users);
    }

    private IAccount getAccount() {
        return AccountFactory.getAccountByKey(key);
    }



    @Test
    void testPushKey() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("baiyi");
        getAccount().pushSSHKey(ocUser);
    }

    @Test
    void testRsync() {
        getAccount().sync();
    }
}
