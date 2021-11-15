package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.common.datasource.GitlabDsInstanceConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.common.util.GitlabTokenUtil;
import com.baiyi.opscloud.datasource.InstanceConfigHelper;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitlabNotifyParam;
import com.baiyi.opscloud.factory.gitlab.GitlabEventConsumeFactory;
import com.baiyi.opscloud.factory.gitlab.IGitlabEventConsume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    private static final String SYSTEM_HOOKS_TAG = "SystemHooks";

    /**
     * 支持SystemHooks标签的实例类型
     */
    private static final DsTypeEnum[] filterInstanceTypes = {DsTypeEnum.GITLAB};

    private final InstanceConfigHelper instanceConfigHelper;

    public void consumeEventV4(GitlabNotifyParam.SystemHook systemHook) {
        try {
            if (StringUtils.isEmpty(GitlabTokenUtil.getToken())) {
                log.warn("未配置Gitlab:SystemHooks.SecretToken,无法路由消息！");
                return;
            }
            DatasourceInstance instance = filter();
            if (instance == null) return; // 未配置 SystemHooks.SecretToken
            IGitlabEventConsume eventConsume = GitlabEventConsumeFactory.getByEventName(systemHook.getEvent_name());
            if (eventConsume != null)
                eventConsume.consumeEventV4(instance, systemHook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DatasourceInstance filter() {
        List<DatasourceInstance> instances = super.listInstance();
        if (CollectionUtils.isEmpty(instances)) return null;
        Optional<DatasourceInstance> optional = instances.stream().filter(i -> {
            GitlabDsInstanceConfig gitlabDsInstanceConfig = (GitlabDsInstanceConfig) instanceConfigHelper.getConfig(i);
            String secretToken = Optional.ofNullable(gitlabDsInstanceConfig.getGitlab())
                    .map(GitlabDsInstanceConfig.Gitlab::getSystemHooks)
                    .map(GitlabDsInstanceConfig.SystemHooks::getToken)
                    .get();
            return GitlabTokenUtil.getToken().equals(secretToken);

        }).findFirst();
        return optional.get();
    }

    @Override
    protected DsTypeEnum[] getFilterInstanceTypes() {
        return filterInstanceTypes;
    }

    @Override
    protected String getTag() {
        return SYSTEM_HOOKS_TAG;
    }
}
