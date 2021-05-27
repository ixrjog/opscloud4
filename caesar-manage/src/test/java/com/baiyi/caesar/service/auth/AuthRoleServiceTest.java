package com.baiyi.caesar.service.auth;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.AuthRole;
import com.baiyi.caesar.domain.param.auth.AuthRoleParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/12 9:56 上午
 * @Version 1.0
 */
@Slf4j
public class AuthRoleServiceTest extends BaseUnit {

    @Resource
    private AuthRoleService authRoleService;

    @Test
    void queryPageTest() {
        AuthRoleParam.AuthRolePageQuery pageQuery = new AuthRoleParam.AuthRolePageQuery();
        pageQuery.setRoleName("d");
        pageQuery.setPage(1);
        pageQuery.setLength(10);
        DataTable<AuthRole> table = authRoleService.queryPageByParam(pageQuery);
        log.info(JSON.toJSON(table).toString());
    }
}
