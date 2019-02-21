package com.sdg.cmdb.service.gitlabTest;


import com.sdg.cmdb.plugin.gitlab.GitlabFactory;
import org.gitlab.api.models.GitlabProject;
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



}
