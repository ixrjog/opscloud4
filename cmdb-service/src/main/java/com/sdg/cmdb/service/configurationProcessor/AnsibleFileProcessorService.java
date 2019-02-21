package com.sdg.cmdb.service.configurationProcessor;


import com.sdg.cmdb.domain.config.PreviewConfig;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.CacheKeyService;
import com.sdg.cmdb.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AnsibleFileProcessorService extends ConfigurationProcessorAbs {

    public static final String CACHE_KEY = "AnsibleFileProcessorService:";

    @Autowired
    private CacheKeyService cacheKeyService;


    private String getCacheKey(ServerGroupDO serverGroupDO, Boolean isSubgroup) {
        return CACHE_KEY + "id:" + serverGroupDO.getId() + ":isSubgroup:" + isSubgroup.toString();
    }


    public void delCache(ServerDO serverDO) {
        delCache(new ServerGroupDO(serverDO.getServerGroupId()));
    }

    public void delCache(ServerGroupDO serverGroupDO) {
        cacheKeyService.del(getCacheKey(serverGroupDO, true));
        cacheKeyService.del(getCacheKey(serverGroupDO, false));
    }

    /**
     * 全局配置文件，只分环境不分组
     *
     * @return
     */
    public String getConfig() {
        return getConfig(0, false);
    }

    /**
     * useType = 0 取all
     *
     * @param useType
     * @return
     */
    public String getConfig(int useType, boolean isSubgroup) {
        List<ServerGroupDO> serverGroups = new ArrayList<>();
        if (useType == 0) {
            serverGroups = serverGroupDao.queryServerGroup();
        } else {
            serverGroups = serverGroupDao.queryServerGroupByUseType(useType);
        }
        return build(serverGroups, isSubgroup);
    }

    private String build(List<ServerGroupDO> serverGroups, boolean isSubgroup) {
        String result = getHeadInfo();
        for (ServerGroupDO serverGroup : serverGroups) {
            String cacheKey = getCacheKey(serverGroup, isSubgroup);
            String cacheConfig = cacheKeyService.getKeyByString(cacheKey);
            if (!StringUtils.isEmpty(cacheConfig)) {
                result += cacheConfig;
                continue;
            }
            //if (!configServerGroupService.isTomcatServer(serverGroup)) continue;
            List<ServerDO> hosts = serverDao.acqServersByGroupId(serverGroup.getId());
            if (hosts.isEmpty())
                continue;
            Map<String, List<ServerDO>> serverMap = grouping(serverGroup, isSubgroup);
            result += format(serverGroup, serverMap, cacheKey);
        }
        return result;
    }

    public void preview(long serverGroupId, List<PreviewConfig> configList) {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
        Map<String, List<ServerDO>> serverMap = grouping(serverGroupDO, true);
        String config = format(serverGroupDO, serverMap, getCacheKey(serverGroupDO, true));
        if(StringUtils.isEmpty(config)) return ;
        PreviewConfig ansibleConfig = new PreviewConfig("Ansible主机配置",config);
        configList.add(ansibleConfig);
    }

    /**
     * 按持续集成分组名称查询主机组
     *
     * @param serverGroupDO
     * @param ciGroupName
     * @return
     */
    public List<ServerDO> queryServerGroupByCiGroupName(ServerGroupDO serverGroupDO, String ciGroupName) {
        Map<String, List<ServerDO>> serverMap = grouping(serverGroupDO, true);
        List<ServerDO> servers = serverMap.get(ciGroupName);
        return servers;
    }

    /**
     * 取服务器分组map (可单独对外提供分组配置)
     *
     * @param serverGroupDO
     * @return
     */
    public Map<String, List<ServerDO>> grouping(ServerGroupDO serverGroupDO, boolean isSubgroup) {
        Map<String, List<ServerDO>> serverMap = new HashMap<String, List<ServerDO>>();
        // TODO 先按环境分  trade-prod  trade-gray trade-daily
        serverMap = groupingByEnv(serverGroupDO);
        // TODO 环境组继续分组 trade-prod-1 trade-prod-2
        if (isSubgroup)
            groupingSubgroup(serverMap);
        //redisTemplate.opsForSet().add(cacheKey,serverMap);
        return serverMap;
    }

    private String format(ServerGroupDO serverGroupDO, Map<String, List<ServerDO>> serverMap, String cacheKey) {
        String content = serverGroupDO.getContent();
        if (StringUtils.isEmpty(content))
            content = serverGroupDO.getName();
        String result = "# " + content + "\n";
        for (String key : serverMap.keySet()) {
            result += "[" + key + "]\n";
            List<ServerDO> servers = serverMap.get(key);
            servers.sort(Comparator.naturalOrder());
            for (ServerDO serverDO : servers)
                result += acqHostLine(serverDO);
            result += "\n";
        }

        cacheKeyService.set(cacheKey, result, 10080);
        return result;
    }

    /**
     * 按环境分组
     *
     * @param serverGroupDO
     * @return
     */
    private Map<String, List<ServerDO>> groupingByEnv(ServerGroupDO serverGroupDO) {
        List<ServerDO> servers = serverDao.acqServersByGroupId(serverGroupDO.getId());
        Map<String, List<ServerDO>> serverMap = new HashMap<>();
        for (ServerDO serverDO : servers) {
            String groupingName = serverGroupDO.acqShortName() + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType());
            if (serverMap.containsKey(groupingName)) {
                serverMap.get(groupingName).add(serverDO);
            } else {
                List<ServerDO> serverDOList = new ArrayList<ServerDO>();
                serverDOList.add(serverDO);
                serverMap.put(groupingName, serverDOList);
            }
        }
        return serverMap;
    }

    /**
     * 分单机
     *
     * @param serverMap
     * @param prodGroupName
     */
    private void groupingHost(Map<String, List<ServerDO>> serverMap, String prodGroupName) {
        List<ServerDO> servers = serverMap.get(prodGroupName);
        for (ServerDO serverDO : servers) {
            List<ServerDO> host = new ArrayList<ServerDO>();
            host.add(serverDO);
            serverMap.put(prodGroupName + "-h" + serverDO.getSerialNumber(), host);
        }
    }

    private void groupingSubgroup(Map<String, List<ServerDO>> serverMap) {
        List<String> keys = new ArrayList<>();
        for (String key : serverMap.keySet())
            keys.add(key);
        for (String key : keys) {
            List<ServerDO> servers = serverMap.get(key);
            if (servers.size() > 1)
                groupingSubgroup(serverMap, servers, key);
        }
    }


    /**
     * 多台机器继续分组
     *
     * @param serverMap
     */
    private void groupingSubgroup(Map<String, List<ServerDO>> serverMap, List<ServerDO> servers, String groupingName) {
        List<ServerDO> newServers = new ArrayList<>();
        newServers.addAll(servers);
        // TODO 数量少于1台则不分组
        // if (newServers.size() <= 1) return;
        // TODO 服务器数量少于分组数量也只分2组
        int subgroup = configServerGroupService.queryAnsibleSubgroup(new ServerGroupDO(servers.get(0).getServerGroupId()));
        if (subgroup > newServers.size())
            subgroup = 2;
        // 每组平均服务器数量
        int subCnt = newServers.size() / subgroup;
        for (int i = 1; i <= subgroup; i++) {
            // 余数
            int remainder = newServers.size() % (subgroup - i + 1);
            List<ServerDO> subGroupServers = new ArrayList<>();
            if (remainder > 0) {
                subGroupServers = acqSubgroup(subCnt + 1, newServers);
            } else {
                subGroupServers = acqSubgroup(subCnt, newServers);
            }
            serverMap.put(groupingName + "-" + i, subGroupServers);
        }
    }


    /**
     * 取子组
     *
     * @param subCnt     数量
     * @param serverList 服务器列表
     * @return
     */
    private List<ServerDO> acqSubgroup(int subCnt, List<ServerDO> serverList) {
        serverList.sort(Comparator.naturalOrder());
        List<ServerDO> subGroup = new ArrayList<>();
        for (int i = 0; i < subCnt; i++) {
            subGroup.add(serverList.get(0));
            serverList.remove(0);
        }
        return subGroup;
    }

    /**
     * prod继续分组(单主机)
     *
     * @param serverMap
     * @param prodGroupName
     */
    private void groupingProd(Map<String, List<ServerDO>> serverMap, String prodGroupName) {
        List<ServerDO> servers = serverMap.get(prodGroupName);
        for (ServerDO serverDO : servers) {
            List<ServerDO> host = new ArrayList<ServerDO>();
            host.add(serverDO);
            serverMap.put(prodGroupName + "-h" + serverDO.getSerialNumber(), host);
        }
    }


}
