package com.baiyi.opscloud.datasource.sonar.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.SonarDsInstanceConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.sonar.convert.SonarAssetConvert;
import com.baiyi.opscloud.datasource.sonar.entry.SonarProjects;
import com.baiyi.opscloud.datasource.sonar.entry.base.BaseSonarElement;
import com.baiyi.opscloud.datasource.sonar.handler.SonarProjectsHandler;
import com.baiyi.opscloud.datasource.sonar.param.PagingParam;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/25 9:50 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class SonarProjectProvider extends BaseAssetProvider<BaseSonarElement.Project> {

    @Resource
    private SonarProjectsHandler sonarProjectsHandler;

    @Resource
    private SonarProjectProvider sonarProjectProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.SONAR.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.SONAR_PROJECT.getType();
    }

    private SonarDsInstanceConfig.Sonar buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, SonarDsInstanceConfig.class).getSonar();
    }

    @Override
    protected List<BaseSonarElement.Project> listEntries(DsInstanceContext dsInstanceContext) {
        SonarDsInstanceConfig.Sonar sonar = buildConfig(dsInstanceContext.getDsConfig());
        List<BaseSonarElement.Project> entries = Lists.newArrayList();
        PagingParam pagingParam = PagingParam.builder().build();
        sonarProjectsHandler.searchProjects(sonar, pagingParam);
        while (true) {
            SonarProjects sonarProjects = sonarProjectsHandler.searchProjects(sonar, pagingParam);
            List<BaseSonarElement.Project> components = sonarProjects.getComponents();
            if (CollectionUtils.isEmpty(components)) {
                break;
            }
            entries.addAll(components);
            if (components.size() < pagingParam.getPs()) {
                break;
            } else {
                pagingParam.setP(pagingParam.getP() + 1);
            }
        }
        return entries;
    }

    @Override
    @SingleTask(name = "pull_sonar_project", lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, BaseSonarElement.Project entry) {
        return SonarAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(sonarProjectProvider);
    }
}


