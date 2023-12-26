package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthResource;
import com.baiyi.opscloud.domain.param.auth.AuthResourceParam;

/**
 * @Author baiyi
 * @Date 2021/5/11 11:19 上午
 * @Version 1.0
 */
public interface AuthResourceService {

    AuthResource queryByName(String resourceName);

    Integer countByGroupId(Integer groupId);

    void deleteById(int id);

    void add(AuthResource authResource);

    void update(AuthResource authResource);

    AuthResource queryById(int id);

    DataTable<AuthResource> queryPageByParam(AuthResourceParam.AuthResourcePageQuery pageQuery);

    DataTable<AuthResource> queryRoleBindResourcePageByParam(AuthResourceParam.RoleBindResourcePageQuery pageQuery);

}