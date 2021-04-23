package com.baiyi.opscloud.tencent;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.tencent.ExmailParam;
import com.baiyi.opscloud.tencent.exmail.handler.TencentExmailTokenHandler;
import com.baiyi.opscloud.tencent.exmail.handler.TencentExmailUserHandler;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 2:02 下午
 * @Since 1.0
 */
public class TencentExmailTest extends BaseUnit {

    @Resource
    private TencentExmailTokenHandler tencentExmailTokenHandler;

    @Resource
    private TencentExmailUserHandler tencentExmailUserHandler;

    @Test
    void test1() {
        tencentExmailTokenHandler.getToken();
    }

    @Test
    void test2() {
        tencentExmailUserHandler.deleteUser("xiuyuantest@xinc818.group");
    }

    @Test
    void test3() {
        ExmailParam.User param = new ExmailParam.User();
        param.setUserid("xiuyuantest@xinc818.group");
        param.setName("修远测试邮箱");
        param.setMobile("15067126666");
        param.setDepartment(Lists.newArrayList(1));
        param.setEnable(0);
        tencentExmailUserHandler.updateUser(param);
    }
}
