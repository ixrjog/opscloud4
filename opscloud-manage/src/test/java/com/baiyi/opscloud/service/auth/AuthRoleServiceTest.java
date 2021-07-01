package com.baiyi.opscloud.service.auth;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRole;
import com.baiyi.opscloud.domain.param.auth.AuthRoleParam;
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
