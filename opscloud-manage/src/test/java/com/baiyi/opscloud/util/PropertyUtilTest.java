package com.baiyi.opscloud.util;

import com.baiyi.opscloud.domain.model.property.ServerProperty;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/8/25 11:57 上午
 * @Version 1.0
 */
public class PropertyUtilTest {

    @Test
    void optionalTest() {
        ServerProperty.Server property = ServerProperty.Server.builder()
                .build();
        String manageIp = Optional.ofNullable(property)
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getManageIp)
                .orElse("1.1.1.1");
        System.err.print(manageIp);
    }
}
