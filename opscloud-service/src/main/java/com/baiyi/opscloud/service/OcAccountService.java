package com.baiyi.opscloud.service;

import com.baiyi.opscloud.domain.generator.OcAccount;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:43 下午
 * @Version 1.0
 */
public interface OcAccountService {

    OcAccount queryOcAccountByAccountId(int accountType, String accountId);

    List<OcAccount> queryOcAccountByAccountType(int accountType);

    void addOcAccount(OcAccount ocAccount);

    OcAccount queryOcAccount(int id);

    void updateOcAccount(OcAccount ocAccount);

    void delOcAccount(int id);

}
