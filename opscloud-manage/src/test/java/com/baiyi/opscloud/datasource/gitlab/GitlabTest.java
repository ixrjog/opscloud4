package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabUserDriver;
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
    private DsConfigHelper dsFactory;


    @Test
    void userTest() {
        GitlabConfig gitlabDsInstanceConfig = (GitlabConfig) getConfig();
        List<GitlabUser> userList = GitlabUserDriver.queryUsers(gitlabDsInstanceConfig.getGitlab());
        for (GitlabUser gitlabUser : userList) {
            System.err.print(gitlabUser.getId());

        }
    }

    @Test
    void userSSKTest() {
        GitlabConfig gitlabDsInstanceConfig = (GitlabConfig) getConfig();
        try {
            List<GitlabSSHKey> keys = GitlabUserDriver.getUserSSHKeys(gitlabDsInstanceConfig.getGitlab(),112);
            for (GitlabSSHKey key : keys) {
                System.err.print(key.getId());

            }
        }catch (IOException e){

        }

    }

    @Test
    BaseConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(2);
        return dsFactory.build(datasourceConfig, GitlabConfig.class);
    }
}
