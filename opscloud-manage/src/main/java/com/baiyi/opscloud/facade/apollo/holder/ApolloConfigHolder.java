package com.baiyi.opscloud.facade.apollo.holder;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/11/13 09:18
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ApolloConfigHolder {

    private final DsConfigService dsConfigService;

    private final DsConfigManager dsConfigManager;

    public Optional<ApolloConfig> getConfigByToken(String token) {
        List<DatasourceConfig> dsConfigs = dsConfigService.queryByDsType(DsTypeEnum.APOLLO.getType());
        // 通过Token过滤实例
        return dsConfigs.stream()
                .map(config -> dsConfigManager.build(config, ApolloConfig.class))
                .toList()
                .stream()
                .filter(e -> e.getApollo().filter(token))
                .findAny();
    }

}