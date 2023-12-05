package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.workorder.delegate.GitLabUserDelegate;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/6/21 5:11 下午
 * @Version 1.0
 */
public class GitLabTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigManager dsFactory;

    @Resource
    private GitLabUserDelegate gitlabUserDelegate;

    @Test
    BaseDsConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(29);
        return dsFactory.build(datasourceConfig, GitLabConfig.class);
    }

    @Test
    void createUserTest() {
        GitLabConfig gitlabDsInstanceConfig = (GitLabConfig) getConfig();
        gitlabUserDelegate.createUser(gitlabDsInstanceConfig.getGitlab(), "xiuyuan");
    }

    @Test
    public void testConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(79);
        GitLabConfig c = dsFactory.build(datasourceConfig, GitLabConfig.class);
        print(c);
    }

    @Test
    public void testC() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(29);
        GitLabConfig c = dsFactory.build(datasourceConfig, GitLabConfig.class);
//        try {
//          //  CompareResults cr = GitLabExampleDriver.compare(c.getGitlab(), 791L, "d6bc4c68", "4a8a2382");
//          //  print(cr);
//        } catch (GitLabApiException e) {
//            e.printStackTrace();
//        }

    }


}
