package com.baiyi.opscloud.ansible.provider;

import com.baiyi.opscloud.ansible.args.CommandArgs;
import com.baiyi.opscloud.ansible.args.PlaybookArgs;
import com.baiyi.opscloud.ansible.builder.AnsibleCommandArgsBuilder;
import com.baiyi.opscloud.ansible.builder.AnsiblePlaybookArgsBuilder;
import com.baiyi.opscloud.ansible.convert.AnsibleAssetConvert;
import com.baiyi.opscloud.ansible.handler.AnsibleHandler;
import com.baiyi.opscloud.ansible.model.AnsibleExecuteResult;
import com.baiyi.opscloud.ansible.model.AnsibleVersion;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AnsibleDsInstanceConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.collect.Lists;
import org.apache.commons.exec.CommandLine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
        return DsAssetTypeEnum.ANSIBLE_VERSION.getType();
    }

    private AnsibleDsInstanceConfig.Ansible buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, AnsibleDsInstanceConfig.class).getAnsible();
    }

    @Override
    protected List<AnsibleVersion.Version> listEntries(DsInstanceContext dsInstanceContext) {
        AnsibleDsInstanceConfig.Ansible ansible = buildConfig(dsInstanceContext.getDsConfig());
        List<AnsibleVersion.Version> versions = Lists.newArrayList();
        versions.add(getAnsibleVersion(ansible));
        versions.add(getAnsiblePlaybookVersion(ansible));
        return versions;
    }

    private AnsibleVersion.Version getAnsibleVersion(AnsibleDsInstanceConfig.Ansible ansible) {
        CommandArgs args = CommandArgs.builder()
                .version(true)
                .build();
        CommandLine commandLine = AnsibleCommandArgsBuilder.build(ansible, args);
        AnsibleExecuteResult er = AnsibleHandler.execute(commandLine, EXEC_TIMEOUT);
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

    private AnsibleVersion.Version getAnsiblePlaybookVersion(AnsibleDsInstanceConfig.Ansible ansible) {
        PlaybookArgs args = PlaybookArgs.builder()
                .version(true)
                .build();
        CommandLine commandLine = AnsiblePlaybookArgsBuilder.build(ansible, args);
        AnsibleExecuteResult er = AnsibleHandler.execute(commandLine, EXEC_TIMEOUT);
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
    @SingleTask(name = "PullAnsibleVersion", lockTime = "1m")
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, AnsibleVersion.Version entry) {
        return AnsibleAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ansibleVersionProvider);
    }
}
