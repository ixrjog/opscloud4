package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.AuthGroup;
import com.baiyi.caesar.domain.param.auth.AuthGroupParam;


/**
 * @Author baiyi
 * @Date 2021/5/11 10:08 上午
 * @Version 1.0
 */
public interface AuthGroupService {

    AuthGroup getById(int id);

    DataTable<AuthGroup> queryPageByParam(AuthGroupParam.AuthGroupPageQuery pageQuery);

    void add(AuthGroup authGroup);

    void update(AuthGroup authGroup);

    void deleteById(int id);
}
