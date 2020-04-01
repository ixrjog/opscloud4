package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.ServerBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;

/**
 * @Author baiyi
 * @Date 2020/4/1 11:07 上午
 * @Version 1.0
 */
public class ServerBuilder {

    public static OcServer build() {
        ServerBO serverBO = ServerBO.builder()
                .build();
        return covert(serverBO);
    }

    private static OcServer covert( ServerBO serverBO) {
        return BeanCopierUtils.copyProperties(serverBO, OcServer.class);
    }
}
