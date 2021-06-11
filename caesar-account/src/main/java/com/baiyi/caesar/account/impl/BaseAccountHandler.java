package com.baiyi.caesar.account.impl;

import com.baiyi.caesar.account.IAccount;
import com.baiyi.caesar.common.datasource.BaseDsInstanceConfig;
import com.baiyi.caesar.datasource.factory.DsFactory;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;
import com.baiyi.caesar.service.datasource.DsAccountService;
import com.baiyi.caesar.service.datasource.DsConfigService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/10 10:31 上午
 * @Version 1.0
 */
public abstract class BaseAccountHandler implements InitializingBean, IAccount {

    private static final String HANDLER_CLASS_NAME_SUFFIX = "AccountHandler";

    @Resource
    protected DsConfigService dsConfigService;

    @Resource
    protected DsFactory dsFactory;

    @Resource
    private DsAccountService dsAccountService;

    protected abstract BaseDsInstanceConfig getConfig(Integer dsConfigId);

    protected abstract List<DatasourceAccount> listAccount(BaseDsInstanceConfig baseDsInstanceConfig);

    protected void doPullAccount(DsInstanceVO.Instance dsInstance) {
        BaseDsInstanceConfig baseDsInstanceConfig = getConfig(dsInstance.getConfigId());
        List<DatasourceAccount> accounts = listAccount(baseDsInstanceConfig);
        accounts.forEach(a -> saveAccount(dsInstance, a));
    }

    private void saveAccount(DsInstanceVO.Instance dsInstance, DatasourceAccount pre) {
        wrap(dsInstance, pre);
        DatasourceAccount account = dsAccountService.getByUniqueKey(pre.getAccountUid(), pre.getAccountId());
        if (account == null) {
            dsAccountService.add(pre);
        } else {
            pre.setUserId(account.getUserId());
            pre.setId(account.getId());
            dsAccountService.update(pre);
        }
    }

    protected abstract void wrap(DsInstanceVO.Instance dsInstance, DatasourceAccount account);

    @Override
    public String getKey() {
        return this.getClass().getSimpleName().replace(HANDLER_CLASS_NAME_SUFFIX, "").toUpperCase();
    }

}
