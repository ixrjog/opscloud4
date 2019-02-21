package com.sdg.cmdb.service;


import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.plugin.gitlab.GitlabFactory;
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

    @Resource
    private GitlabService gitlabService;

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


            System.err.println(projects);
        } catch (Exception e) {

        }
    }

    @Test
    public void testMembers() {
        try {

            GitlabProject gitlabProject = gitlabFactory.getApi().getProject(15);
            List<GitlabProjectMember> members = gitlabFactory.getApi().getProjectMembers(gitlabProject);
            System.err.println(members);
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

            //  List<GitlabCommit>  getChanges(long jobId, String jobName,String branch);

            List<GitlabCommit> list = gitlabService.getChanges(1,"java_opscloud_prod","master");


         //   List<GitlabCommit> list = gitlabFactory.getApi().getAllCommits(1, "256aaac33d6ce16152fd5e7f88e9c5c9dc013e78");


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
            String r ="";

            List<GitlabUser>  list = gitlabFactory.getApi().getUsers();
            for(GitlabUser user:list){
                r+= user.getName() + "/" + user.getUsername() +"\n";


            }

            System.err.println(r);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
