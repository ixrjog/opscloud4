package com.baiyi.opscloud.datasource.nacos;

import com.baiyi.opscloud.datasource.nacos.base.BaseNacosTest;
import com.baiyi.opscloud.datasource.nacos.drive.NacosAuthDrive;
import com.baiyi.opscloud.datasource.nacos.drive.NacosClusterDrive;
import com.baiyi.opscloud.datasource.nacos.entity.*;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/11 5:20 下午
 * @Version 1.0
 */
public class NacosTest extends BaseNacosTest {

    @Resource
    private NacosAuthDrive nacosAuthDrive;

    @Resource
    private NacosClusterDrive nacosClusterDrive;

    @Test
    void authLoginTest() {
        NacosLogin.AccessToken accessToken = nacosAuthDrive.login(getConfig().getNacos());
        System.err.println(accessToken.toString());
    }

    @Test
    void listNodesTest() {
        NacosCluster.NodesResponse nr = nacosClusterDrive.listNodes(getConfig().getNacos());
        System.err.println(nr.getData());
    }

    @Test
    void listPermissionsTest() {
        NacosPermission.PermissionsResponse pr = nacosAuthDrive.listPermissions(getConfig().getNacos(), NacosPageParam.PageQuery.builder().build());
        System.err.println(pr.getPageItems());
    }

    @Test
    void listUserTest() {
        NacosUser.UsersResponse usersResponse = nacosAuthDrive.listUsers(getConfig().getNacos(), NacosPageParam.PageQuery.builder().build());
        System.err.println(usersResponse.getPageItems());
    }

    @Test
    void searchUsersTest() {
        List<String> users = nacosAuthDrive.searchUsers(getConfig().getNacos(), "baiyi");
        print(users);
    }

    @Test
    void createUserTest() {
       nacosAuthDrive.createUser(getConfig().getNacos(), "LDAP_xxijihd3","23432532523");
    }

    @Test
    void listRolesTest() {
        NacosRole.RolesResponse rolesResponse = nacosAuthDrive.listRoles(getConfig().getNacos(), NacosPageParam.PageQuery.builder().build());
        System.err.println(rolesResponse.getPageItems());
    }

}