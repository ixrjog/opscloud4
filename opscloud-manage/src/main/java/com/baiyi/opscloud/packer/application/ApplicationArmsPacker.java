package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunArmsConfig;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/6/27 15:01
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationArmsPacker {

    private final DsInstanceService dsInstanceService;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsConfigManager dsConfigManager;

    public void warp(Env env, ApplicationVO.Kubernetes kubernetes) {
        List<DatasourceInstance> instances = dsInstanceService.listByInstanceType(DsTypeEnum.ALIYUN_ARMS.name());
        for (DatasourceInstance instance : instances) {
            DatasourceInstanceAsset query = DatasourceInstanceAsset.builder()
                    .instanceUuid(instance.getUuid())
                    .name(Joiner.on("-").join(kubernetes.getName(), env.getEnvName()))
                    .build();
            List<DatasourceInstanceAsset> assets = dsInstanceAssetService.queryAssetByAssetParam(query);
            if (!CollectionUtils.isEmpty(assets) && assets.size() == 1) {
                DatasourceConfig datasourceConfig = dsConfigManager.getConfigById(instance.getConfigId());
                AliyunArmsConfig armsConfig = dsConfigManager.build(datasourceConfig, AliyunArmsConfig.class);
                Optional<String> optionalHomeUrl = Optional.of(armsConfig.getArms())
                        .map(AliyunArmsConfig.Arms::getUrl)
                        .map(AliyunArmsConfig.Url::getHome);
                if (optionalHomeUrl.isEmpty()) {
                    continue;
                }

                final String regionId = Optional.of(armsConfig.getArms())
                        .map(AliyunArmsConfig.Arms::getRegionId)
                        .orElse("cn-hangzhou");

                String homeUrl = optionalHomeUrl.get();
                String pid = assets.getFirst().getAssetKey();

                ApplicationVO.ArmsTraceApp traceApp = ApplicationVO.ArmsTraceApp.builder()
                        .show(true)
                        .homeUrl(format(homeUrl, pid, regionId, pid))
                        .build();
                kubernetes.setArmsTraceApp(traceApp);
                return;
            }
        }

        ApplicationVO.ArmsTraceApp traceApp = ApplicationVO.ArmsTraceApp.builder()
                .show(false)
                .build();
        kubernetes.setArmsTraceApp(traceApp);

    }

    private String format(String message, Object... var2) {
        return StringFormatter.arrayFormat(message, var2);
    }

}
