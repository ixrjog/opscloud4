package com.baiyi.caesar.packer.datasource;

import com.baiyi.caesar.common.type.AccountRelationTypeEnum;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountRelation;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.vo.datasource.DsAccountGroupVO;
import com.baiyi.caesar.domain.vo.datasource.DsAccountVO;
import com.baiyi.caesar.service.datasource.DsAccountRelationService;
import com.baiyi.caesar.service.datasource.DsAccountService;
import com.baiyi.caesar.util.ExtendUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/15 11:26 上午
 * @Version 1.0
 */
@Component
public class DsAccountGroupPacker {

    @Resource
    private DsAccountService dsAccountService;

    @Resource
    private DsAccountRelationService dsAccountRelationService;

    public List<DsAccountGroupVO.Group> wrapVOList(List<DatasourceAccountGroup> data, IExtend iExtend) {
        List<DsAccountGroupVO.Group> voList = BeanCopierUtil.copyListProperties(data, DsAccountGroupVO.Group.class);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;
        voList.forEach(e ->
                wrap(e)
        );
        return voList;
    }

    public void wrap(DsAccountGroupVO.Group group) {
        DatasourceAccountRelation relation = DatasourceAccountRelation.builder()
                .relationType(AccountRelationTypeEnum.ACCOUNT_GROUP.getType())
                .accountUid(group.getAccountUid())
                .targetId(group.getId())
                .build();
        List<DatasourceAccountRelation> relations = dsAccountRelationService.queryRelationshipsByTarget(relation);
        List<DsAccountVO.Account> accounts = relations.stream().map(e -> {
            DatasourceAccount datasourceAccount = dsAccountService.getById(e.getDatasourceAccountId());
            return BeanCopierUtil.copyProperties(datasourceAccount, DsAccountVO.Account.class);
        }).collect(Collectors.toList());
        group.setAccounts(accounts);
    }
}
