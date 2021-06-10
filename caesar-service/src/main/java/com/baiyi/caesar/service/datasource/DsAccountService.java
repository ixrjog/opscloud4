package com.baiyi.caesar.service.datasource;

import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;

/**
 * @Author baiyi
 * @Date 2021/6/10 10:09 上午
 * @Version 1.0
 */
public interface DsAccountService {

    void add(DatasourceAccount datasourceAccount);

    void update(DatasourceAccount datasourceAccount);

    DatasourceAccount getByUniqueKey(String accountUid, String accountId);

}
