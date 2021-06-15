package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.domain.param.datasource.DsAccountGroupParam;

/**
 * @Author baiyi
 * @Date 2021/6/15 10:08 上午
 * @Version 1.0
 */
public interface DsAccountGroupService {

    void add(DatasourceAccountGroup datasourceAccountGroup);

    void update(DatasourceAccountGroup datasourceAccountGroup);

    void deleteById(Integer id);

    DatasourceAccountGroup getByUniqueKey(String accountUid, String accountId);

    DataTable<DatasourceAccountGroup> queryPageByParam(DsAccountGroupParam.GroupPageQuery pageQuery);
}
