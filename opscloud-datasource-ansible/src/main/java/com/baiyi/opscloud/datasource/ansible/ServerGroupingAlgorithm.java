package com.baiyi.opscloud.datasource.ansible;

import com.baiyi.opscloud.algorithm.BaseAlgorithm;
import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.opscloud.common.base.Global.DEF_NUM_OF_GROUPS;

/**
 * 服务器算法
 *
 * @Author baiyi
 * @Date 2021/5/28 1:28 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerGroupingAlgorithm extends BaseAlgorithm {

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'server_intactgrouping_algorithm_servergroupid_' + #serverGroupId + 'is_subgroup_' + #isSubgroup")
    public void evictIntactGrouping(Integer serverGroupId, boolean isSubgroup) {
        log.info("清除缓存: evictIntactGrouping");
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'server_intactgrouping_algorithm_servergroupid_' + #serverGroup.id + 'is_subgroup_' + #isSubgroup", unless = "#result == null")
    public Map<String, List<ServerPack>> intactGrouping(ServerGroup serverGroup, boolean isSubgroup) {
        Map<String, List<ServerPack>> serverMap = groupingByEnv(serverGroup);
        if (isSubgroup) {
            groupingSubgroup(serverMap, getSubgroup(serverGroup));
        }
        return serverMap;
    }

    public Map<String, List<ServerPack>> groupingByEnv(ServerGroup serverGroup, boolean isSubgroup, int envType) {
        Map<String, List<ServerPack>> serverMap = groupingByEnv(serverGroup, envType);
        if (isSubgroup) {
            groupingSubgroup(serverMap, getSubgroup(serverGroup));
        }
        return serverMap;
    }

    private int getSubgroup(ServerGroup serverGroup) {
        ServerProperty.Server serverGroupProperty = getBusinessProperty(serverGroup);
        return Optional.ofNullable(serverGroupProperty)
                .map(ServerProperty.Server::getAnsible)
                .map(ServerProperty.Ansible::getSubgroup)
                .orElse(DEF_NUM_OF_GROUPS);
    }

    private void groupingSubgroup(Map<String, List<ServerPack>> serverMap, int subgroup) {
        if (serverMap.isEmpty()) {
            return;
        }
        Set<String> keySet = Sets.newHashSet(serverMap.keySet());
        keySet.forEach(k -> {
            List<ServerPack> servers = serverMap.get(k);
            if (servers.size() >= 2) {
                groupingSubgroup(serverMap, servers, k, subgroup);
            }
        });
    }

    /**
     * ServerTree 取服务器分组map，不含重复的主机分组模式
     * server-pord-1
     * server-pord-2
     * server-pord(不包含)
     *
     * @param serverGroup
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'server_grouping_algorithm_servergroupid_' + #serverGroup.id", unless = "#result == null")
    public Map<String, List<ServerPack>> grouping(ServerGroup serverGroup) {
        Map<String, List<ServerPack>> serverMap = groupingByEnv(serverGroup);
        if (serverMap.isEmpty()) {
            return serverMap;
        }
        // 分2组
        int subgroup = getSubgroup(serverGroup);
        Set<String> keSet = Sets.newHashSet(serverMap.keySet());
        for (String k : keSet) {
            List<ServerPack> servers = serverMap.get(k);
            if (servers.size() >= 2) {
                groupingSubgroup(serverMap, servers, k, subgroup);
                serverMap.remove(k);
            }
        }
        return serverMap;
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'server_grouping_algorithm_servergroupid_' + #serverGroupId")
    public void evictGrouping(Integer serverGroupId) {
    }

    private void groupingSubgroup(Map<String, List<ServerPack>> serverMap, List<ServerPack> servers, String groupingName, int subgroup) {
        List<ServerPack> preServerList = Lists.newArrayList(servers);
        // 服务器数量少于分组数量也只分2组
        if (subgroup > preServerList.size()) {
            subgroup = 2;
        }
        // 每组平均服务器数量
        int size = preServerList.size() / subgroup;
        int compensate = preServerList.size() % subgroup;
        int i = 1;
        while (!preServerList.isEmpty()) {
            List<ServerPack> subServerList = acqSubgroup(preServerList, compensate >= 1 ? size + 1 : size);
            serverMap.put(Joiner.on("-").join(groupingName, i), subServerList);
            compensate--;
            i++;
        }
    }

    /**
     * 取子组
     *
     * @param serverList 服务器列表
     * @param size       数量
     * @return
     */
    private List<ServerPack> acqSubgroup(List<ServerPack> serverList, int size) {
        List<ServerPack> subList = Lists.newArrayList(serverList.subList(0, size));
        serverList.subList(0, size).clear();
        return subList;
    }

}
