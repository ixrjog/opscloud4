package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.common.util.GitlabTokenUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.InstanceConfigHelper;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.factory.gitlab.GitlabEventConsumerFactory;
import com.baiyi.opscloud.factory.gitlab.IGitlabEventConsumer;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/10/28 6:03 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitlabFacade extends BaseManager {

    private final DsConfigService dsConfigService;

    private final DsConfigHelper dsConfigHelper;

    /**
     * 支持SystemHooks标签的实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.GITLAB};

    private final InstanceConfigHelper instanceConfigHelper;

    public void consumeEventV4(GitLabNotifyParam.SystemHook systemHook) {
        if (StringUtils.isEmpty(GitlabTokenUtil.getToken())) {
            log.warn("未配置Gitlab SystemHooks SecretToken 无法路由消息!");
            return;
        }
        Optional<DatasourceInstance> optional = filterInstance();
        if (!optional.isPresent()) return; // 数据源配置文件未配置 SystemHooks.SecretToken
        IGitlabEventConsumer eventConsume = GitlabEventConsumerFactory.getByEventName(systemHook.getEvent_name());
        if (eventConsume != null) eventConsume.consumeEventV4(optional.get(), systemHook);

    }

    private Optional<DatasourceInstance> filterInstance() {
        List<DatasourceInstance> instances = super.listInstance();
        if (CollectionUtils.isEmpty(instances)) return Optional.empty();
        return instances.stream().filter(i -> {
            DatasourceConfig datasourceConfig = dsConfigService.getById(i.getConfigId());
            GitLabConfig gitlabDsInstanceConfig = dsConfigHelper.build(datasourceConfig, GitLabConfig.class);
            Optional<String> tokenOptional = Optional.ofNullable(gitlabDsInstanceConfig.getGitlab())
                    .map(GitLabConfig.Gitlab::getSystemHooks)
                    .map(GitLabConfig.SystemHooks::getToken);
            return tokenOptional.filter(s -> GitlabTokenUtil.getToken().equals(s))
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
