package com.baiyi.opscloud.account.builder;

import com.baiyi.opscloud.account.base.AccountType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;


/**
 * @Author baiyi
 * @Date 2019/11/27 4:30 PM
 * @Version 1.0
 */
public class OcAccountBuilder {

    public static OcAccount build(ZabbixUser user) {
        OcAccountBO ocAccountBO = OcAccountBO.builder()
                .accountId(user.getUserid())
                .username(user.getAlias())
                .displayName(user.getName())
                .isActive(true)
                .accountType(AccountType.ZABBIX.getType())
                .build();
        return convert(ocAccountBO);
    }

    private static OcAccount convert(OcAccountBO ocAccountBO) {
        return BeanCopierUtils.copyProperties(ocAccountBO, OcAccount.class);
    }

}
