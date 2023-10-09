package com.baiyi.opscloud.datasource.apollo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.apollo.driver.ApolloAppDriver;
import com.ctrip.framework.apollo.openapi.dto.OpenAppDTO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/29 18:14
 * @Version 1.0
 */
public class ApolloTest extends BaseUnit {

    @Resource
    private DsConfigManager dsConfigManager;

    protected ApolloConfig getConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByDsType(DsTypeEnum.APOLLO.getType()), ApolloConfig.class);
    }

    @Test
    public void test() {
       // List<OpenAppDTO> apps = ApolloApplicationDriver.getAuthorizedApps(getConfig().getApollo());
        List<OpenAppDTO> apps = ApolloAppDriver.getAllApps(getConfig().getApollo());
        print(apps);
    }

}