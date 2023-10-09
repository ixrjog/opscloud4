package com.baiyi.opscloud.datasource.ldap;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.business.account.impl.LdapAccountHandler;
import com.baiyi.opscloud.datasource.business.account.AccountGroupHandlerFactory;
import com.baiyi.opscloud.datasource.business.account.IAccountGroup;
import com.baiyi.opscloud.datasource.ldap.driver.LdapDriver;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;
import com.baiyi.opscloud.datasource.ldap.entity.LdapPerson;
import com.baiyi.opscloud.datasource.ldap.repo.PersonRepo;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.user.UserGroupService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

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
    private DsConfigManager dsFactory;

    @Resource
    private LdapDriver ldapHandler;

    @Resource
    private UserService userService;

    @Resource
    private UserGroupService userGroupService;

    @Resource
    private PersonRepo PersonRepo;

    @Resource
    private DsInstanceService dsInstanceService;

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

    @Resource
    private LdapAccountHandler ldapAccountProvider;


    @Test
    void createUserTest() {
        User user = userService.getByUsername("xiuyuan");
        user.setPassword("");
        DatasourceInstance dsInstance = dsInstanceService.getById(2);
        ldapAccountProvider.create(dsInstance, user);
    }

    @Test
    void updateUserTest() {
        User user = userService.getByUsername("nacos-dev-new");
        user.setPassword("abcd1234");
        DatasourceInstance dsInstance = dsInstanceService.getById(2);
        ldapAccountProvider.update(dsInstance, user);
    }

    @Test
    void createUserTest2() {
        User user = userService.getByUsername("baiyi");
        UserGroup userGroup = userGroupService.getByName("vpn-users");
        DatasourceInstance dsInstance = dsInstanceService.getById(2);
        // ldapAccountGroupProvider.create(dsInstance, userGroup);
        SimpleBusiness businessResource = SimpleBusiness.builder()
                .businessId(userGroup.getId())
                .businessType(BusinessTypeEnum.USERGROUP.getType())
                .build();
        IAccountGroup iAccountGroup = AccountGroupHandlerFactory.getIAccountGroupByInstanceType(DsTypeEnum.LDAP.name());
        // iAccountGroup.create(dsInstance,userGroup);
        iAccountGroup.grant(dsInstance, user, businessResource);
    }

    @Test
    void createUserGroupTest() {
        // nexus-admin nexus-developer nexus-users
        UserGroup userGroup = userGroupService.getByName("nexus-users");
        DatasourceInstance dsInstance = dsInstanceService.getById(2);
        IAccountGroup iAccountGroup = AccountGroupHandlerFactory.getIAccountGroupByInstanceType(DsTypeEnum.LDAP.name());
        iAccountGroup.create(dsInstance,userGroup);

    }

    @Test
    void queryPersonTest() {
        LdapConfig ldapDsInstanceConfig = (LdapConfig) getConfig();
        LdapPerson.Person person = ldapHandler.getPersonWithDN(ldapDsInstanceConfig.getLdap(), "cn=baiyi,ou=People");
    }


    @Test
    void queryGroupTest() {
        LdapConfig ldapDsInstanceConfig = (LdapConfig) getConfig();
        // "cn=confluence-users,ou=Groups"
        LdapGroup.Group group = ldapHandler.getGroupWithDN(ldapDsInstanceConfig.getLdap(), "cn=vpn-users,ou=Groups");
    }

    @Test
    void queryGroupTest2() {
        LdapConfig ldapDsInstanceConfig = (LdapConfig) getConfig();

        ldapHandler.unbind(ldapDsInstanceConfig.getLdap(), "cn=dev,ou=Groups");
        ldapHandler.unbind(ldapDsInstanceConfig.getLdap(), "cn=daily,ou=Groups");
        ldapHandler.unbind(ldapDsInstanceConfig.getLdap(), "cn=gray,ou=Groups");
        ldapHandler.unbind(ldapDsInstanceConfig.getLdap(), "cn=backend,ou=Groups");
        ldapHandler.unbind(ldapDsInstanceConfig.getLdap(), "cn=frontend,ou=Groups");
        // dc=xincheng,dc=org
//        List<Group> groups = ldapHandler.queryGroupList(ldapDsInstanceConfig.getLdap());
//        groups.forEach(g -> {
//            if (g.getGroupName().startsWith("bamboo-")) {
//                System.out.println(g.getGroupName());
//                ldapHandler.unbind(ldapDsInstanceConfig.getLdap(), "cn=" + g.getGroupName() + ",ou=Groups");
//            }
//        });
    }

    @Test
    BaseDsConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(2);
        return dsFactory.build(datasourceConfig, LdapConfig.class);
    }

    @Test
    void xxx() {
        List<User> userList = userService.listInactive();
        LdapConfig ldapDsInstanceConfig = (LdapConfig) getConfig();

        userList.forEach(user -> {
            // ,dc=xincheng,dc=org
            String dn = ldapDsInstanceConfig.getLdap().buildUserDn(user.getUsername());
//            ldapHandler.unbind(ldapDsInstanceConfig.getLdap(), dn);


//            Person person = PersonRepo.findPersonWithDn(ldapDsInstanceConfig.getLdap(), dn);
            System.out.println(dn);
        });


    }
}
