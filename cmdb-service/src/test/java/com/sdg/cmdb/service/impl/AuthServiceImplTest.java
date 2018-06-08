package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 2016/11/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public final class AuthServiceImplTest {

    @Resource
    private AuthService authService;

    @Test
    public void addUser() {
        UserDO userDO = new UserDO();

        userDO.setUsername("test003");
        userDO.setPwd("123456");
        userDO.setMail("userTest001@qq.com");
        // userDO.setMobile("12388888888");
        userDO.setDisplayName("测试账户");
        System.err.println(authService.addUser(userDO));
    }
//
//    @Test
//    public void addUser2() {
//        String username = "test889";
//        String dn = "cn=" + username + ",ou=users,ou=system";
//
//        Attributes attrs = new BasicAttributes();
//        BasicAttribute ocattr = new BasicAttribute("objectclass");
//        ocattr.add("top");
//        ocattr.add("inetOrgPerson");
//        ocattr.add("person");
//        ocattr.add("organizationalPerson");
//
//        attrs.put(ocattr);
//        attrs.put("cn", "test888");
//        attrs.put("sn", "test888");
//        attrs.put("displayName", "test888");
//        attrs.put("givenName", "test888");
//        attrs.put("mail", "test888@qq.com");
//        attrs.put("userpassword", "123456");
//
//        ldapTemplate.bind(dn, null, attrs);
//    }
//
//    @Test
//    public void testDelUser() {
//        String username = "test02";
//
//        AndFilter filter = new AndFilter();
//        filter.and(new EqualsFilter("cn", username));
//        List list = ldapTemplate.search("", filter.encode(), (Attributes attrs) -> {
//            return attrs.get("cn").get();
//        });
//
//        if (list.isEmpty()) {
//            addUser();
//        }
//
//        list = ldapTemplate.search("", filter.encode(), (Attributes attrs) -> {
//            return attrs.get("cn").get();
//        });
//        Assert.notEmpty(list);
//
//        authService.delUser(username);
//
//        list = ldapTemplate.search("", filter.encode(), (Attributes attrs) -> {
//            return attrs.get("cn").get();
//        });
//        Assert.isTrue(list.isEmpty());
//    }
//
//    @Test
//    public void testSearchUser() {
//        AndFilter filter = new AndFilter();
//        filter.and(new EqualsFilter("objectclass", "person"));
//        List<UserDO> list = ldapTemplate.search("", filter.encode(),
//                (Attributes attrs) -> {
//                    Object cn = attrs.get("cn").get();
//                    String mail = attrs.get("mail") == null ? (cn + "@51xianqu.net") : attrs.get("mail").get().toString();
//                    Object displayName = attrs.get("displayname").get();
//                    UserDO userDO = new UserDO();
//
//                    userDO.setMail(mail);
//                    userDO.setDisplayName(displayName.toString());
//                    userDO.setUsername(cn.toString());
//                    userDO.setPwd(PasswdUtils.getRandomPasswd(0));
//                    return userDO;
//                });
//
//        for (UserDO userDO : list) {
//            System.err.println(userDO.toString());
////            userDao.saveUserInfo(userDO);
////            userDao.updateUser(userDO.getUsername(), userDO.getPwd());
//        }
//    }
//
//    @Test
//    public void testCheckUserInLdapGroup() {
//        //判断用户是否在gourp
//        UserDO userDO = new UserDO();
//        ServerGroupDO serverGroupDO = new ServerGroupDO();
//        userDO.setUsername("xiaozhenxing");
//        serverGroupDO.setName("jira_users");
//
//        System.err.println(authService.checkUserInLdapGroup(userDO, serverGroupDO));
//    }
//
//
//    @Test
//    public void testSearchGroup() {
//        AndFilter filter = new AndFilter();
//        filter.and(new EqualsFilter("objectclass", "groupOfUniqueNames")).and(new EqualsFilter("cn", "bamboo-cmdb"));
//        ldapTemplate.search("", filter.encode(),
//                (Attributes attrs) -> {
//                    //Object cn = attrs.get("cn").get();
//                    //System.err.println(attrs.get("cn").get());
//                    //System.err.println(attrs.get("uniqueMember").size());
//                    //System.err.println(attrs.get("uniqueMember").get(0).toString());
//                    //System.err.println(attrs.get("uniqueMember").get(1).toString());
//                    //System.err.println(attrs.get("uniqueMember").get(2).toString());
//
//                    for (int i = 1; i <= attrs.get("uniqueMember").size(); i++) {
//                        System.err.println(i - 1);
//                        Object uniqueMember = attrs.get("uniqueMember").get(i - 1);
//                        System.err.println(uniqueMember.toString());
//                    }
//
//                    System.err.println("-----------------");
//                    Object um = attrs.get("uniqueMember").get();
//                    System.err.println(attrs.get("uniqueMember").get());
//                    return null;
//                });
//
//
//    }


    @Test
    public void testAddMemberToGroup() {
        //判断用户是否在gourp
        UserDO userDO = new UserDO();
        //ServerGroupDO serverGroupDO = new ServerGroupDO();
        userDO.setUsername("liangjian");
        //serverGroupDO.setName("group_cmdb-test");
        //   System.err.println(authService.addMemberToGroup(userDO, serverGroupDO));

        System.err.println(authService.addMemberToGroup(userDO, "bamboo-trade"));
    }


    @Test
    public void testDelMemberToGroup() {
        //判断用户是否在gourp
        UserDO userDO = new UserDO();
        ServerGroupDO serverGroupDO = new ServerGroupDO();
        userDO.setUsername("liangjan");
        serverGroupDO.setName("group_wmtback");
        //  System.err.println(authService.delMemberToGroup(userDO, serverGroupDO));


    }

    @Test
    public void testCheckUserInLdapGroup() {
        //判断用户是否在gourp
        System.err.println(authService.isJiraUsers("xiaozhenxing"));
        System.err.println(authService.isConfluenceUsers("xiaozhenxing"));
    }

    @Test
    public void testCheckUserInLdap() {
//        List<String> groups = authService.searchLdapGroup("gechong");
//        for (String g : groups)
//            System.err.println(g);
        System.err.println(authService.checkUserInLdap("zhangsimeng"));
        //System.err.println(authService.removeMember2Group("gechong","Administrators"));
    }

    @Test
    public void testUnbindUser() {
        System.err.println(authService.unbindUser("chenhongtu"));
        System.err.println(authService.checkUserInLdap("chenhongtu"));
    }

    @Test
    public void testGetMailUserAccountStatus() {
        UserDO userDO = new UserDO();
        userDO.setMail("liubingling@5aaa");
        authService.setMailUserAccountStatus(userDO, AuthServiceImpl.MAIL_ACCOUNT_STATUS_CLOSED);
        System.err.println(authService.getMailAccountStatus("liubingling@aaa"));
    }

    @Test
    public void testUserLeaving() {
        //职员离职通知: {  姓名: 郑,   工号: 002864,   手机号: 1516,   职务: 服务体验专员,   部门: 总裁办-服务体验部,   工作城市: 杭州,
        // 工作邮箱: zhengyan@xx }
        authService.delUser("zhengyan");
    }

    @Test
    public void testSearchBambooGroup() {
        List<String> groups = authService.searchBambooGroup();
        for (String group : groups) {
            System.err.println(group);
        }

    }

    @Test
    public void testLogin() {

        String username = "liangjian";
        String password = "1";
        //System.err.println(authService.loginCredentialCheck(username, password));

        UserDO userDO = new UserDO("liangjian");
        userDO.setPwd("12345");
        System.err.println(authService.apiLoginCheck(userDO));
//        String username = "shisanyang";
//        String password = "123";
//        // cn=shisanyang;ou=users;ou=syste
//
//        AndFilter filter = new AndFilter();
//        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter("cn", username));
//        LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstanceByType(LdapDO.LdapTypeEnum.cmdb.getDesc());
//        System.err.println( filter.toString());
//
//        boolean authResult = ldapTemplate.authenticate("ou=users1,ou=system", filter.toString(), password);
//
//       // boolean authResult = ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.toString(), password);
//
//
//        System.err.println( authResult);
    }


}
