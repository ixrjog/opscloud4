package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/6/29 10:55
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitLabInstanceStore extends BaseManager {

    private final DsConfigService dsConfigService;

    private final DsConfigManager dsConfigManager;

    /**
     * 支持SystemHooks标签的实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.GITLAB};

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10M, key = "'GITLAB#INSTANCE#TOKEN:' + #token", unless = "#result == null")
    public DatasourceInstance getGitLabInstance(String token) {
        Optional<DatasourceInstance> optional = filterInstance(token);
        // 数据源配置文件未配置 SystemHooks.SecretToken
        return optional.orElse(null);
    }

    private Optional<DatasourceInstance> filterInstance(String token) {
        List<DatasourceInstance> instances = super.listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            return Optional.empty();
        }
        return instances.stream().filter(i -> {
            DatasourceConfig datasourceConfig = dsConfigService.getById(i.getConfigId());
            GitLabConfig gitlabDsInstanceConfig = dsConfigManager.build(datasourceConfig, GitLabConfig.class);
            Optional<String> tokenOptional = Optional.ofNullable(gitlabDsInstanceConfig.getGitlab())
                    .map(GitLabConfig.GitLab::getSystemHooks)
                    .map(GitLabConfig.SystemHooks::getToken);
            return tokenOptional.filter(token::equals)
                    .isPresent();
        }).findFirst();
    }

    @Override
    protected DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    @Override
    protected String getTag() {
        return TagConstants.SYSTEM_HOOKS.getTag();
    }


}
