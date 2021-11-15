package com.baiyi.opscloud.datasource.nacos;

import com.baiyi.opscloud.datasource.nacos.base.BaseNacosTest;
import com.baiyi.opscloud.datasource.nacos.entry.*;
import com.baiyi.opscloud.datasource.nacos.handler.NacosAuthHandler;
import com.baiyi.opscloud.datasource.nacos.handler.NacosClusterHandler;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/11 5:20 下午
 * @Version 1.0
 */
public class NacosTest extends BaseNacosTest {

    @Resource
    private NacosAuthHandler nacosAuthHandler;

    @Resource
    private NacosClusterHandler nacosClusterHandler;

    @Test
    void authLoginTest() {
        NacosLogin.AccessToken accessToken = nacosAuthHandler.login(getConfig().getNacos());
        System.err.println(accessToken.toString());
    }

    @Test
    void listNodesTest() {
        NacosCluster.NodesResponse nr = nacosClusterHandler.listNodes(getConfig().getNacos());
        System.err.println(nr.getData());
    }

    @Test
    void listPermissionsTest() {
        NacosPermission.PermissionsResponse pr = nacosAuthHandler.listPermissions(getConfig().getNacos(), NacosPageParam.PageQuery.builder().build());
        System.err.println(pr.getPageItems());
    }

    @Test
    void listUserTest() {
        NacosUser.UsersResponse usersResponse = nacosAuthHandler.listUsers(getConfig().getNacos(), NacosPageParam.PageQuery.builder().build());
        System.err.println(usersResponse.getPageItems());
    }

   @Test
    void listRolesTest() {
        NacosRole.RolesResponse rolesResponse = nacosAuthHandler.listRoles(getConfig().getNacos(), NacosPageParam.PageQuery.builder().build());
        System.err.println(rolesResponse.getPageItems());
    }
}