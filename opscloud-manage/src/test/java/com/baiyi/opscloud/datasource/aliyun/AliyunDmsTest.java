package com.baiyi.opscloud.datasource.aliyun;

import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.aliyun.dms.driver.AliyunDmsUserDriver;
import com.baiyi.opscloud.datasource.aliyun.dms.entity.DmsUser;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/16 3:55 PM
 * @Version 1.0
 */
public class AliyunDmsTest extends BaseAliyunTest {

    @Test
    void listUserTest() {
        AliyunConfig config = getConfig();
        try {
            List<DmsUser.User> users = AliyunDmsUserDriver.listUser(config.getAliyun());
            System.out.println(users);
        } catch (Exception e) {
        }
    }

}
