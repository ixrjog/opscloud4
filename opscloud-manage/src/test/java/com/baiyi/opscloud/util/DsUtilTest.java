package com.baiyi.opscloud.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.common.util.DsUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/17 2:14 下午
 * @Version 1.0
 */
public class DsUtilTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    @Test
    void toConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(1);
        System.err.println(JSON.toJSON(datasourceConfig));
        LdapDsInstanceConfig ldapDsInstanceConfig = DsUtil.toDatasourceConfig(datasourceConfig.getPropsYml(), LdapDsInstanceConfig.class);
        System.err.println(JSON.toJSON(ldapDsInstanceConfig));
    }

    @Test
    void toConfig2() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(1);
        System.err.println(JSON.toJSON(datasourceConfig));
        BaseDsInstanceConfig base = dsFactory.build(datasourceConfig, LdapDsInstanceConfig.class);
        System.err.println(JSON.toJSON(base));
    }
}
