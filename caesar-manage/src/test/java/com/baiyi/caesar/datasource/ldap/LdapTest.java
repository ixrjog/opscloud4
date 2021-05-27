package com.baiyi.caesar.datasource.ldap;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.datasource.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.model.Authorization;
import com.baiyi.caesar.factory.DsFactory;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.handler.LdapHandler;
import com.baiyi.caesar.service.datasource.DsConfigService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/18 4:31 下午
 * @Version 1.0
 */
public class LdapTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsFactory dsFactory;

    @Resource
    private LdapHandler ldapHandler;

    @Test
    void longinTest() {
        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) getConfig();
        Authorization.Credential credential = Authorization.Credential.builder()
                .username("baiyi")
                .password("123456")
                .build();
        boolean login = ldapHandler.loginCheck(ldapDsInstanceConfig.getLdap(), credential);
        System.err.println(login);
    }


    @Test
    void queryGroupListTest() {
        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) getConfig();
        List<Group> groups = ldapHandler.queryGroupList(ldapDsInstanceConfig.getLdap());
        groups.forEach(g -> {
            System.err.println(JSON.toJSON(g));
        });
    }

    @Test
    BaseDsInstanceConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(1);
        System.err.println(JSON.toJSON(datasourceConfig));
        return dsFactory.build(datasourceConfig, LdapDsInstanceConfig.class);
    }
}
