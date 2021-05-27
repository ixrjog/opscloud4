package com.baiyi.caesar.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.datasource.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.common.util.DsUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.factory.DsFactory;
import com.baiyi.caesar.service.datasource.DsConfigService;
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
    private DsFactory dsFactory;

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
