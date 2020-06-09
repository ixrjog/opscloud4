package com.baiyi.opscloud.cloud.ram;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 11:33 上午
 * @Version 1.0
 */
public class AliyunRAMTest extends BaseUnit {

    @Resource
    private AliyunRAM aliyunRAM;

    @Resource
    private AliyunCore aliyunCore;

    @Test
    void testListUsers() {
        aliyunCore.getAccounts().forEach(e -> {
            List<ListUsersResponse.User> users = aliyunRAM.getUsers(e);
            System.err.println(JSON.toJSONString(users));
        });

    }

}
