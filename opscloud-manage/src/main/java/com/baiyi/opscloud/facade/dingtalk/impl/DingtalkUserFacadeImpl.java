package com.baiyi.opscloud.facade.dingtalk.impl;

import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.builder.AccountBuilder;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.decorator.dingtalk.DingtalkUserDecorator;
import com.baiyi.opscloud.dingtalk.DingtalkUserApi;
import com.baiyi.opscloud.dingtalk.bo.DingtalkUserBO;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkUserDept;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.opscloud.facade.AccountFacade;
import com.baiyi.opscloud.facade.dingtalk.DingtalkUserFacade;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkUserDeptService;
import com.baiyi.opscloud.service.user.OcAccountService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/15 11:46 上午
 * @Since 1.0
 */

@Component("DingtalkUserFacade")
public class DingtalkUserFacadeImpl implements DingtalkUserFacade {

    private static final String DINGTALK_ACCOUNT_KEY = "DingtalkAccount";

    @Resource
    private AccountFacade accountFacade;

    @Resource
    private OcAccountService ocAccountService;

    @Resource
    private DingtalkUserDecorator dingtalkUserDecorator;

    @Resource
    private DingtalkUserApi dingtalkUserApi;

    @Resource
    private OcDingtalkUserDeptService ocDingtalkUserDeptService;

    @Override
    public DataTable<DingtalkVO.User> queryDingtalkUserPage(DingtalkParam.UserPageQuery pageQuery) {
        pageQuery.setAccountType(AccountType.DINGTALK.getType());
        DataTable<OcAccount> table = accountFacade.queryOcAccountByParam(pageQuery);
        List<DingtalkVO.User> userList = dingtalkUserDecorator.decoratorVOList(table.getData());
        return new DataTable<>(userList, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> syncUser() {
        IAccount account = AccountFactory.getAccountByKey(DINGTALK_ACCOUNT_KEY);
        account.async();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> bindOcUser(DingtalkParam.BindOcUser param) {
        OcAccount ocAccount = ocAccountService.queryOcAccount(param.getAccountId());
        if (ocAccount != null) {
            Integer userId = param.getOcUserId().equals(-1) ? null : param.getOcUserId();
            ocAccount.setUserId(userId);
            ocAccountService.updateOcAccount(ocAccount);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateUser(DingtalkParam.GetUser param) {
        DingtalkUserBO dingtalkUserBO = dingtalkUserApi.refreshUserDetail(param.getUserid(), param.getUid());
        OcAccount ocAccount = AccountBuilder.build(dingtalkUserBO);
        OcAccount oldOcAccount = ocAccountService.queryOcAccountByUsername(AccountType.DINGTALK.getType(), param.getUserid());
        ocAccount.setUserId(oldOcAccount.getUserId());
        ocAccount.setId(oldOcAccount.getId());
        accountFacade.updateOcAccount(ocAccount);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<DingtalkVO.User>> queryDingtalkUserByDingtalkDeptId(Integer ocDingtalkDeptId) {
        List<OcDingtalkUserDept> ocDingtalkUserDeptList = ocDingtalkUserDeptService.queryOcDingtalkUserDeptByDeptId(ocDingtalkDeptId);
        List<OcAccount> accountList = Lists.newArrayListWithCapacity(ocDingtalkUserDeptList.size());
        ocDingtalkUserDeptList.forEach(ocDingtalkUserDept -> {
            OcAccount ocAccount = ocAccountService.queryOcAccountByUsername(AccountType.DINGTALK.getType(), ocDingtalkUserDept.getOcAccountUsername());
            if (ocAccount != null)
                accountList.add(ocAccount);
        });
        List<DingtalkVO.User> userList = dingtalkUserDecorator.decoratorVOList(accountList);
        return new BusinessWrapper<>(userList);
    }
}
