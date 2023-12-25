package com.baiyi.opscloud.datasource.sonar.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.SonarConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.sonar.driver.SonarProjectsDriver;
import com.baiyi.opscloud.datasource.sonar.entity.SonarProjects;
import com.baiyi.opscloud.datasource.sonar.entity.base.BaseSonarElement;
import com.baiyi.opscloud.datasource.sonar.param.PagingParam;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_SONAR_PROJECT;

/**
 * @Author baiyi
 * @Date 2021/10/25 9:50 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class SonarProjectProvider extends BaseAssetProvider<BaseSonarElement.Project> {

    @Resource
    private SonarProjectsDriver sonarProjectsDriver;

    @Resource
    private SonarProjectProvider sonarProjectProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.SONAR.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.SONAR_PROJECT.name();
    }

    private SonarConfig.Sonar buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, SonarConfig.class).getSonar();
    }

    @Override
    protected List<BaseSonarElement.Project> listEntities(DsInstanceContext dsInstanceContext) {
        SonarConfig.Sonar sonar = buildConfig(dsInstanceContext.getDsConfig());
        List<BaseSonarElement.Project> entities = Lists.newArrayList();
        PagingParam pagingParam = PagingParam.builder().build();
        sonarProjectsDriver.searchProjects(sonar, pagingParam);
        while (true) {
            SonarProjects sonarProjects = sonarProjectsDriver.searchProjects(sonar, pagingParam);
            List<BaseSonarElement.Project> components = sonarProjects.getComponents();
            if (CollectionUtils.isEmpty(components)) {
                break;
            }
            entities.addAll(components);
            if (components.size() < pagingParam.getPs()) {
                break;
            } else {
                pagingParam.setP(pagingParam.getP() + 1);
            }
        }
        return entities;
    }

    @Override
    @SingleTask(name = PULL_SONAR_PROJECT, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfActive()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(sonarProjectProvider);
    }

}