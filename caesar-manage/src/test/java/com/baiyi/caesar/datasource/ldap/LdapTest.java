package com.baiyi.caesar.datasource.ldap;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.caesar.datasource.factory.DsConfigFactory;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.entry.Person;
import com.baiyi.caesar.ldap.handler.LdapHandler;
import com.baiyi.caesar.service.datasource.DsConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/18 4:31 下午
 * @Version 1.0
 */
@Slf4j
public class LdapTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    @Resource
    private LdapHandler ldapHandler;

    //
//    @Test
//    void longinTest() {
//        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) getConfig();
//        Authorization.Credential credential = Authorization.Credential.builder()
//                .username("baiyi")
//                .password("123456")
//                .build();
//        boolean login = ldapHandler.loginCheck(ldapDsInstanceConfig.getLdap(), credential);
//        System.err.println(login);
//    }
//
//

    @Test
    void queryPersonTest() {
        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) getConfig();
        Person person = ldapHandler.getPersonWithDn(ldapDsInstanceConfig.getLdap(), "cn=baiyi,ou=Users");
        log.info(JSON.toJSONString(person));
    }


    @Test
    void queryGroupTest() {
        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) getConfig();
        // dc=xincheng,dc=org
        Group group = ldapHandler.getGroupWithDn(ldapDsInstanceConfig.getLdap(), "cn=confluence-users,ou=Groups");
        log.info(JSON.toJSONString(group));
    }

    @Test
    BaseDsInstanceConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(1);
        return dsFactory.build(datasourceConfig, LdapDsInstanceConfig.class);
    }
}
