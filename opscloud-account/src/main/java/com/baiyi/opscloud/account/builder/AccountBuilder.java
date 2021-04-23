package com.baiyi.opscloud.account.builder;

import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.account.bo.AccountBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.dingtalk.bo.DingtalkUserBO;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import org.gitlab.api.models.GitlabUser;


/**
 * @Author baiyi
 * @Date 2019/11/27 4:30 PM
 * @Version 1.0
 */
public class AccountBuilder {

    public static OcAccount build(ZabbixUser user) {
        AccountBO bo = AccountBO.builder()
                .accountId(user.getUserid())
                .username(user.getAlias())
                .displayName(user.getName())
                .isActive(true)
                .accountType(AccountType.ZABBIX.getType())
                .build();
        return convert(bo);
    }

    public static OcAccount build(GitlabUser gitlabUser) {
        AccountBO bo = AccountBO.builder()
                .accountId(gitlabUser.getId().toString())
                .username(gitlabUser.getUsername())
                .displayName(gitlabUser.getName())
                .email(gitlabUser.getEmail())
                .isActive("active".equals(gitlabUser.getState()))
                .accountType(AccountType.GITLAB.getType())
                .build();
        return convert(bo);
    }

    public static OcAccount build(DingtalkUserBO dingtalkUser) {
        AccountBO bo = AccountBO.builder()
                .accountUid(dingtalkUser.getUid())
                .accountId(dingtalkUser.getUnionid())
                .username(dingtalkUser.getUserid())
                .displayName(dingtalkUser.getName())
                .email(dingtalkUser.getEmail())
                .isActive(dingtalkUser.getActive())
                .phone(dingtalkUser.getMobile())
                .accountType(AccountType.DINGTALK.getType())
                .comment(dingtalkUser.getExtension())
                .build();
        return convert(bo);
    }

    private static OcAccount convert(AccountBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcAccount.class);
    }

}
