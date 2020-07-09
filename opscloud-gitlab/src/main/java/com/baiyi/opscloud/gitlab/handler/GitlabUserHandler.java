package com.baiyi.opscloud.gitlab.handler;

import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.gitlab.factory.GitlabFactory;
import org.gitlab.api.models.CreateUserRequest;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 1:41 下午
 * @Version 1.0
 */
@Component
public class GitlabUserHandler {

    @Resource
    private GitlabFactory gitlabFactory;

    public List<GitlabSSHKey> getUserSSHKeys(Integer targetUserId) throws IOException {
        return gitlabFactory.getAPI().getSSHKeys(targetUserId);
    }

    public List<GitlabUser> getUsers() {
        return gitlabFactory.getAPI().getUsers();
    }

    public GitlabUser createUser(CreateUserRequest createUserReq) throws IOException {
        return gitlabFactory.getAPI().createUser(createUserReq);
    }

    public GitlabSSHKey createSSHKey(Integer targetUserId, UserCredentialVO.UserCredential credential) throws IOException {
        return gitlabFactory.getAPI().createSSHKey(targetUserId, "By opscloud", credential.getCredential());
    }


}
