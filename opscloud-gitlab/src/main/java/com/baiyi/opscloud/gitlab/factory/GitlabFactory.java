package com.baiyi.opscloud.gitlab.factory;


import com.baiyi.opscloud.gitlab.config.GitlabConfig;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.GitlabAPIException;
import org.gitlab.api.models.GitlabUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@Component
public class GitlabFactory implements InitializingBean {

    @Resource
    private GitlabConfig gitlabConfig;

    private static GitlabAPI api;

    public GitlabAPI getAPI() {
        return GitlabFactory.api;
    }

    private static String createRandomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private void checkInvalidCredentials() throws IOException {
        try {
            api.dispatch().with("login", "INVALID").with("password", createRandomString()).to("session", GitlabUser.class);
        } catch (GitlabAPIException e) {
            final String message = e.getMessage();
            if (!message.equals("{\"message\":\"401 Unauthorized\"}")) {
                throw new AssertionError("Expected an unauthorized message", e);
            } else if (e.getResponseCode() != 401) {
                throw new AssertionError("Expected 401 code", e);
            }
        }
    }


    private void initialGitlabAPI(){
        try {
            GitlabFactory.api = GitlabAPI.connect(gitlabConfig.getUrl(), gitlabConfig.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet(){
        initialGitlabAPI();
    }


}
