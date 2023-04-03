package com.baiyi.opscloud.datasource.dingtalk;

import com.baiyi.opscloud.datasource.dingtalk.base.BaseDingtalkTest;
import com.baiyi.opscloud.datasource.dingtalk.driver.DingtalkUserDriver;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkUser;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkUserParam;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/29 4:46 下午
 * @Version 1.0
 */
public class DingtalkUserTest extends BaseDingtalkTest {

    @Resource
    private DingtalkUserDriver dingtalkUserDrive;

    @Test
    void listUserTest() {
        DingtalkUserParam.QueryUserPage queryUserPage = DingtalkUserParam.QueryUserPage.builder().build();
        print(queryUserPage);
        DingtalkUser.UserResponse userResponse = dingtalkUserDrive.list(getConfig().getDingtalk(), queryUserPage);
        print(userResponse);
    }

}
