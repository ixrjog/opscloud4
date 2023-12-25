package com.baiyi.opscloud.datasource.jenkins.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.converter.JenkinsAssetConverter;
import com.baiyi.opscloud.datasource.jenkins.model.Computer;
import com.baiyi.opscloud.datasource.jenkins.model.ComputerWithDetails;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_JENKINS_COMPUTER;

/**
 * @Author baiyi
 * @Date 2021/7/2 9:59 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsComputerProvider extends BaseAssetProvider<ComputerWithDetails> {

    @Resource
    private JenkinsComputerProvider jenkinsComputerProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.JENKINS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.JENKINS_COMPUTER.name();
    }

    private JenkinsConfig.Jenkins buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, JenkinsConfig.class).getJenkins();
    }

    @Override
    protected List<ComputerWithDetails> listEntities(DsInstanceContext dsInstanceContext) {
        JenkinsConfig.Jenkins jenkins = buildConfig(dsInstanceContext.getDsConfig());
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            Map<String, Computer> computerMap = jenkinsServer.getComputers();
            List<ComputerWithDetails> computerWithDetails = Lists.newArrayList();
            for (String k : computerMap.keySet()) {
                computerWithDetails.add(computerMap.get(k).details());
            }
            return computerWithDetails;
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = PULL_JENKINS_COMPUTER, lockTime = "5m")
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ComputerWithDetails entity) {
        return JenkinsAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(jenkinsComputerProvider);
    }

}