package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.param.account.AccountParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:43 下午
 * @Version 1.0
 */
public interface OcAccountService {

    DataTable<OcAccount> queryOcAccountByParam(AccountParam.AccountPageQuery pageQuery);

    OcAccount queryOcAccountByAccountId(int accountType, String accountId);

    OcAccount queryOcAccountByUsername(int accountType, String username);

    OcAccount queryOcAccountByUserId(int accountType, Integer userId);

    List<OcAccount> queryOcAccountByAccountType(int accountType);

    void addOcAccount(OcAccount ocAccount);

    OcAccount queryOcAccount(int id);

    void updateOcAccount(OcAccount ocAccount);

    void delOcAccount(int id);

    Integer countActiveAccount(int accountType, String accountUid);

}
