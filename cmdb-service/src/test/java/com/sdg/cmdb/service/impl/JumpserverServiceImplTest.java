package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.dao.jumpserver.JumpserverDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.jumpserver.AssetsNodeDO;
import com.sdg.cmdb.domain.jumpserver.UsersUserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.util.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class JumpserverServiceImplTest {

    @Autowired
    private JumpserverServiceImpl jumpserverService;


    @Autowired
    private UserDao userDao;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Autowired
    private JumpserverDao jumpserverDao;

    @Test
    public void testAuthAdmin() {
        UserDO userDO = userDao.getUserByName("baiyi");
        jumpserverService.authAdmin(userDO.getId());
    }

    @Test
    public void testGetAdministrators() {
        List<UsersUserDO> administrators = jumpserverService.getAdministrators();
        for (UsersUserDO usersUserDO : administrators)
            System.err.println(usersUserDO);
    }

    /**
     * 用户绑定服务器组
     */
    @Test
    public void testBindUserGroup() {
        UserDO userDO = userDao.getUserByName("baiyi");
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_zebra-platform");
        jumpserverService.bindUserGroup(userDO, serverGroupDO);
    }

    /**
     * 用户解除绑定服务器组
     */
    @Test
    public void testUnbindUserGroup() {
        UserDO userDO = userDao.getUserByName("baiyi");
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_zebra-platform");
        jumpserverService.unbindUserGroup(userDO, serverGroupDO);
    }


    @Test
    public void testAddServer() {

        ServerDO serverDO = serverDao.getServerInfoById(33);
        //  jumpserverService.addAssetsAsset(serverDO);
    }


    @Test
    public void testAddServerGroup() {

        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(11);
        //  jumpserverService.addAssetsNode(serverGroupDO);
    }


    @Test
    public void testSyncAssets() {
        jumpserverService.syncAssets();
    }

    /**
     * 校验用户
     */
    @Test
    public void testCheckUsers() {
        jumpserverService.checkUsers();
    }

    /**
     * 校验资产
     */
    @Test
    public void testCheckAssets() {
        jumpserverService.checkAssets();
    }

    @Test
    public void testSyncUsers() {
        jumpserverService.syncUsers();
    }

    @Test
    public void testNow() {
        Date date = new Date();
        long time = date.getTime();
        time = time - TimeUtils.hourTime * 8;
        System.err.println(date.getTime());

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String now = formatter.format(time);
        System.err.println(now);
    }


    @Test
    public void testUpdateUserPubkey() {
        jumpserverService.updateUserPubkey("xiaodu");
    }


    @Test
    public void test2() {
        List<AssetsNodeDO> list = jumpserverDao.getAssetsNodeAll();
        int key = 0;
        for (AssetsNodeDO node : list) {
            key++;
            node.setKey("1:" + key);
            jumpserverDao.updateAssetsNode(node);
        }


    }


    @Test
    public void test3() {
      System.err.println(jumpserverService.getAssetsNodeKey());


    }

}
