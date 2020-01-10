package com.baiyi.opscloud.service;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.OcServerGroupPermission;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 9:56 上午
 * @Version 1.0
 */
public class OcServerGroupPermissionServiceTest extends BaseUnit {

    @Resource
    private OcServerGroupPermissionService ocServerGroupPermissionService;

    @Test
    void testQueryUserServerGroupPermission() {
        List<OcServerGroupPermission> list = ocServerGroupPermissionService.queryUserServerGroupPermission(11);
        System.err.println(JSON.toJSONString(list));
    }

}
