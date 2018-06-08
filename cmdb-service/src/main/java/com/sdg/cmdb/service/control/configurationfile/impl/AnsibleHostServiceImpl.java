package com.sdg.cmdb.service.control.configurationfile.impl;

import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ConfigServerGroupService;
import com.sdg.cmdb.service.control.configurationfile.AnsibleHostService;
import com.sdg.cmdb.util.SortList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by liangjian on 2017/4/12.
 */
@Service
public class AnsibleHostServiceImpl implements AnsibleHostService {


    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigDao configDao;

    @Resource
    private ConfigServerGroupService configServerGroupService;

    /**
     * 生成 web-service 服务器列表
     */
    public void build() {
        List<ServerGroupDO> serverGroups = serverGroupDao.queryServerGroup();
        //if (serverGroups == null || serverGroups.get(0) == null) return ;
        Map<String, List<ServerDO>> servers = new HashMap<>();
        for (ServerGroupDO serverGroup : serverGroups) {
            List<ServerDO> hosts = serverDao.acqServersByGroupId(serverGroup.getId());
            if (hosts.isEmpty()) {
                continue;
            }
            servers.put(serverGroup.getName(), hosts);
        }
    }

    /**
     * 按环境分组(prod环境分多组)
     *
     * @param serverGroupDO
     * @return
     */
    private Map<String, List<ServerDO>> groupingByUseType(ServerGroupDO serverGroupDO) {
        Map<String, List<ServerDO>> serverMap = grouping(serverGroupDO);
        groupingProd(serverGroupDO, serverMap);
        groupingBack(serverGroupDO, serverMap);
        return serverMap;
    }

    /**
     * 按环境分组(prod环境不分多组)
     *
     * @param serverGroupDO
     * @return
     */
    private Map<String, List<ServerDO>> grouping(ServerGroupDO serverGroupDO) {
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
     * prod继续分组
     *
     * @param serverGroupDO
     * @param serverMap
     */
    private void groupingProd(ServerGroupDO serverGroupDO, Map<String, List<ServerDO>> serverMap) {
        String prodGroupName = serverGroupDO.getName().replace("group_", "") + "-" + ServerDO.EnvTypeEnum.prod.getDesc();
        if (!serverMap.containsKey(prodGroupName)) return;
        List<ServerDO> servers = serverMap.get(prodGroupName);
        // 数量少于1台则不分组
        if (servers.size() <= 1) return;
        List<ServerDO> prod1 = new ArrayList<ServerDO>();
        List<ServerDO> prod2 = new ArrayList<ServerDO>();
        int boundary = servers.size() + 1;
        for (ServerDO serverDO : servers) {
            if (Integer.valueOf(serverDO.getSerialNumber()) * 2 <= boundary) {
                prod1.add(serverDO);
            } else {
                prod2.add(serverDO);
            }
        }
        serverMap.put(prodGroupName + "-" + 1, prod1);
        serverMap.put(prodGroupName + "-" + 2, prod2);
        groupingProd(serverMap, prodGroupName);
    }

    /**
     * 后台分组
     *
     * @param serverGroupDO
     * @param serverMap
     */
    private void groupingBack(ServerGroupDO serverGroupDO, Map<String, List<ServerDO>> serverMap) {
        String backGroupName = serverGroupDO.getName().replace("group_", "") + "-" + ServerDO.EnvTypeEnum.back.getDesc();
        if (!serverMap.containsKey(backGroupName)) return;
        List<ServerDO> servers = serverMap.get(backGroupName);
        // 数量少于1台则不分组
        if (servers.size() <= 1) return;
        List<ServerDO> back1 = new ArrayList<ServerDO>();
        List<ServerDO> back2 = new ArrayList<ServerDO>();
        int boundary = servers.size() + 1;
        for (ServerDO serverDO : servers) {
            if (Integer.valueOf(serverDO.getSerialNumber()) * 2 <= boundary) {
                back1.add(serverDO);
            } else {
                back2.add(serverDO);
            }
        }
        serverMap.put(backGroupName + "-" + 1, back1);
        serverMap.put(backGroupName + "-" + 2, back2);
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


    @Override
    public List<ServerDO> queryServerGroupByCiGroupName(ServerGroupDO serverGroupDO, String ciGroupName) {
        Map<String, List<ServerDO>> serverMap = groupingByUseType(serverGroupDO);
        List<ServerDO> servers = new ArrayList<ServerDO>();
        if (!serverMap.containsKey(ciGroupName)) return servers;
        return serverMap.get(ciGroupName);
    }


    private String buildCfgByServerGroup(List<ServerGroupDO> serverGroups, int useType) {
        String result = getHeadInfo();
        for (ServerGroupDO serverGroup : serverGroups) {
            //if (!configServerGroupService.isTomcatServer(serverGroup)) continue;
            List<ServerDO> hosts = serverDao.acqServersByGroupId(serverGroup.getId());
            if (hosts.isEmpty())
                continue;
            if (useType == 0) {
                Map<String, List<ServerDO>> serverMap = grouping(serverGroup);
                result += format(serverGroup, serverMap);
            } else {
                Map<String, List<ServerDO>> serverMap = groupingByUseType(serverGroup);
                result += format(serverGroup, serverMap);
            }
        }
        return result;
    }


    @Override
    public String acqHostsCfgByUseType(int useType) {
        List<ServerGroupDO> serverGroups = new ArrayList<>();
        if(useType == 0){
            serverGroups =  serverGroupDao.queryServerGroup();
        }else{
            serverGroups =   serverGroupDao.queryServerGroupByUseType(useType);
        }
        return buildCfgByServerGroup(serverGroups, useType);
    }

    @Override
    public String acqHostsAllCfg() {
        List<ServerGroupDO> serverGroups = serverGroupDao.queryServerGroup();
        String result = getHeadInfo();
        for (ServerGroupDO serverGroup : serverGroups) {
            //if (!check(serverGroup)) continue;
            List<ServerDO> hosts = serverDao.acqServersByGroupId(serverGroup.getId());
            if (hosts.isEmpty())
                continue;
            Map<String, List<ServerDO>> serverMap = this.grouping(serverGroup);
            result += format(serverGroup, serverMap);
        }
        return result;
    }

    private String format(ServerGroupDO serverGroupDO, Map<String, List<ServerDO>> serverMap) {
        //SortList<ServerDO> sortList = new SortList<ServerDO>();
        //sortList.Sort(servers, "getSerialNumber", null);
        String content = serverGroupDO.getContent();
        if (StringUtils.isEmpty(content))
            content = serverGroupDO.getName();
        String result = "# " + content + "\n";
        for (String key : serverMap.keySet()) {
            result += "[" + key + "]\n";
            List<ServerDO> servers = serverMap.get(key);
            SortList<ServerDO> sortList = new SortList<ServerDO>();
            sortList.Sort(servers, "getSerialNumber", null);
            for (ServerDO serverDO : servers)
                result += acqHostLine(serverDO);
            result += "\n";
        }
        return result;
    }

    private String acqHostLine(ServerDO serverDO) {
        configServerGroupService.invokeGetwayIp(serverDO);
        return serverDO.getInsideIp() + " ansible_ssh_user=" + serverDO.getLoginUser() + " # " + serverDO.getSerialNumber() + "\n";
    }

    private String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return "# Created by cmdb on " + fastDateFormat.format(new Date()) + "\n\n";
    }


}
