package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.common.util.DsUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:14 下午
 * @Version 1.0
 */
public class DsUtilTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigManager dsFactory;

    @Test
    void toConfig() {
        DatasourceConfig dsConfig = dsConfigService.getById(1);
        print(dsConfig);
        LdapConfig ldapDsInstanceConfig = DsUtil.toDsConfig(dsConfig.getPropsYml(), LdapConfig.class);
        print(ldapDsInstanceConfig);

    }

    @Test
    void toConfig2() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(1);
        print(datasourceConfig);
        BaseDsConfig base = dsFactory.build(datasourceConfig, LdapConfig.class);
        print(base);
    }
}
