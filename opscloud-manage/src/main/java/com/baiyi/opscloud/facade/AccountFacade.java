package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.param.account.AccountParam;

/**
 * @Author baiyi
 * @Date 2020/12/9 11:05 上午
 * @Version 1.0
 */
public interface AccountFacade {

    DataTable<OcAccount> queryOcAccountByParam(AccountParam.AccountPageQuery pageQuery);

    void updateOcAccount(OcAccount ocAccount);
}
