package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:43 下午
 * @Version 1.0
 */
public interface OcAccountService {

    OcAccount queryOcAccountByAccountId(int accountType, String accountId);

    OcAccount queryOcAccountByUsername(int accountType, String username);

    List<OcAccount> queryOcAccountByAccountType(int accountType);

    void addOcAccount(OcAccount ocAccount);

    OcAccount queryOcAccount(int id);

    void updateOcAccount(OcAccount ocAccount);

    void delOcAccount(int id);

}
