package com.sdg.cmdb.service.gitlabTest;


import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.plugin.gitlab.GitlabFactory;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GitlabTest {

    @Autowired
    private GitlabFactory gitlabFactory;


    @Test
    public void testAllProjects() {
        List<GitlabProject> projects = gitlabFactory.getApi().getProjects();
        for (GitlabProject project : projects) {
            System.err.println(project.getSshUrl());
            System.err.println(project.getOwner());
            System.err.println(project.getLastActivityAt().toString());

        }


    }


    @Test
    public void test2() {
        //{"admin":true,"avatarUrl":"https://www.gravatar.com/avatar/e07e74fd740aa5ee7c7e5cc50d5e186b?s=80&d=identicon","bio":"",
        // "canCreateGroup":true,"canCreateProject":true,"colorSchemeId":1,"createdAt":1544693543182,"currentSignInAt":1552041154659,
        // "email":"baiyi@gegejia.com","external":false,"id":2,"identities":[{"externUid":"cn=baiyi,ou=users,ou=system","provider":"ldapmain"}],
        // "lastActivityOn":1553126400000,"lastSignInAt":1551338692720,"linkedin":"","name":"白衣","projectsLimit":100000,"skype":"","state":"active","themeId":1,
        // "twitter":"","username":"baiyi","websiteUrl":""}

        List<GitlabUser> users = gitlabFactory.getApi().getUsers();
        for (GitlabUser user : users) {
            System.err.println(JSON.toJSONString(user));
        }


    }


    @Test
    public void test3() {
        try {
            List<GitlabSSHKey> sshKeys = gitlabFactory.getApi().getSSHKeys(2);
            for (GitlabSSHKey sshKey : sshKeys) {
                System.err.println(JSON.toJSONString(sshKey));
            }
        } catch (Exception e) {
              e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        try {
         //   List<GitlabSSHKey> sshKeys = gitlabFactory.getApi().createSSHKey()

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
