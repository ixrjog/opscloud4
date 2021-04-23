package com.baiyi.opscloud.account.impl;

import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.builder.AccountBuilder;
import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.dingtalk.DingtalkUserApi;
import com.baiyi.opscloud.dingtalk.bo.DingtalkUserBO;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkDeptService;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkUserDeptService;
import com.baiyi.opscloud.service.user.OcAccountService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.service.user.OcUserToBeRetiredService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 5:50 下午
 * @Since 1.0
 */
@Slf4j
@Component("DingtalkAccount")
public class DingtalkAccount extends BaseAccount implements IAccount {

    @Resource
    private DingtalkUserApi dingtalkUserApi;

    @Resource
    private OcDingtalkDeptService ocDingtalkDeptService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcAccountService ocAccountService;

    @Resource
    private OcUserToBeRetiredService ocUserToBeRetiredService;

    @Resource
    private OcDingtalkUserDeptService ocDingtalkUserDeptService;

    @Override
    protected int getAccountType() {
        return AccountType.DINGTALK.getType();
    }

    @Override
    protected List<OcUser> getUserList() {
        return null;
    }

    @Override
    protected List<OcAccount> getOcAccountList() {
        List<OcDingtalkDept> dingtalkDeptList = ocDingtalkDeptService.queryOcDingtalkDeptAll();
        List<OcAccount> ocAccountList = Lists.newArrayList();
        dingtalkDeptList.forEach(ocDingtalkDept -> {
            DingtalkParam.QueryByDeptId param = new DingtalkParam.QueryByDeptId();
            param.setUid(ocDingtalkDept.getDingtalkUid());
            param.setDeptId(ocDingtalkDept.getDeptId());
            List<DingtalkUserBO> dingtalkUserBOList = dingtalkUserApi.getDeptUserList(param);
            if (!CollectionUtils.isEmpty(dingtalkUserBOList)) {
                List<OcAccount> accountList = dingtalkUserBOList.stream().map(AccountBuilder::build).collect(Collectors.toList());
                ocAccountList.addAll(accountList);
                try {
                    Set<String> accountUsernameList = accountList.stream().map(OcAccount::getUsername).collect(Collectors.toSet());
                    List<OcDingtalkUserDept> dingtalkUserDeptList = buildDingtalkUserDeptList(ocDingtalkDept.getId(), accountUsernameList);
                    ocDingtalkUserDeptService.delOcDingtalkUserDeptByDeptId(ocDingtalkDept.getId());
                    ocDingtalkUserDeptService.addOcDingtalkUserDeptList(dingtalkUserDeptList);
                } catch (Exception e) {
                    log.error("插入OcDingtalkUserDeptList失败，OcDingtalkDept={}", ocDingtalkDept.getId(), e);
                }
            }
        });
        return ocAccountList;
    }

    private List<OcDingtalkUserDept> buildDingtalkUserDeptList(Integer ocDingtalkDeptId, Set<String> accountUsernameList) {
        List<OcDingtalkUserDept> dingtalkUserDeptList = Lists.newArrayListWithCapacity(accountUsernameList.size());
        accountUsernameList.forEach(accountUsername -> {
            OcDingtalkUserDept ocDingtalkUserDept = new OcDingtalkUserDept();
            ocDingtalkUserDept.setOcDingtalkDeptId(ocDingtalkDeptId);
            ocDingtalkUserDept.setOcAccountUsername(accountUsername);
            dingtalkUserDeptList.add(ocDingtalkUserDept);
        });
        return dingtalkUserDeptList;
    }

    @Override
    public BusinessWrapper<Boolean> delete(OcUser user) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> create(OcUser user) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> active(OcUser user, boolean active) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    protected void saveOcAccount(OcAccount preOcAccount, Map<String, OcAccount> map) {
        if (map.containsKey(preOcAccount.getAccountId())) {
            OcAccount account = map.get(preOcAccount.getAccountId());
            updateOcAccount(bindAccount(preOcAccount), account);
            map.remove(preOcAccount.getAccountId());
        } else {
            try {
                ocAccountService.addOcAccount(bindAccount(preOcAccount));
            } catch (Exception e) {
                log.error("插入失败，user={}", preOcAccount.getDisplayName(), e);
            }
        }
    }

    private OcAccount bindAccount(OcAccount ocAccount) {
        List<OcUser> ocUserList = ocUserService.queryOcUserByPhone(ocAccount.getPhone());
        if (ocUserList.size() == 1) {
            ocAccount.setUserId(ocUserList.get(0).getId());
            return ocAccount;
        }
        return ocAccount;
    }

    @Override
    protected void delAccountByMap(Map<String, OcAccount> accountMap) {
        if (accountMap.isEmpty()) return;
        accountMap.forEach((k, v) -> {
            v.setIsActive(false);
            ocAccountService.updateOcAccount(v);
            if (v.getUserId() != null) {
                OcUser ocUser = ocUserService.queryOcUserById(v.getUserId());
                if (ocUser.getIsActive()) {
                    OcUserToBeRetired userToBeResigned = new OcUserToBeRetired();
                    userToBeResigned.setUserId(v.getUserId());
                    try {
                        ocUserToBeRetiredService.addOcUserToBeRetired(userToBeResigned);
                    } catch (Exception e) {
                        log.error("插入待离职表失败,displayName:{}", v.getDisplayName(), e);
                    }
                }
            }
        });
    }
}
