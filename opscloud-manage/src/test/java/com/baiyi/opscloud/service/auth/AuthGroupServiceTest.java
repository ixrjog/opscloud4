package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthGroup;
import com.baiyi.opscloud.domain.param.auth.AuthGroupParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/11 10:26 上午
 * @Version 1.0
 */
@Slf4j
public class AuthGroupServiceTest extends BaseUnit {

    @Resource
    private AuthGroupService authGroupService;

    @Test
    void queryPageTest() {
        AuthGroupParam.AuthGroupPageQuery pageQuery = AuthGroupParam.AuthGroupPageQuery.builder()
                .groupName("group")
                .page(1)
                .length(10)
                .build();
        DataTable<AuthGroup> table = authGroupService.queryPageByParam(pageQuery);
    }
}
