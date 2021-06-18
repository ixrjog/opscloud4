package com.baiyi.caesar.account.impl;

import com.baiyi.caesar.account.IAccountProvider;
import com.baiyi.caesar.account.relation.AccountRelationFacade;
import com.baiyi.caesar.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.caesar.common.type.AccountRelationTypeEnum;
import com.baiyi.caesar.datasource.factory.DsFactory;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountRelation;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;
import com.baiyi.caesar.service.datasource.DsAccountGroupService;
import com.baiyi.caesar.service.datasource.DsAccountRelationService;
import com.baiyi.caesar.service.datasource.DsAccountService;
import com.baiyi.caesar.service.datasource.DsConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/10 10:31 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseAccountProvider implements InitializingBean, IAccountProvider {

    public static final int PASSWORD_LENGTH = 16; // 初始密码长度

    private static final String HANDLER_CLASS_NAME_SUFFIX = "AccountHandler";

    @Resource
    protected DsConfigService dsConfigService;

    @Resource
    protected DsFactory dsFactory;

    @Resource
    private DsAccountService dsAccountService;

    @Resource
    private DsAccountGroupService dsAccountGroupService;

    @Resource
    private DsAccountRelationService dsAccountRelationService;

    @Resource
    private AccountRelationFacade accountRelationFacade;

    protected abstract BaseDsInstanceConfig getConfig(Integer dsConfigId);

    protected abstract List<DatasourceAccount> listAccount(BaseDsInstanceConfig baseDsInstanceConfig);

    protected abstract List<DatasourceAccountGroup> listGroup(BaseDsInstanceConfig baseDsInstanceConfig);

    protected void doPullAccount(DsInstanceVO.Instance dsInstance) {
        BaseDsInstanceConfig baseDsInstanceConfig = getConfig(dsInstance.getConfigId());
        List<DatasourceAccount> accounts = listAccount(baseDsInstanceConfig);
        accounts.forEach(a -> saveAccount(dsInstance, a));
    }

    protected void doPullGroup(DsInstanceVO.Instance dsInstance) {
        BaseDsInstanceConfig baseDsInstanceConfig = getConfig(dsInstance.getConfigId());
        List<DatasourceAccountGroup> groups = listGroup(baseDsInstanceConfig);
        groups.forEach(g -> saveGroup(dsInstance, baseDsInstanceConfig, g));
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

    protected abstract List<String> listGroupRelationships(BaseDsInstanceConfig baseDsInstanceConfig, String groupName);

    private void buildingRelationships(BaseDsInstanceConfig baseDsInstanceConfig, DatasourceAccountGroup group) {
        List<String> accountIds = listGroupRelationships(baseDsInstanceConfig, group.getAccountId());
        DatasourceAccountRelation query = DatasourceAccountRelation.builder()
                .relationType(AccountRelationTypeEnum.ACCOUNT_GROUP.getType())
                .targetId(group.getId())
                .accountUid(group.getAccountUid())
                .build();
        List<DatasourceAccountRelation> relationships = dsAccountRelationService.queryRelationshipsByTarget(query);
        // 建立关系
        log.info("建立账户关系, groupAccountId = {}",group.getAccountId());
        accountRelationFacade.buildingRelationships(accountIds,relationships,group);
    }

    private void saveGroup(DsInstanceVO.Instance dsInstance, BaseDsInstanceConfig baseDsInstanceConfig, DatasourceAccountGroup pre) {
        wrap(dsInstance, pre);
        DatasourceAccountGroup group = dsAccountGroupService.getByUniqueKey(pre.getAccountUid(), pre.getAccountId());
        if (group == null) {
            dsAccountGroupService.add(pre);
            group = pre;
        }
        buildingRelationships(baseDsInstanceConfig, group);
    }

    protected abstract void wrap(DsInstanceVO.Instance dsInstance, DatasourceAccount account);

    protected abstract void wrap(DsInstanceVO.Instance dsInstance, DatasourceAccountGroup group);


    @Override
    public String getKey() {
        return this.getClass().getSimpleName().replace(HANDLER_CLASS_NAME_SUFFIX, "").toUpperCase();
    }

}
