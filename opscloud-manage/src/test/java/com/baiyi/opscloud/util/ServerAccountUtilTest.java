package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.ServerAccountUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/9/28 15:56
 * @Version 1.0
 */
public class ServerAccountUtilTest extends BaseUnit {

    @Test
    void dddTest() {
        List<ServerAccount> accounts = Lists.newArrayList();
        ServerAccount sa1 = ServerAccount.builder()
                .accountType(0)
                .username("0AAAAAAA")
                .build();
        accounts.add(sa1);

        ServerAccount sa2 = ServerAccount.builder()
                .accountType(0)
                .username("0BBBBBB")
                .build();
        accounts.add(sa2);

        ServerAccount sa3 = ServerAccount.builder()
                .accountType(1)
                .username("1CCCCCC")
                .build();
        accounts.add(sa3);
        Map<Integer, List<ServerAccount>> map = ServerAccountUtil.catByTypeTest(accounts);
        print(map);
    }

}
