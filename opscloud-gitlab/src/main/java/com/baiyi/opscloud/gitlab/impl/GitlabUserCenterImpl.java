package com.baiyi.opscloud.gitlab.impl;

import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.common.util.SSHUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.gitlab.GitlabUserCenter;
import com.baiyi.opscloud.gitlab.handler.GitlabUserHandler;
import org.gitlab.api.models.CreateUserRequest;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:22 下午
 * @Version 1.0
 */
@Component("GitlabUserCenter")
public class GitlabUserCenterImpl implements GitlabUserCenter {

    @Resource
    private GitlabUserHandler gitlabUserHandler;

    @Resource
    private StringEncryptor stringEncryptor;

    public static final String BRANCH_REF = "refs/heads/";

    @Override
    public boolean pushKey(OcUser ocUser, OcAccount ocAccount, UserCredentialVO.UserCredential credential) {
        List<GitlabSSHKey> tryKeys =
                getUserKeys(ocAccount).stream().filter(key ->
                        SSHUtils.getFingerprint(key.getKey()).equals(credential.getFingerprint())
                ).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tryKeys)) return true;
        try {
            gitlabUserHandler.createSSHKey(Integer.parseInt(ocAccount.getAccountId()), credential);
            return gitlabUserHandler.createSSHKey(Integer.parseInt(ocAccount.getAccountId()), credential) != null;
        } catch (IOException e) {
            return false;
        }

    }

    private List<GitlabSSHKey> getUserKeys(OcAccount ocAccount) {
        try {
            List<GitlabSSHKey> keys = gitlabUserHandler.getUserSSHKeys(Integer.parseInt(ocAccount.getAccountId()));
            return keys;
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public GitlabUser createUser(OcUser ocUser, String userDN) {
        CreateUserRequest createUserReq = new CreateUserRequest(ocUser.getDisplayName(), ocUser.getUsername(), ocUser.getEmail());
        createUserReq.setPassword(getPassword(ocUser));
        createUserReq.setProjectsLimit(10000);
        createUserReq.setExternUid(userDN);
        createUserReq.setProvider("ldapmain");
        createUserReq.setAdmin(false);
        createUserReq.setCanCreateGroup(true);
        createUserReq.setSkipConfirmation(true);
        createUserReq.setExternal(false);

        try {
            return gitlabUserHandler.createUser(createUserReq);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPassword(OcUser ocUser) {
        if (StringUtils.isEmpty(ocUser.getPassword())) {
            return PasswordUtils.getRandomPW(16);
        } else {
            return stringEncryptor.decrypt(ocUser.getPassword());
        }
    }

}
