package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitlabDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.core.factory.DsConfigFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.datasource.gitlab.handler.GitlabUserHandler;
import com.baiyi.opscloud.service.datasource.DsConfigService;
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
    private DsConfigFactory dsFactory;


    @Test
    void userTest() {
        GitlabDsInstanceConfig gitlabDsInstanceConfig = (GitlabDsInstanceConfig) getConfig();
        List<GitlabUser> userList = GitlabUserHandler.queryUsers(gitlabDsInstanceConfig.getGitlab());
        for (GitlabUser gitlabUser : userList) {
            System.err.print(gitlabUser.getId());

        }
    }

    @Test
    void userSSKTest() {
        GitlabDsInstanceConfig gitlabDsInstanceConfig = (GitlabDsInstanceConfig) getConfig();
        try {
            List<GitlabSSHKey> keys = GitlabUserHandler.getUserSSHKeys(gitlabDsInstanceConfig.getGitlab(),112);
            for (GitlabSSHKey key : keys) {
                System.err.print(key.getId());

            }
        }catch (IOException e){

        }

    }

    @Test
    BaseDsInstanceConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(2);
        return dsFactory.build(datasourceConfig, GitlabDsInstanceConfig.class);
    }
}
