package com.baiyi.opscloud.ldap;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
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

    @Test
    void testQueryPersonList() {
        List<Group> list = groupRepo.getGroupList();
        for (Group group : list) {
            System.err.println(group);
        }
    }

    @Test
    void testQueryGroupMember() {
        List<String> list = groupRepo.queryGroupMember("jenkins-admin");
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
        Boolean result = groupRepo.addGroupMember("bigdata-group-platform", "baiyi");
        System.err.println(result);
    }
}
