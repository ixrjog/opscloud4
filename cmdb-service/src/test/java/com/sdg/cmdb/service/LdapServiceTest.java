package com.sdg.cmdb.service;


import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.LdapGroup;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.impl.LdapServiceImpl;
import com.sdg.cmdb.util.PasswdUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.naming.directory.Attributes;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class LdapServiceTest {

    @Resource
    private LdapService ldapService;

    @Resource
    private LdapServiceImpl ldapServiceImpl;

    @Resource
    private UserDao userDao;

    @Test
    public void test() {
        List<LdapGroup> gropus = ldapService.searchUserLdapGroup("baiyi");
        for(LdapGroup group:gropus)
            System.err.println(group);
    }

    @Test
    public void testAddAndDelUser2Group() {
       // userDao.getUserByName("baiyi");
        UserDO userDO =new UserDO("baiyi");
        ldapService.delMemberToGroup(userDO,"nexus-users");
        //ldapService.addMemberToGroup(userDO,"nexus-users");

      // ldapServiceImpl.removeUserToGroup("baiyi","nexus-admin");
    }

    @Test
    public void testSearchLdapGroup1() {
        List<String> groups = ldapService.searchLdapGroup();
        for(String group:groups)
            System.err.println(group);

    }


    @Test
    public void testSearchLdapGroup2() {
        List<String> gropus = ldapService.searchLdapGroup("baiyi");
        for(String group:gropus)
            System.err.println(group);
    }




    @Test
    public void testSearchGroupUser() {
      List<UserVO>  users =  ldapService.searchLdapGroupUsers("Administrators");
      for(UserVO user:users)
          System.err.println(user);
    }
}
