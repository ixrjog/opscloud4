package com.baiyi.opscloud.datasource.ansible;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.baiyi.opscloud.common.base.Global.DEF_NUM_OF_GROUPS;

/**
 * @Author baiyi
 * @Date 2021/8/16 6:46 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupingTool {

    private final ServerService serverService;

    private final EnvService envService;

    /**
     * 清空缓存
     */
    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'grouping_' + #serverGroupId", beforeInvocation = true)
    public void evictGrouping1(Integer serverGroupId) {
        log.info("evictBuild 清除缓存，serverGroupId={}", serverGroupId);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'grouping_' + #serverGroup.id")
    public Map<String, List<Server>> grouping(ServerGroup serverGroup) {
        return grouping(serverGroup, true);
    }

    /**
     * 取服务器分组map (可单独对外提供分组配置)
     * 完整的包含 环境全组数据
     *
     * @param serverGroup
     * @param isSubgroup
     * @return
     */
    public Map<String, List<Server>> grouping(ServerGroup serverGroup, boolean isSubgroup) {
        Map<String, List<Server>> serverMap = groupingByEnv(serverGroup);
        if (isSubgroup) {
            groupingSubgroup(serverMap, getSubgroup(serverGroup));
        }
        return serverMap;
    }

    private int getSubgroup(ServerGroup serverGroup) {
        return DEF_NUM_OF_GROUPS;
    }

    private void groupingSubgroup(Map<String, List<Server>> serverMap, int subgroup) {
        if (serverMap.isEmpty()) {
            return;
        }
        Set<String> keySet = Sets.newHashSet(serverMap.keySet());
        keySet.forEach(k -> {
            List<Server> servers = serverMap.get(k);
            if (servers.size() >= 2) {
                groupingSubgroup(serverMap, servers, k, subgroup);
            }
        });
    }

    private void groupingSubgroup(Map<String, List<Server>> serverMap, List<Server> servers, String groupingName, int subgroup) {
        List<Server> preServers = Lists.newArrayList(servers);
        // 服务器数量少于分组数量也只分2组
        if (subgroup > preServers.size()) {
            subgroup = 2;
        }
        // 每组平均服务器数量
        int size = preServers.size() / subgroup;
        int compensate = preServers.size() % subgroup;
        int i = 1;
        while (!preServers.isEmpty()) {
            List<Server> subServerList = acqSubgroup(preServers, compensate >= 1 ? size + 1 : size);
            serverMap.put(groupingName + "-" + i, subServerList);
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
    private List<Server> acqSubgroup(List<Server> serverList, int size) {
        List<Server> subList = Lists.newArrayList(serverList.subList(0, size));
        serverList.subList(0, size).clear();
        return subList;
    }

    /**
     * 按环境分组
     *
     * @param serverGroup
     * @return
     */
    private Map<String, List<Server>> groupingByEnv(ServerGroup serverGroup) {
        List<Server> serverList = serverService.queryByServerGroupId(serverGroup.getId());
        return groupingByEnv(serverGroup, serverList);
    }

    protected Map<String, List<Server>> groupingByEnv(ServerGroup serverGroup, int envType) {
        List<Server> servers = serverService.queryByGroupIdAndEnvType(serverGroup.getId(), envType);
        return groupingByEnv(serverGroup, servers);
    }

    protected Map<String, List<Server>> groupingByEnv(ServerGroup ocServerGroup, List<Server> servers) {
        Map<String, List<Server>> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(servers)) {
            return map;
        }
        servers.forEach(e -> {
            String groupingName = toSubgroupName(ocServerGroup, e.getEnvType());
            if (map.containsKey(groupingName)) {
                map.get(groupingName).add(e);
            } else {
                List<Server> list = Lists.newArrayList();
                list.add(e);
                map.put(groupingName, list);
            }
        });
        return map;
    }

    protected String toSubgroupName(ServerGroup ocServerGroup, int envType) {
        return Joiner.on("-").join(ocServerGroup.getName().replace("group_", ""), toEnvName(envType));
    }

    protected String toEnvName(int envType) {
        return envService.getByEnvType(envType).getEnvName();
    }

}
