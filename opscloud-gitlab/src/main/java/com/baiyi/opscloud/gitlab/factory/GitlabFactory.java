package com.baiyi.opscloud.gitlab.factory;


import com.baiyi.opscloud.gitlab.config.GitlabConfig;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.GitlabAPIException;
import org.gitlab.api.models.GitlabUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@Component
public class GitlabFactory {

    @Resource
    private GitlabConfig gitlabConfig;

    static GitlabAPI api;

    public void buildApi() {
        try {
            api = GitlabAPI.connect(gitlabConfig.getUrl(), gitlabConfig.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GitlabAPI getApi() {
        return api;
    }

    private static String createRandomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private void Check_invalid_credentials() throws IOException {
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


}
