package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabProjectDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabUserDriver;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.workorder.delegate.GitlabUserDelegate;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 5:11 下午
 * @Version 1.0
 */
public class GitlabTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigHelper dsFactory;

    @Resource
    private GitlabUserDelegate gitlabUserDelegate;

    @Test
    void userTest() {
        GitlabConfig gitlabDsInstanceConfig = (GitlabConfig) getConfig();
        List<GitlabUser> userList = GitlabUserDriver.getUsers(gitlabDsInstanceConfig.getGitlab());
        for (GitlabUser gitlabUser : userList) {
            System.err.print(gitlabUser.getId());

        }
    }

    @Test
    void userSSKTest() {
        GitlabConfig gitlabDsInstanceConfig = (GitlabConfig) getConfig();
        try {
            List<GitlabSSHKey> keys = GitlabUserDriver.getUserSSHKeys(gitlabDsInstanceConfig.getGitlab(), 112);
            for (GitlabSSHKey key : keys) {
                System.err.print(key.getId());

            }
        } catch (IOException e) {

        }

    }

    @Test
    BaseDsConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(29);
        return dsFactory.build(datasourceConfig, GitlabConfig.class);
    }

    @Test
    void userSSK111Test() {
        GitlabConfig gitlabDsInstanceConfig = (GitlabConfig) getConfig();
        try {
            GitlabProjectDriver.addProjectMember(gitlabDsInstanceConfig.getGitlab(), 356, 135, GitlabAccessLevel.Owner);
        } catch (IOException e) {
            e.printStackTrace();
            print(e.getMessage());
        }
    }

    @Test
    void createUserTest() {
        GitlabConfig gitlabDsInstanceConfig = (GitlabConfig) getConfig();
        gitlabUserDelegate.createGitlabUser(gitlabDsInstanceConfig.getGitlab(), "xiuyuan");
    }

    @Test
    void aaaaTest() {
//        DatasourceConfig datasourceConfig = dsConfigService.getById(56);
//        GitlabConfig gitlabDsInstanceConfig = dsFactory.build(datasourceConfig, GitlabConfig.class);
//        try {
//            List<GitlabProjectMember> members = GitlabProjectDriver.getProjectMembers(gitlabDsInstanceConfig.getGitlab(), 73);
//            print(members);
//        } catch (IOException e) {
//
//        }
    }

}
