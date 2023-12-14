package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.datasource.gitlab.base.BaseGitLabApiUnit;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabProjectDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabRepositoryDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabSshKeyDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabUserDriver;
import com.baiyi.opscloud.workorder.delegate.GitLabUserDelegate;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import jakarta.annotation.Resource;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/10/26 13:57
 * @Version 1.0
 */
public class GitLabApiTest extends BaseGitLabApiUnit {

    private static final Long BAIYI_USER_ID = 2L;

    private static final Long KEY_ID = 10L;

    @Resource
    private GitLabUserDelegate gitlabUserDelegate;

    /**
     * 模糊查询用户
     */
    @Test
    void findUserTest() {
        try {
            List<User> users = GitLabUserDriver.findUsers(getConfig().getGitlab(), "baiyi");
            print(users.getFirst());
        } catch (GitLabApiException e) {
            print(e);
        }
    }

    /**
     * 查询用户
     */
    @Test
    void getUserTest() {
        try {
            // baiyi  userId = 2L
            User user = GitLabUserDriver.getUser(getConfig().getGitlab(), BAIYI_USER_ID);
            print(user);
        } catch (GitLabApiException e) {
            print(e);
        }
    }

    @Test
    void getUsersTest() {
        try {
            List<User> users = GitLabUserDriver.getUsers(getConfig().getGitlab());
            print(users);
        } catch (GitLabApiException e) {
            print(e);
        }
    }

    @Test
    void getSshKeysWithUserIdTest() {
        try {
            List<SshKey> sshKeys = GitLabSshKeyDriver.getSshKeysWithUserId(getConfig().getGitlab(), BAIYI_USER_ID);
            print(sshKeys);
        } catch (GitLabApiException e) {
            print(e);
        }
    }


    /**
     * [
     * {
     * "sourceId":5,
     * "sourceName":"basic-service",
     * "sourceType":"Namespace",
     * "accessLevel":30
     * },
     * {
     * "sourceId":10,
     * "sourceName":"ops",
     * "sourceType":"Namespace",
     * "accessLevel":50
     * },
     * {
     * "sourceId":23,
     * "sourceName":"merchant-kili",
     * "sourceType":"Project",
     * "accessLevel":40
     * },
     * {
     * "sourceId":8,
     * "sourceName":"Android",
     * "sourceType":"Namespace",
     * "accessLevel":30
     * },
     * {
     * "sourceId":10,
     * "sourceName":"lib_net",
     * "sourceType":"Project",
     * "accessLevel":30
     * }
     * ]
     */
    @Test
    void getUserMembershipsTest() {
        try {
            List<Membership> memberships = GitLabUserDriver.getUserMemberships(getConfig().getGitlab(), BAIYI_USER_ID);
            print(memberships);
        } catch (GitLabApiException e) {
            print(e);
        }
    }

    @Test
    void getMembersTest() {
        try {
            List<Member> members = GitLabProjectDriver.getMembersWithProjectId(getConfigById(56).getGitlab(), 73L, 20);
            print(members);
        } catch (GitLabApiException e) {
            print(e);
        }
    }

    @Test
    void getProjectsTest() {
        try {
            List<Project> projects = GitLabProjectDriver.getProjects(getConfig().getGitlab());
            print(projects);
        } catch (GitLabApiException e) {
            print(e);
        }
    }

    @Test
    void createUser() {
        try {
            gitlabUserDelegate.createUser(getConfigById(56).getGitlab(), "chenyingying");
        } catch (TicketProcessException e) {
            print(e);
        }
    }

    @Test
    void ddd() {
        try {
            RepositoryFile rf = GitLabRepositoryDriver.getRepositoryFile(
                    getConfigById(29).getGitlab(),
                    163L,
                    "build.gradle",
                    "master");
            String x = new String(Base64.getDecoder().decode(rf.getContent()));
            // print(x);
            // version '0.0.1-SNAPSHOT'
//            String regex = "^\\s*version\\s*";
//            System.out.println(x.matches(regex));

//            GradleConnector connector = GradleConnector.newConnector();
//
//            connector.useGradleVersion(x);
//
//            // connector.forProjectDirectory(new File());
//
//            ProjectConnection connection =  connector.connect();
//
//            ModelBuilder<ProjectModel> customModelBuilder = connection.model(ProjectModel.class);
//
//            ProjectModel pm =  customModelBuilder.get();



            //ddd2(x);

        } catch (GitLabApiException e) {
            print(e);
        }
    }

    private void ddd2(String file){
        try {
            BufferedReader reader  = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8));

            String line = reader.readLine();
            while (line != null){
                String regex = "^\\s*version\\s*\\S*";
                if(line.matches(regex)){
                    System.out.println(line);
                }
                line = reader.readLine();
            }
            reader.close();


        }catch (IOException e){
            print(e);
        }
    }

    @Test
    void mavenTest() {
        try {
            /*
             *  <project>
             *     <artifactId>member-center-client</artifactId>
             *     <packaging>jar</packaging>
             *     <name>member-center-client</name>
             *     <version>1.0.1-SNAPSHOT</version>
             *  </project>
             *
             */
            RepositoryFile rf = GitLabRepositoryDriver.getRepositoryFile(
                    getConfigById(29).getGitlab(),
                    613L,
                    "member-center-client/pom.xml",
                    "master");
            String x = new String(Base64.getDecoder().decode(rf.getContent()));
            print(x);


            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model pomModel = reader.read(new ByteArrayInputStream(x.getBytes(StandardCharsets.UTF_8)));

            pomModel.getArtifactId();


        } catch (Exception e) {
            print(e);
        }
    }

}
