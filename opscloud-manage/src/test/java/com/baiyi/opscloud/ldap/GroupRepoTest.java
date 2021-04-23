package com.baiyi.opscloud.ldap;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthRole;
import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:39 下午
 * @Version 1.0
 */
public class GroupRepoTest extends BaseUnit {


    @Resource
    private GroupRepo groupRepo;

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    @Resource
    private OcUserService ocUserService;

    @Test
    void testQueryPersonList() {
        List<Group> list = groupRepo.getGroupList();
        for (Group group : list) {
            System.err.println(group);
        }
    }

    @Test
    void testQueryGroupMember() {
        List<String> list = groupRepo.queryGroupMember("jenkins-administrators");
        for (String username : list) {
            System.err.println(username);
        }
    }

    /**
     * nexus-developer
     * bigdata-group-platform
     */
    @Test
    void testRemoveGroupMember() {
        Boolean result = groupRepo.removeGroupMember("bigdata-group-platform", "baiyi");
        System.err.println(result);
    }

    @Test
    void testAddGroupMember() {
        Boolean result = groupRepo.addGroupMember("vpn-users", "baiyi");
        System.err.println(result);
    }

    @Test
    void test() {
        ocUserService.queryOcUserActive().forEach(u -> {
            try {
                OcAuthRole ocAuthRole = ocAuthRoleService.queryTopOcAuthRoleByUsername(u.getUsername());
                if (ocAuthRole.getRoleName().equals("dev")){
                    Boolean result = groupRepo.addGroupMember("vpn-users", u.getUsername());
                    System.err.println(result);
                }
            } catch (Exception e) {

            }
        });
    }
}
