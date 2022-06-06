package com.baiyi.opscloud.datasource.ansible.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.ansible.builder.args.AnsibleArgs;
import com.baiyi.opscloud.datasource.ansible.builder.AnsibleCommandArgsBuilder;
import com.baiyi.opscloud.datasource.ansible.builder.AnsiblePlaybookArgumentsBuilder;
import com.baiyi.opscloud.datasource.ansible.entity.AnsibleExecuteResult;
import com.baiyi.opscloud.datasource.ansible.entity.AnsibleVersion;
import com.baiyi.opscloud.datasource.ansible.executor.AnsibleExecutor;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.google.common.collect.Lists;
import org.apache.commons.exec.CommandLine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
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
        return dsConfigHelper.build(dsConfig, AnsibleConfig.class).getAnsible();
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
        AnsibleArgs.Command args = AnsibleArgs.Command.builder()
                .version(true)
                .build();
        CommandLine commandLine = AnsibleCommandArgsBuilder.build(ansible, args);
        AnsibleExecuteResult er = AnsibleExecutor.execute(commandLine, EXEC_TIMEOUT);
        try {
            return AnsibleVersion.Version.builder()
                    .executableLocation(ansible.getAnsible())
                    .details(er.getOutput().toString("utf8"))
                    .type(AnsibleVersion.VersionType.ANSIBLE)
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new CommonRuntimeException("AnsibleVersion执行错误！");
        }
    }

    private AnsibleVersion.Version getAnsiblePlaybookVersion(AnsibleConfig.Ansible ansible) {
        AnsibleArgs.Playbook args = AnsibleArgs.Playbook.builder()
                .version(true)
                .build();
        CommandLine commandLine = AnsiblePlaybookArgumentsBuilder.build(ansible, args);
        AnsibleExecuteResult er = AnsibleExecutor.execute(commandLine, EXEC_TIMEOUT);
        try {
            return AnsibleVersion.Version.builder()
                    .executableLocation(ansible.getPlaybook())
                    .details(er.getOutput().toString("utf8"))
                    .type(AnsibleVersion.VersionType.ANSIBLE_PLAYBOOK)
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new CommonRuntimeException("AnsibleVersion执行错误！");
        }
    }

    @Override
    @SingleTask(name = PULL_ANSIBLE_VERSION, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ansibleVersionProvider);
    }
}
