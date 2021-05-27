package com.baiyi.caesar.service.auth;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.AuthGroup;
import com.baiyi.caesar.domain.param.auth.AuthGroupParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

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
        AuthGroupParam.AuthGroupPageQuery pageQuery = new AuthGroupParam.AuthGroupPageQuery();
        pageQuery.setGroupName("group");
        pageQuery.setPage(1);
        pageQuery.setLength(10);
        DataTable<AuthGroup> table = authGroupService.queryPageByParam(pageQuery);
        log.info(JSON.toJSON(table).toString());
    }
}
