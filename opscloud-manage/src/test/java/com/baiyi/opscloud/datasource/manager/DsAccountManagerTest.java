package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.user.UserService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;


/**
 * @Author baiyi
 * @Date 2021/8/11 3:20 下午
 * @Version 1.0
 */
public class DsAccountManagerTest extends BaseUnit {

    @Resource
    private DsAccountManager dsAccountManager;

    @Resource
    private UserService userService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    void create() {
        User user = buildTestUser();
        dsAccountManager.create(user);
    }

    @Test
    void update() {
        User user = buildTestUser();
        dsAccountManager.update(user);
    }

    @Test
    void delete() {
        User user = buildTestUser();
        dsAccountManager.delete(user);
    }

    private User buildTestUser(){

//
//        return  User.builder()
//                .username("test2021")
//                .email("test202122222@qq.com")
//                .password(stringEncryptor.encrypt("11111111"))
//                .phone("12345678911")
//                .displayName("我是测试账户2222")
//                .build();

        return userService.getByUsername("jinzhengjie");
    }

}