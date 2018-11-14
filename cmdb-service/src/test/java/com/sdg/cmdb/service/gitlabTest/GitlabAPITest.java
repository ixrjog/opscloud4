package com.sdg.cmdb.service.gitlabTest;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.GitlabAPIException;
import org.gitlab.api.models.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GitlabAPITest {
    static GitlabAPI api;

    private static final String TEST_URL = "http://" + System.getProperty("10.10.10.10", "10.10.10.10") + ":" + System.getProperty("80", "80");
    String rand = createRandomString();

    @BeforeClass
    public static void getApi() {
        api = APIForIntegrationTestingHolder.INSTANCE.getApi();
    }

    @Test
    public void Check_invalid_credentials() throws IOException {
        try {
            api.dispatch().with("login", "INVALID").with("password", createRandomString()).to("session", GitlabUser.class);
        } catch (GitlabAPIException e) {
            final String message = e.getMessage();
            if (!message.equals("{\"message\":\"401 Unauthorized\"}")) {
                throw new AssertionError("Expected an unauthorized message", e);
            } else if(e.getResponseCode() != 401) {
                throw new AssertionError("Expected 401 code", e);
            }
        }
    }
    @Test
    public void testAllProjects() throws IOException {
        api.getProjects();
    }

    @Test
    public void testConnect() throws IOException {
        assertEquals(GitlabAPI.class, api.getClass());
    }

    @Test
    public void testGetAPIUrl() throws IOException {
        URL expected = new URL(TEST_URL + "/api/v4/");
        assertEquals(expected, api.getAPIUrl(""));
    }

    @Test
    public void testGetUrl() throws IOException {
        URL expected = new URL(TEST_URL);
        assertEquals(expected + "/", api.getUrl("").toString());
    }

    @Test
    public void testCreateUpdateDeleteVariable() throws IOException {
        String key = randVal("key");
        String value = randVal("value");
        String newValue = randVal("new_value");
        String projectName = randVal("project");

        GitlabProject project = api.createProject(projectName);
        assertNotNull(project);

        GitlabBuildVariable variable = api.createBuildVariable(project.getId(), key, value);
        assertNotNull(variable);

        GitlabBuildVariable refetched = api.getBuildVariable(project.getId(), key);

        assertNotNull(refetched);

        assertEquals(refetched.getKey(), variable.getKey());
        assertEquals(refetched.getValue(), variable.getValue());

        api.updateBuildVariable(project.getId(), key, newValue);


        GitlabBuildVariable postUpdate = api.getBuildVariable(project.getId(), key);


        assertNotNull(postUpdate);
        assertEquals(postUpdate.getKey(), variable.getKey());
        assertNotEquals(postUpdate.getValue(), variable.getValue());
        assertEquals(postUpdate.getValue(), newValue);


        api.deleteBuildVariable(project.getId(), key);

        // expect a 404, but we have no access to it
        try {
            GitlabBuildVariable shouldNotExist = api.getBuildVariable(project.getId(), key);
            assertNull(shouldNotExist);
        } catch (FileNotFoundException thisIsSoOddForAnRESTApiClient) {
            assertTrue(true); // expected
        }

        api.deleteProject(project.getId());
    }

    @Test
    public void testGetProject() throws IOException {
      List<GitlabProject> list = api.getAllProjects();
      for(GitlabProject project:list){
          System.err.println(project.getName());
         //GitlabUser gu= new GitlabUser();
         // api.addProjectMember(project,)

      }
    }

    @Test
    public  void  testGitlabAccess() throws IOException {
     // api.addProjectMember()
    }



    @Test
    public void testGetGroupByPath() throws IOException {
        // Given
        String name = "groupName";
        String path = "groupPath";

        GitlabGroup originalGroup = api.createGroup(name, path);

        // When
        GitlabGroup group = api.getGroup(path);

        // Then:
        assertNotNull(group);
        assertEquals(originalGroup.getId(), group.getId());
        assertEquals(originalGroup.getName(), group.getName());
        assertEquals(originalGroup.getPath(), group.getPath());

        // Cleanup
        api.deleteGroup(group.getId());
    }

    @Test
    public void Check_get_owned_projects() throws IOException {
        final List<GitlabProject> ownedProjects = api.getOwnedProjects();
        assertEquals(0, ownedProjects.size());
    }

    @Test
    public void Check_search_projects() throws IOException {
        final List<GitlabProject> searchedProjects = api.searchProjects("foo");
        assertEquals(0, searchedProjects.size());
    }

    private String randVal(String postfix) {
        return rand + "_" + postfix;
    }

    private static String createRandomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

}
