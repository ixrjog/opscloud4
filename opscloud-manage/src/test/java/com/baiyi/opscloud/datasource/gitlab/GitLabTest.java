package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.workorder.delegate.GitLabUserDelegate;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/21 5:11 下午
 * @Version 1.0
 */
public class GitLabTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigHelper dsFactory;

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



}
