package com.baiyi.opscloud.gitlab.handler;

import com.baiyi.opscloud.gitlab.factory.GitlabFactory;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 1:53 下午
 * @Version 1.0
 */
@Component
public class GitlabProjectHandler {

    @Resource
    private GitlabFactory gitlabFactory;

    public List<GitlabProject> getProjects() {
        return gitlabFactory.getAPI().getAllProjects();
    }

    public GitlabProject getProject(Serializable projectId) throws IOException {
        return gitlabFactory.getAPI().getProject(projectId);
    }
}
