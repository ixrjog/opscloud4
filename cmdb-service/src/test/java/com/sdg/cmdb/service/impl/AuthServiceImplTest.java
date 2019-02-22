package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.GUser;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.LdapService;
import com.sdg.cmdb.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 2016/11/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public final class AuthServiceImplTest {


    @Resource
    private AuthService authService;

    @Resource
    private UserDao userDao;

    @Resource
    private LdapService ldapService;


    @Test
    public void addGUser() {

//
//        for (GUser gu : users) {
//            System.err.println(gu.getUserVO().toString());
//            System.err.println(authService.addUser(gu.getUserVO()));
//        }
//        System.err.println( users.size());
    }

    @Test
    public void updateUser() {
        UserVO userVO = new UserVO();
        userVO.setUsername("test555");
        userVO.setUserpassword("123456");
        userVO.setMail("123456@qq.com");
        userVO.setMobile("123455555555");
        //System.err.println(authService.updateUser(userVO));
    }

    @Test
    public void testA() {
        List<UserDO> userList = userDao.getAllUser();
        String r = "";
        r += userList.size() + "\n";
        for (UserDO userDO : userList) {
            if (authService.isRole(userDO.getUsername(), RoleDO.roleDevelop)) {
                r += userDO.getUsername() + " " + userDO.getDisplayName() + "\n";
                try{
                  r += "confluence-users:" + ldapService.addMemberToGroup(userDO,"confluence-users");
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    r += "nexus-users:" +  ldapService.addMemberToGroup(userDO,"nexus-users");
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    r += "sonar-users:" +   ldapService.addMemberToGroup(userDO,"sonar-users");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        System.err.println(r);
    }


    @Test
    public void addUser() {
        UserVO userVO = new UserVO();
        userVO.setUsername("test003");
        userVO.setUsername("123456");
        userVO.setMail("userTest001@qq.com");
        // userDO.setMobile("12388888888");
        userVO.setDisplayName("测试账户");
        System.err.println(authService.addUser(userVO));
    }


    /**
     * 将用户添加到组
     */
    @Test
    public void testAddGroup() {
        //  System.err.println(authService.addLdapGroup("sonar-administrators"));
    }

    /**
     * 将用户添加到组
     */
    @Test
    public void testAddMemberToGroup() {


        UserDO userDO = new UserDO();
        userDO.setUsername("admin-sonar");
        //  System.err.println(authService.addMemberToGroup(userDO, "Administrators"));
    }

    @Test
    public void testSearchBambooGroup() {
//        List<String> groups = authService.searchBambooGroup();
//        for (String group : groups) {
//            System.err.println(group);
//        }

    }


    @Test
    public void testDelMemberToGroup() {
        //判断用户是否在gourp
        UserDO userDO = new UserDO();
        ServerGroupDO serverGroupDO = new ServerGroupDO();
        userDO.setUsername("liangjan");
        serverGroupDO.setName("bamboo-trade");
        //  System.err.println(authService.delMemberToGroup(userDO, serverGroupDO));


    }

    @Test
    public void testCheckUserInLdapGroup() {
        //判断用户是否在gourp
//        System.err.println(authService.isJiraUsers("xiaozhenxing"));
//        System.err.println(authService.isConfluenceUsers("xiaozhenxing"));
    }

    @Test
    public void testCheckUserInLdap() {
//        List<String> groups = authService.searchLdapGroup("gechong");
//        for (String g : groups)
//            System.err.println(g);
        // System.err.println(authService.checkUserInLdap("zhangsimeng"));
        //System.err.println(authService.removeMember2Group("gechong","Administrators"));
    }

    @Test
    public void testUnbindUser() {
        //  System.err.println(authService.unbindUser("chenhongtu"));
        //  System.err.println(authService.checkUserInLdap("chenhongtu"));
    }


    @Test
    public void testUserLeaving() {
        //职员离职通知: {  姓名: 郑,   工号: 002864,   手机号: 1516,   职务: 服务体验专员,   部门: 总裁办-服务体验部,   工作城市: 杭州,
        // 工作邮箱: zhengyan@xx }
        authService.delUser("zhengyan");
    }


    @Test
    public void testLogin() {
        // 320d9c0d29c1a16f9604ca608d6f2557a3fd0270
        UserDO userDO = new UserDO("caosp");
        userDO.setPwd("caosp");
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
