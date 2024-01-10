package com.baiyi.opscloud.datasource.ansible.provider;

import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.ansible.ServerGroupingAlgorithm;
import com.baiyi.opscloud.datasource.ansible.entity.AnsibleHosts;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ANSIBLE_HOSTS;

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
        return DsAssetTypeConstants.ANSIBLE_HOSTS.name();
    }

    private AnsibleConfig.Ansible buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AnsibleConfig.class).getAnsible();
    }

    @Override
    protected List<AnsibleHosts.Hosts> listEntities(DsInstanceContext dsInstanceContext) {
        AnsibleConfig.Ansible ansible = buildConfig(dsInstanceContext.getDsConfig());
        List<AnsibleHosts.Hosts> hosts = Lists.newArrayList();
        Credential credential = getCredential(dsInstanceContext.getDsConfig().getCredentialId());
        hosts.add(buildHosts(credential.getUsername(), ansible.getInventoryHost()));
        return hosts;
    }

    private AnsibleHosts.Hosts buildHosts(String sshUser, String inventoryHost) {
        ServerGroupParam.ServerGroupPageQuery pageQuery = ServerGroupParam.ServerGroupPageQuery.builder()
                .page(1)
                .length(10000)
                .build();
        List<AnsibleHosts.Group> groups = Lists.newArrayList();
        DataTable<ServerGroup> table = serverGroupService.queryPageByParam(pageQuery);
        for (ServerGroup e : table.getData()) {
            int serverSize = serverService.countByServerGroupId(e.getId());
            if (serverSize > 0) {
                // 包含环境整组
                Map<String, List<ServerPack>> serverSubgroup = serverGroupingAlgorithm.intactGrouping(e, true);
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
    @SingleTask(name = PULL_ANSIBLE_HOSTS, lockTime = "1m")
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
        AssetProviderFactory.register(ansibleHostsProvider);
    }

}
