package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.util.GitLabTokenUtil;
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

    /**
     * 支持SystemHooks标签的实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.GITLAB};

    public void consumeEventV4(GitLabNotifyParam.SystemHook systemHook) {
        if (StringUtils.isBlank(systemHook.getEvent_name())) {
            // 未知的事件名称
            return;
        }
        if (StringUtils.isEmpty(GitLabTokenUtil.getToken())) {
            log.debug("未配置 gitLab system hooks secret token 无法路由消息!");
            return;
        }
        DatasourceInstance datasourceInstance = gitLabInstanceStore.getGitLabInstance(GitLabTokenUtil.getToken());
        if (datasourceInstance == null) {
            // 数据源配置文件未配置 SystemHooks.SecretToken
            return;
        }
        IGitLabEventConsumer eventConsume = GitLabEventConsumerFactory.getByEventName(systemHook.getEvent_name());
        if (eventConsume != null) {
            eventConsume.consumeEventV4(datasourceInstance , systemHook);
        }
    }

}
