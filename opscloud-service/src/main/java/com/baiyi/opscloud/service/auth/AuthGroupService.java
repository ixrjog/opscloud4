package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthGroup;
import com.baiyi.opscloud.domain.param.auth.AuthGroupParam;


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