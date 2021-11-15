package com.baiyi.opscloud.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.common.util.DsUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
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
    private DsConfigHelper dsFactory;

    @Test
    void toConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(1);
        System.err.println(JSON.toJSON(datasourceConfig));
        LdapConfig ldapDsInstanceConfig = DsUtil.toDatasourceConfig(datasourceConfig.getPropsYml(), LdapConfig.class);
        System.err.println(JSON.toJSON(ldapDsInstanceConfig));
    }

    @Test
    void toConfig2() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(1);
        System.err.println(JSON.toJSON(datasourceConfig));
        BaseConfig base = dsFactory.build(datasourceConfig, LdapConfig.class);
        System.err.println(JSON.toJSON(base));
    }
}
