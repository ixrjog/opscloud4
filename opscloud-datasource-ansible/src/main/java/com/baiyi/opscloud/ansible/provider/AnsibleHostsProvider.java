package com.baiyi.opscloud.ansible.provider;

import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.ansible.ServerGroupingAlgorithm;
import com.baiyi.opscloud.ansible.convert.AnsibleAssetConvert;
import com.baiyi.opscloud.ansible.model.AnsibleHosts;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AnsibleDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAnsibleConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/8/16 5:57 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsibleHostsProvider extends BaseAssetProvider<AnsibleHosts.Hosts> {

    @Resource
    private AnsibleHostsProvider ansibleHostsProvider;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ServerGroupingAlgorithm serverGroupingAlgorithm;

    @Resource
    private ServerService serverService;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ANSIBLE.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ANSIBLE_HOSTS.getType();
    }

    private DsAnsibleConfig.Ansible buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, AnsibleDsInstanceConfig.class).getAnsible();
    }

    @Override
    protected List<AnsibleHosts.Hosts> listEntries(DsInstanceContext dsInstanceContext) {
        DsAnsibleConfig.Ansible ansible = buildConfig(dsInstanceContext.getDsConfig());
        List<AnsibleHosts.Hosts> hosts = Lists.newArrayList();
        Credential credential = getCredential(dsInstanceContext.getDsConfig().getCredentialId());
        hosts.add(buildHosts(credential.getUsername(), ansible.getInventoryHost()));
        return hosts;
    }

    private AnsibleHosts.Hosts buildHosts(String sshUser, String inventoryHost) {
        ServerGroupParam.ServerGroupPageQuery pageQuery = ServerGroupParam.ServerGroupPageQuery.builder()
                .build();
        pageQuery.setPage(1);
        pageQuery.setLength(10000);
        List<AnsibleHosts.Group> groups = Lists.newArrayList();
        DataTable<ServerGroup> table = serverGroupService.queryPageByParam(pageQuery);
        for (ServerGroup e : table.getData()) {
            int serverSize = serverService.countByServerGroupId(e.getId());
            if (serverSize > 0) {
                Map<String, List<ServerPack>> serverSubgroup = serverGroupingAlgorithm.intactGrouping(e, true); // 包含环境整组
                AnsibleHosts.Group group = AnsibleHosts.Group.builder()
                        .serverGroup(e)
                        .serverMap(serverSubgroup)
                        .sshUser(sshUser)
                        .build();
                groups.add(group);
            }
        }
        return AnsibleHosts.Hosts.builder()
                .groups(groups)
                .inventoryHost(inventoryHost)
                .build();
    }

    @Override
    @SingleTask(name = "PullAnsibleHosts", lockTime = "1m")
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, AnsibleHosts.Hosts entry) {
        return AnsibleAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ansibleHostsProvider);
    }
}
