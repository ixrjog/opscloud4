package com.baiyi.caesar.account.relation;

import com.baiyi.caesar.common.type.AccountRelationTypeEnum;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountRelation;
import com.baiyi.caesar.service.datasource.DsAccountRelationService;
import com.baiyi.caesar.service.datasource.DsAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/15 3:21 下午
 * @Version 1.0
 */
@Service
public class AccountRelationFacade {

    @Resource
    private DsAccountService dsAccountService;

    @Resource
    private DsAccountRelationService dsAccountRelationService;

    public void buildingRelationships(List<String> accountIds, List<DatasourceAccountRelation> relationships, DatasourceAccountGroup group) {
        for (String accountId : accountIds) {
            if (!haveRelation(relationships, accountId)) {
                DatasourceAccount datasourceAccount = dsAccountService.getByUniqueKey(group.getAccountUid(), accountId);
                if (datasourceAccount == null) continue; // 账户不存在
                DatasourceAccountRelation relation = DatasourceAccountRelation.builder()
                        .accountUid(group.getAccountUid())
                        .datasourceAccountId(datasourceAccount.getId())
                        .targetId(group.getId())
                        .relationType(AccountRelationTypeEnum.ACCOUNT_GROUP.getType())
                        .build();
                dsAccountRelationService.add(relation);
            }
        }
        relationships.forEach(e -> dsAccountRelationService.deleteById(e.getId()));
    }

    private boolean haveRelation(List<DatasourceAccountRelation> relationships, String accountId) {
        Iterator<DatasourceAccountRelation> iter = relationships.iterator();
        while (iter.hasNext()) {
            DatasourceAccountRelation relation = iter.next();
            DatasourceAccount account = dsAccountService.getById(relation.getDatasourceAccountId());
            if (account == null) return true;
            if (account.getAccountId().equals(accountId)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }
}
