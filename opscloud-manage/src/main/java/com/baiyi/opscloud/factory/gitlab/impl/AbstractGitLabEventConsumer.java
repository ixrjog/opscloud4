package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.facade.datasource.SimpleDsAssetFacade;
import com.baiyi.opscloud.facade.event.EventFacade;
import com.baiyi.opscloud.factory.gitlab.GitLabEventConsumerFactory;
import com.baiyi.opscloud.factory.gitlab.IGitLabEventConsumer;
import com.baiyi.opscloud.factory.gitlab.converter.SystemHookConverter;
import com.baiyi.opscloud.factory.gitlab.enums.GitLabEventNameEnum;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.user.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/10/29 10:54 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractGitLabEventConsumer implements IGitLabEventConsumer, InitializingBean {

    @Resource
    private EventFacade eventFacade;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    protected SimpleDsAssetFacade simpleDsAssetFacade;

    @Resource
    private UserService userService;

    /**
     * 获取事件枚举
     *
     * @return
     */
    protected abstract GitLabEventNameEnum[] getEventNameEnums();

    // protected final ThreadLocal<GitlabEventContext> eventContext = ThreadLocal.withInitial(GitlabEventContext::new);

    @Override
    public List<String> getEventNames() {
        return Arrays.stream(getEventNameEnums()).map(e -> e.name().toLowerCase()).collect(Collectors.toList());
    }

    @Override
    @Async
    public void consumeEventV4(DatasourceInstance instance, GitLabNotifyParam.SystemHook systemHook) {
        Event event = SystemHookConverter.toEvent(instance, systemHook);
        eventFacade.recordEvent(event);
        process(instance, systemHook);
    }

    /**
     * TODO 这只是偷懒写法(全量同步)，最好重写
     */
    protected void process(DatasourceInstance instance, GitLabNotifyParam.SystemHook systemHook) {
        List<SimpleAssetProvider<?>> providers = AssetProviderFactory.getProviders(instance.getInstanceType(), getAssetType());
        assert providers != null;
        providers.forEach(x -> x.pullAsset(instance.getId()));
    }

    /**
     * 资产类型
     *
     * @return
     */
    protected abstract String getAssetType();

    @Override
    public void afterPropertiesSet() throws Exception {
        GitLabEventConsumerFactory.register(this);
    }

    protected Optional<User> getUser(DatasourceInstance instance, int gitLabUserId) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(instance.getUuid())
                .assetType(DsAssetTypeConstants.GITLAB_USER.name())
                .assetId(String.valueOf(gitLabUserId))
                .build();

        List<DatasourceInstanceAsset> assets = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (assets.size() == 1) {
            String username = assets.getFirst().getAssetKey();
            return Optional.ofNullable(userService.getByUsername(username));
        }
        return Optional.empty();
    }

}
