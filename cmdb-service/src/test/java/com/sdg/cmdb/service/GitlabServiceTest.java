package com.sdg.cmdb.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.dao.cmdb.GitlabDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.gitlab.GitlabProjectDO;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.plugin.gitlab.GitlabFactory;
import com.sdg.cmdb.util.EncryptionUtil;
import com.sdg.cmdb.util.SSHKeyUtils;
import org.gitlab.api.Pagination;
import org.gitlab.api.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GitlabServiceTest {

    @Autowired
    private GitlabFactory gitlabFactory;

    @Autowired
    private GitlabService gitlabService;

    @Autowired
    private GitlabDao gitlabDao;

    @Autowired
    private UserService userService;

    @Test
    public void testUpdateProjects() {
        boolean rt = gitlabService.updateProjcets();
        System.err.println(rt);
    }

    @Test
    public void testVer() {
        try {
            GitlabVersion gv = gitlabFactory.getApi().getVersion();
            System.err.println(gv.getRevision());
            System.err.println(gv.getVersion());
        } catch (Exception e) {

        }
    }

    @Test
    public void testProjects() {
        try {
            List<GitlabProject> projects = gitlabFactory.getApi().getAllProjects();
            System.err.println(projects);


        } catch (Exception e) {
        }
    }

    @Test
    public void testGroups() {
        try {
            List<GitlabGroup> groups = gitlabFactory.getApi().getGroups();
            for (GitlabGroup group : groups)
                System.err.println(group.getId() + " " + group.getName());
        } catch (Exception e) {
        }
    }

    @Test
    public void testGroups2() {
        /**
         * 104 BaseBusiness
         17 bigdata
         105 buyer
         80 core
         84 docs
         112 effect
         73 fe-admin
         68 fe-buyer
         63 fe-effect
         18 fe-gnpm
         27 fe-scaffold
         119 fe-web
         35 fe-xql
         6 fe-zebra
         128 ggj-fe-innerserver
         65 ggj-fe-innerserver-bailu
         51 gim
         24 gnpm
         12 libs_android
         82 libs_android
         13 libs_fe
         11 libs_ios
         81 libs_ios
         5 ops
         31 platform
         100 seller
         66 service
         10 service_fe
         76 xiaoqule
         9 zebra_fe
         */
        try {
            List<GitlabProject> projects = gitlabFactory.getApi().getGroupProjects(31);
            for (GitlabProject p : projects)
                System.err.println(p.getId() + " " + p.getName());
        } catch (Exception e) {
        }
    }


    @Test
    public void testMembers1() {
        try {

            GitlabProject gitlabProject = gitlabFactory.getApi().getProject(1);
            List<GitlabProjectMember> members = gitlabFactory.getApi().getProjectMembers(gitlabProject);
            for (GitlabProjectMember m : members)
                System.err.println(JSON.toJSONString(m));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMembers2() {
        try {
            //GitlabGroup gitlabGroup = gitlabFactory.getApi().getGroup(9);
            List<GitlabGroupMember> members = gitlabFactory.getApi().getGroupMembers(27);
            for (GitlabGroupMember m : members)
                System.err.println(JSON.toJSONString(m));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2() {
        try {
            GitlabProject gp = gitlabFactory.getApi().getProject(1);
            GitlabBranch gb = gitlabFactory.getApi().getBranch(1, "master");

            List<GitlabCommit> commits = gitlabFactory.getApi().getAllCommits(1);

            List<GitlabTag> tags = gitlabFactory.getApi().getTags(1);


            System.err.println(JSON.toJSONString(gb));
            System.err.println(JSON.toJSONString(commits));
            System.err.println(JSON.toJSONString(tags));

        } catch (Exception e) {

        }
    }


    @Test
    public void test3() {
        String m = "Merge branch 'dev'";
        System.err.println(m.indexOf("Merge branch", 0));

    }


    // bf7bf49ef1b26e8164f8d1f66a281385733a9105
    // 1e89d6b22ea7239a62fb464cc045b59783531b06
    @Test
    public void test4() {
        try {
            GitlabProject gp = gitlabFactory.getApi().getProject(1);
            GitlabBranch gb = gitlabFactory.getApi().getBranch(1, "master");

            GitlabCommit commits = gitlabFactory.getApi().getCommit(1, "bf7bf49ef1b26e8164f8d1f66a281385733a9105");

            System.err.println(JSON.toJSONString(commits));

            List<GitlabTag> tags = gitlabFactory.getApi().getTags(1);


            System.err.println(gitlabFactory.getApi().getCommit(1, "2f0bd1fc251fde005ac014fbf4d75c98323c8e6c").getMessage());

            System.err.println(gitlabFactory.getApi().getCommit(1, "256aaac33d6ce16152fd5e7f88e9c5c9dc013e78").getMessage());


            System.err.println(JSON.toJSONString(gb));

            System.err.println(JSON.toJSONString(tags));

        } catch (Exception e) {

        }
    }


    @Test
    public void test5() {
        try {
            List<GitlabCommit> list = gitlabService.getChanges(12, "buyer_platform", "buyer");
            for (GitlabCommit x : list) {
                System.err.println(JSON.toJSONString(x));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Test
    public void test6() {
        try {
            String r = "";
            List<GitlabUser> list = gitlabFactory.getApi().getUsers();
            for (GitlabUser user : list) {
                r += user.getName() + "/" + user.getUsername() + "\n";
                System.err.println(JSON.toJSONString(user));
            }
            System.err.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test7() {
        gitlabService.updateUsers();
    }

    @Test
    public void test8() {
        List<GitlabSSHKey> keys = gitlabService.getUserSSHKey("mingzhe");
        for (GitlabSSHKey key : keys) {
            System.err.println(JSON.toJSONString(key));
            System.err.println(SSHKeyUtils.getMD5(key.getKey()));
        }
        //    UserDO userDO = userService.getUserDOByName("baiyi");
        //    System.err.println(SSHKeyUtils.getMD5(userDO.getRsaKey()));

    }

    @Test
    public void test9() {
        UserDO userDO = userService.getUserDOByName("baiyi");
        boolean r = gitlabService.pushSSHKey("baiyi", userDO.getRsaKey());
        System.err.println(r);
    }

    @Test
    public void test10() {
        GitlabUser gitlabUser = gitlabService.createUser("test001");
        System.err.println(JSON.toJSONString(gitlabUser));

    }


    @Test
    public void test11() {
        try {
            List<GitlabProjectMember> list = gitlabFactory.getApi().getProjectMembers(27);
            for (GitlabProjectMember gitlabProjectMember : list) {
                System.err.println(JSON.toJSONString(gitlabProjectMember));
            }

        } catch (Exception e) {

        }

    }


    @Test
    public void test12() {
        try {
            List<GitlabGroup> list = gitlabFactory.getApi().getGroups();
            for (GitlabGroup group : list) {
                System.err.println(JSON.toJSONString(group));
                List<GitlabGroupMember> gmList = gitlabFactory.getApi().getGroupMembers(group.getId());
                for (GitlabGroupMember gitlabGroupMember : gmList)
                    System.err.println(JSON.toJSONString(gitlabGroupMember));
            }
        } catch (Exception e) {
        }
    }

    @Test
    public void test13() {
        try {
            List<GitlabBranch> list = gitlabService.getProjectBranchsByGitFlow(102, EnvType.EnvTypeEnum.test.getCode());
            for (GitlabBranch branch : list) {
                System.err.println(JSON.toJSONString(branch));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test15() {
        try {
            GitlabProjectDO gitlabProjectDO = gitlabDao.getGitlabProjectByName("opscloud");
            GitlabTag gitlabTag = gitlabFactory.getApi().addTag(gitlabProjectDO.getProjectId(), "1.0.0", "master", "测试", "#### 标题 \n");
            System.err.println(JSON.toJSONString(gitlabTag));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test16() {
        gitlabService.updateGroups();
    }


    @Test
    public void test17() {
        JSONObject obj = new JSONObject();
        String x = "http://1ij.coijgee.com1\"";
        x = x.replaceAll("\"", "\\\"");
        System.err.println(x);
        obj.put("a", x);
        System.err.println(obj.toJSONString());
    }

    @Test
    public void test99() {
        try {
            List<GitlabProject> list = gitlabFactory.getApi().getGroupProjects(6);
            for (GitlabProject p: list) {
                System.err.println(JSON.toJSONString(p.getName()));
                System.err.println(p.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
