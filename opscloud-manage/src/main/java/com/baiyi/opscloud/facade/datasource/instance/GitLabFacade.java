package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.factory.gitlab.GitLabEventConsumerFactory;
import com.baiyi.opscloud.factory.gitlab.IGitLabEventConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/28 6:03 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitLabFacade {

    private final GitLabInstanceStore gitLabInstanceStore;

    public void consumeEventV4(GitLabNotifyParam.SystemHook systemHook, String gitLabToken) {
        if (StringUtils.isBlank(systemHook.getEvent_name())) {
            // 未知的事件名称
            return;
        }
        if (StringUtils.isBlank(gitLabToken)) {
            log.debug("未配置 gitLab system hooks secret token 无法路由消息!");
            return;
        }
        DatasourceInstance datasourceInstance = gitLabInstanceStore.getGitLabInstance(gitLabToken);
        if (datasourceInstance == null) {
            // 数据源配置文件未配置 SystemHooks.SecretToken
            return;
        }
        IGitLabEventConsumer gitLabEventConsumer = GitLabEventConsumerFactory.getByEventName(systemHook.getEvent_name());
        if (gitLabEventConsumer != null) {
            gitLabEventConsumer.consumeEventV4(datasourceInstance, systemHook);
        }
    }

}
