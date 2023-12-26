package com.baiyi.opscloud.datasource.jenkins.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.jenkins.converter.JenkinsAssetConverter;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.model.FolderJob;
import com.baiyi.opscloud.datasource.jenkins.model.Job;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_JENKINS_TEMPLATE;

/**
 * @Author baiyi
 * @Date 2022/10/31 19:35
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsTemplateProvider extends BaseAssetProvider<Job> {

    @Resource
    private JenkinsTemplateProvider jenkinsTemplateProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.JENKINS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.JENKINS_TEMPLATE.name();
    }

    private JenkinsConfig.Jenkins buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, JenkinsConfig.class).getJenkins();
    }

    @Override
    protected List<Job> listEntities(DsInstanceContext dsInstanceContext) {
        List<Job> entities = Lists.newArrayList();
        JenkinsConfig.Jenkins config = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> templateFolders = Optional.ofNullable(config.getTemplate()).map(JenkinsConfig.Template::getFolders).orElse(JenkinsConfig.Template.DEF_FOLDERS);
        String prefix = Optional.ofNullable(config.getTemplate()).map(JenkinsConfig.Template::getPrefix).orElse(JenkinsConfig.Template.DEF_PREFIX);
        try {
            // 遍历所有模板目录
            for (String templateFolder : templateFolders) {
                // https://leo.jenkins.org/job/templates/
                FolderJob folder = new FolderJob(templateFolder, Joiner.on("/").join(config.getUrl(), "job", templateFolder, ""));
                Map<String, Job> jobMap = JenkinsServerDriver.getJobs(config, folder);
                for (String k : jobMap.keySet()) {
                    if (jobMap.get(k).getName().startsWith(prefix)) {
                        entities.add(jobMap.get(k));
                    }
                }
            }
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = PULL_JENKINS_TEMPLATE, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparer.COMPARE_NAME;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Job entity) {
        return JenkinsAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(jenkinsTemplateProvider);
    }

}