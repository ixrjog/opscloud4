package com.baiyi.opscloud.datasource.ansible.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.ansible.builder.AnsibleCommandArgsBuilder;
import com.baiyi.opscloud.datasource.ansible.builder.AnsiblePlaybookArgumentsBuilder;
import com.baiyi.opscloud.datasource.ansible.builder.args.AnsibleCommandArgs;
import com.baiyi.opscloud.datasource.ansible.builder.args.AnsiblePlaybookArgs;
import com.baiyi.opscloud.datasource.ansible.entity.AnsibleExecuteResult;
import com.baiyi.opscloud.datasource.ansible.entity.AnsibleVersion;
import com.baiyi.opscloud.datasource.ansible.executor.AnsibleExecutor;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.apache.commons.exec.CommandLine;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ANSIBLE_VERSION;

/**
 * @Author baiyi
 * @Date 2021/8/16 11:21 上午
 * @Version 1.0
 */
@Component
public class AnsibleVersionProvider extends BaseAssetProvider<AnsibleVersion.Version> {

    @Resource
    private AnsibleVersionProvider ansibleVersionProvider;

    private static final long EXEC_TIMEOUT = 2000L;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ANSIBLE.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ANSIBLE_VERSION.name();
    }

    private AnsibleConfig.Ansible buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AnsibleConfig.class).getAnsible();
    }

    @Override
    protected List<AnsibleVersion.Version> listEntities(DsInstanceContext dsInstanceContext) {
        AnsibleConfig.Ansible ansible = buildConfig(dsInstanceContext.getDsConfig());
        List<AnsibleVersion.Version> versions = Lists.newArrayList();
        versions.add(getAnsibleVersion(ansible));
        versions.add(getAnsiblePlaybookVersion(ansible));
        return versions;
    }

    private AnsibleVersion.Version getAnsibleVersion(AnsibleConfig.Ansible ansible) {
        AnsibleCommandArgs args = AnsibleCommandArgs.builder()
                .version(true)
                .build();
        CommandLine commandLine = AnsibleCommandArgsBuilder.build(ansible, args);
        AnsibleExecuteResult er = AnsibleExecutor.execute(commandLine, EXEC_TIMEOUT);
        return AnsibleVersion.Version.builder()
                .executableLocation(ansible.getAnsible())
                .details(er.getOutput().toString(StandardCharsets.UTF_8))
                .type(AnsibleVersion.VersionType.ANSIBLE)
                .build();
    }

    private AnsibleVersion.Version getAnsiblePlaybookVersion(AnsibleConfig.Ansible ansible) {
        AnsiblePlaybookArgs args = AnsiblePlaybookArgs.builder()
                .version(true)
                .build();
        CommandLine commandLine = AnsiblePlaybookArgumentsBuilder.build(ansible, args);
        AnsibleExecuteResult er = AnsibleExecutor.execute(commandLine, EXEC_TIMEOUT);
        return AnsibleVersion.Version.builder()
                .executableLocation(ansible.getPlaybook())
                .details(er.getOutput().toString(StandardCharsets.UTF_8))
                .type(AnsibleVersion.VersionType.ANSIBLE_PLAYBOOK)
                .build();
    }

    @Override
    @SingleTask(name = PULL_ANSIBLE_VERSION, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey()
                .compareOfKey2()
                .compareOfDescription()
                .compareOfActive()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ansibleVersionProvider);
    }

}