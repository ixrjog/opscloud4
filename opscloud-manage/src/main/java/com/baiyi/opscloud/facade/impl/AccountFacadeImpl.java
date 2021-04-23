package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.param.account.AccountParam;
import com.baiyi.opscloud.facade.AccountFacade;
import com.baiyi.opscloud.service.user.OcAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/12/9 11:05 上午
 * @Version 1.0
 */
@Service
public class AccountFacadeImpl implements AccountFacade {

    @Resource
    private OcAccountService ocAccountService;

    @Override
    public DataTable<OcAccount> queryOcAccountByParam(AccountParam.AccountPageQuery pageQuery) {
        return ocAccountService.queryOcAccountByParam(pageQuery);
    }

    @Override
    public void updateOcAccount(OcAccount ocAccount) {
        ocAccountService.updateOcAccount(ocAccount);
    }


}
