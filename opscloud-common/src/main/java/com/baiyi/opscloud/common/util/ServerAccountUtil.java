package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/31 10:48 上午
 * @Version 1.0
 */
public class ServerAccountUtil {

    private ServerAccountUtil() {
    }

    public static Map<Integer, List<ServerAccount>> catByType(List<ServerAccount> accounts) {
        Map<Integer, List<ServerAccount>> accountTypeMap = Maps.newHashMap();
        accounts.forEach(a -> {
            if (accountTypeMap.containsKey(a.getAccountType())) {
                accountTypeMap.get(a.getAccountType()).add(a);
            } else {
                accountTypeMap.put(a.getAccountType(), Lists.newArrayList(a));
            }
        });
        return accountTypeMap;
    }

    public static Map<Integer, List<ServerAccount>> catByTypeTest(List<ServerAccount> accounts) {
        Map<Integer, List<ServerAccount>> accountTypeMap = Maps.newHashMap();
        accounts.forEach(account -> FunctionUtil.isTureOrFalse(accountTypeMap.containsKey(account.getAccountType()))
                .withBoolean(
                        () -> accountTypeMap.get(account.getAccountType()).add(account),
                        () -> accountTypeMap.put(account.getAccountType(), Lists.newArrayList(account))
                ));
        return accountTypeMap;
    }

}