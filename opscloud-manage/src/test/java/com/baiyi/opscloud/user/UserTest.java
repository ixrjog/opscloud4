package com.baiyi.opscloud.user;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.auth.UserRoleVO;
import com.baiyi.opscloud.domain.vo.org.OrgDepartmentMemberVO;
import com.baiyi.opscloud.domain.vo.org.OrgDepartmentVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.AuthFacade;
import com.baiyi.opscloud.facade.OrgFacade;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.service.user.OcUserService;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.base.Global.BASE_ROLE_NAME;

/**
 * @Author baiyi
 * @Date 2020/3/11 9:30 上午
 * @Version 1.0
 */
public class UserTest extends BaseUnit {
    @Resource
    private OcUserService ocUserService;

    @Resource
    private UserFacade userFacade;

    @Resource
    private AuthFacade authFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private OrgFacade orgFacade;

    @Resource
    private AccountCenter accountCenter;

    @Test
    void updateUsersUuid() {
        UserParam.UserPageQuery pageQuery = new UserParam.UserPageQuery();
        pageQuery.setUsername("");
        pageQuery.setLength(10000);
        pageQuery.setPage(1);
        DataTable<OcUser> table = ocUserService.queryOcUserByParam(pageQuery);
        System.err.println(JSON.toJSONString(table));
        for (OcUser ocUser : table.getData()) {
            ocUser.setUuid(UUIDUtils.getUUID());
            ocUserService.updateOcUser(ocUser);
        }

    }

    @Test
    void accessLevelTest() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("xujian");
        int accessLevel = userPermissionFacade.getUserAccessLevel(ocUser);
        System.err.println(JSON.toJSONString(accessLevel));
    }

    @Test
    void aa() {
        String userStr =
                "shiluo\n" +
                        "zuobing";
        String[] users = userStr.split("\\n");
        for (String user : users) {
            System.err.println(user);
            OcUser ocUser =
                    ocUserService.queryOcUserByUsername(user);
            if (ocUser == null) continue;
            userFacade.retireUser(ocUser.getId());

        }

    }

    @Test
    void b() {
        OcUser ocUser =
                ocUserService.queryOcUserByUsername("ouyanggw");
        boolean r = RegexUtils.isPhone(ocUser.getPhone());
        System.err.println(r);
    }

    @Test
    void beReinstatedUserTest() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("xiuyuantest");
        userFacade.beReinstatedUser(ocUser.getId());
    }

    @Test
    void userSetRoleTest() {
        List<OcUser> users = ocUserService.queryOcUserAll();
        users.forEach(e -> {
            UserRoleVO.UserRole userRole = new UserRoleVO.UserRole();
            userRole.setRoleName(BASE_ROLE_NAME);
            userRole.setUsername(e.getUsername());
            authFacade.addUserRole(userRole);
        });
    }

    @Test
    void orgUserSetRoleTest() {
        DepartmentParam.PageQuery pageQuery = new DepartmentParam.PageQuery();
        pageQuery.setLength(100);
        pageQuery.setPage(1);
        DataTable<OrgDepartmentVO.Department> table = orgFacade.queryDepartmentPage(pageQuery);
        List<OrgDepartmentVO.Department> depts = table.getData();
        depts.forEach(e -> {
            // 技术部
            if (e.getDeptType() == 1) {
                System.err.println(e.getName());
                DepartmentMemberParam.PageQuery query = new DepartmentMemberParam.PageQuery();
                query.setDepartmentId(e.getId());
                query.setPage(1);
                query.setLength(100);
                DataTable<OrgDepartmentMemberVO.DepartmentMember> t = orgFacade.queryDepartmentMemberPage(query);
                if (!CollectionUtils.isEmpty(t.getData())) {
                    List<OrgDepartmentMemberVO.DepartmentMember> members = t.getData();
                    members.forEach(m -> {
                        System.err.println("---" + m.getUsername());
                        UserRoleVO.UserRole userRole = new UserRoleVO.UserRole();
                        userRole.setRoleName("dev");
                        userRole.setUsername(m.getUsername());
                        authFacade.addUserRole(userRole);
                    });
                }
            }
        });
    }


    @Test
    void queryUserGroupPageTest() {
        UserBusinessGroupParam.PageQuery pageQuery = new UserBusinessGroupParam.PageQuery();
        pageQuery.setName("nexus-users");
        pageQuery.setExtend(1);
        pageQuery.setPage(1);
        pageQuery.setLength(10);
        List<UserGroupVO.UserGroup> userGroupList = userFacade.queryUserGroupPage(pageQuery).getData();
        UserGroupVO.UserGroup nexusUserGroup = userGroupList.get(0);
        List<UserVO.User> nexusUsers = nexusUserGroup.getUsers();
        nexusUsers.forEach(user -> {
            UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission = new UserBusinessGroupParam.UserUserGroupPermission();
            userUserGroupPermission.setUserGroupId(18);
            userUserGroupPermission.setUserId(user.getId());
            userFacade.grantUserUserGroup(userUserGroupPermission);
        });
    }

    @Test
    void test1() {
        UserParam.CreateUser createUser = new UserParam.CreateUser();
        createUser.setDisplayName("修远");
        createUser.setUsername("xiuyuan");
        createUser.setPassword("123456");
        createUser.setEmail("xiuyuan@xinc818.group");
        createUser.setDingtalkUserId(422);
//        userFacade.sendDingtalkMsg(createUser);
    }


    @Test
    void test11() {
        List<OcUser> userList = ocUserService.queryOcUserInActive();
        userList.forEach(user -> accountCenter.active(user,false));
    }


}
