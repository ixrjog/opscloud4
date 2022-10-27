package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.workorder.delegate.GitlabUserDelegate;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

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
    BaseDsConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(29);
        return dsFactory.build(datasourceConfig, GitlabConfig.class);
    }

    @Test
    void createUserTest() {
        GitlabConfig gitlabDsInstanceConfig = (GitlabConfig) getConfig();
        gitlabUserDelegate.createGitlabUser(gitlabDsInstanceConfig.getGitlab(), "xiuyuan");
    }

}
