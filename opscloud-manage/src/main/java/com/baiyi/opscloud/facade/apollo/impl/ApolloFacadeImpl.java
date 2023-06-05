package com.baiyi.opscloud.facade.apollo.impl;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.ApolloFacade;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/5/30 14:10
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
@AllArgsConstructor
@Component
public class ApolloFacadeImpl implements ApolloFacade {

    private final DsConfigService dsConfigService;

    private final DsConfigHelper dsConfigHelper;

    @Override
    public HttpResult interceptRelease(ApolloParam.ReleaseEvent releaseEvent) {
        List<DatasourceConfig> dsConfigs = dsConfigService.queryByDsType(DsTypeEnum.APOLLO.getType());
        Optional<ApolloConfig> optionalConfig = dsConfigs.stream()
                .map(config -> dsConfigHelper.build(config, ApolloConfig.class))
                .toList()
                .stream()
                .filter(e -> e.getApollo().filter(releaseEvent.getToken()))
                .findAny();

        if (optionalConfig.isEmpty()) {
            log.debug("Invalid Apollo interceptor token");
            return HttpResult.SUCCESS;
        }

        ApolloConfig.Apollo apolloConfig = optionalConfig.get().getApollo();

        // 需要拦截的环境
        List<String> envs = Optional.of(apolloConfig)
                .map(ApolloConfig.Apollo::getPortal)
                .map(ApolloConfig.Portal::getRelease)
                .map(ApolloConfig.Release::getInterceptor)
                .map(ApolloConfig.Interceptor::getEnvs)
                .orElse(Collections.emptyList());
        // 环境配置不存在
        if (CollectionUtils.isEmpty(envs)) {
            log.debug("Interceptor environment configuration is empty");
            return HttpResult.SUCCESS;
        }
        // TODO 白名单规则校验



        return HttpResult.SUCCESS;

    }

}
