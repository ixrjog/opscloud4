package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.util.SortList;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by liangjian on 2016/12/14.
 */
@Service
public class AnsibleService extends ConfigurationFileControlAbs {

    private ThreadLocal<List<ServerGroupDO>> tlServerGroups = new ThreadLocal<List<ServerGroupDO>>();

    private ThreadLocal<Map<String, List<ServerDO>>> tlServers = new ThreadLocal<Map<String, List<ServerDO>>>();

    private ThreadLocal<String> tlResult = new ThreadLocal<String>();

    private ThreadLocal<Integer> tlType = new ThreadLocal<Integer>();

    public static final String check_tomcat_project_name = "TOMCAT_APP_NAME_OPT";

    public static final String getway_host_ssh_public_ip = "GETWAY_HOST_SSH_PUBLIC_IP";



    /**
     * 部分服务器需要公网ip访问
     *
     * @return
     */
    private void invokeIp(ServerDO serverDO) {
        ConfigPropertyDO confPropertyDo = configDao.getConfigPropertyByName(getway_host_ssh_public_ip);
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerPropertyData(serverDO.getId(), confPropertyDo.getId());
        if (serverGroupPropertiesDO != null && serverGroupPropertiesDO.getPropertyValue().equalsIgnoreCase("true")) {
            serverDO.setInsideIp(serverDO.getPublicIp());
        }
    }


    /**
     * 检查是否为持续集成服务器组
     *
     * @param serverGroupDO
     * @return
     */
    public boolean check(ServerGroupDO serverGroupDO) {
        if (serverGroupDO == null) return false;
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName(check_tomcat_project_name);
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerGroupPropertyData(serverGroupDO.getId(), confPropertyDO.getId());
        if (serverGroupPropertiesDO != null && !serverGroupPropertiesDO.getPropertyValue().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 初始化
     */
    public void init() {
        List<ServerGroupDO> serverGroups = serverGroupDao.queryServerGroup();
        if (serverGroups == null || serverGroups.get(0) == null) return;
        Map<String, List<ServerDO>> servers = new HashMap<>();
        for (ServerGroupDO serverGroup : serverGroups) {
            List<ServerDO> hosts = serverDao.acqServersByGroupId(serverGroup.getId());
            if (hosts.isEmpty()) {
                continue;
            }
            servers.put(serverGroup.getName(), hosts);
        }
        addServerGroup(serverGroups);
        addServers(servers);
    }

    public void addServerGroup(List<ServerGroupDO> serverGroups) {
        if (serverGroups == null) return;
        tlServerGroups.set(serverGroups);
    }

    public void addServers(Map<String, List<ServerDO>> servers) {
        if (servers == null) return;
        tlServers.set(servers);
    }

    /**
     * @param type 分组类型（只针对production）
     *             0 只分2组
     *             1 按每组2台分组
     *             2 生成全局服务器列表
     */

    public void build(int type) {
        init();
        tlType.set(type);
        String headInfo = getHeadInfo();
        tlResult.set("");
        if (tlServerGroups.get() != null && tlServers.get() != null) {
            buildHosts();
        }
    }


    public void build() {
        build(0);
    }

    private void addValue(String value) {
        tlResult.set(tlResult.get() + value);
    }

    private void buildHosts() {
        for (ServerGroupDO group : tlServerGroups.get()) {
            if (tlType.get() != 2 && !check(group)) continue;
            String content = group.getContent();
            if (content == null || content.isEmpty()) {
                content = group.getName();
            }
            addValue("# " + content + "\n");
            buildHosts(group);
            addValue("\n");
        }
    }

    private void buildHosts(ServerGroupDO group) {
        List<ServerDO> servers = tlServers.get().get(group.getName());
        if (servers == null) return;
        Map<String, List<ServerDO>> map = convert(servers);
        buildHosts(map, "default", group);
        buildHosts(map, "test", group);
        buildHosts(map, "dev", group);
        buildHosts(map, "daily", group);
        buildHosts(map, "gray", group);
        buildHosts(map, "back", group);
        buildHosts(map, "production", group);
        //prod分组（基于算法）
        buildHostsProd(map, group);
    }

    /**
     * 创建各个环境的主机文件
     * [trade-production]
     * 10.17.1.5 ansible_ssh_user=manage
     * 10.17.1.6 ansible_ssh_user=manage
     * [trade-gray]
     * 10.17.1.7 ansible_ssh_user=manage
     * ...
     *
     * @param map
     * @param envName
     * @param group
     */
    private void buildHosts(Map<String, List<ServerDO>> map, String envName, ServerGroupDO group) {
        List<ServerDO> servers = map.get(envName);
        if (servers == null || servers.size() == 0) return;
        String groupName = group.getName().replace("group_", "");
        addValue("[" + groupName + "-" + envName + "]\n");
        for (ServerDO server : servers) {
            if (server == null) return;
            invokeIp(server);
            addValue(server.getInsideIp() + " ansible_ssh_user=" + server.getLoginUser() + " # " + server.getSerialNumber() + "\n");
        }
        addValue("\n");
    }

    /**
     * prod分组
     *
     * @param map
     * @param group
     */
    private void buildHostsProd(Map<String, List<ServerDO>> map, ServerGroupDO group) {
        List<ServerDO> servers = map.get("production");
        if (servers == null) return;
        for (ServerDO server : servers) {
            if (server.getSerialNumber() == null || server.getSerialNumber().isEmpty()) return;
        }
        SortList<ServerDO> sortList = new SortList<ServerDO>();
        sortList.Sort(servers, "getSerialNumber", null);

        switch (tlType.get()) {
            case 0:
                buildHostsProdType0(servers, group);
                break;
            case 1:
                buildHostsProdType1(servers, group);
                break;
        }
        buildHostsProd(servers, group);
    }

    private void buildHostsProd(List<ServerDO> servers, ServerGroupDO group) {
        String groupName = group.getName().replace("group_", "");
        for (ServerDO server : servers) {
            invokeIp(server);
            addValue("[" + groupName + "-production-h" + server.getSerialNumber() + "]\n");
            addValue(server.getInsideIp() + " ansible_ssh_user=" + server.getLoginUser() + " # " + server.getSerialNumber() + "\n");
        }
    }

    /**
     * 只分成2组
     *
     * @param servers
     * @param group
     */
    private void buildHostsProdType0(List<ServerDO> servers, ServerGroupDO group) {
        int groupCnt = servers.size() / 2;
        if (servers.size() <= 1) return;
        String groupName = group.getName().replace("group_", "");
        String production1 = "[" + groupName + "-production-1]\n";
        String production2 = "[" + groupName + "-production-2]\n";
        for (ServerDO server : servers) {
            invokeIp(server);
            if (server.getSerialNumber() == null || server.getSerialNumber().isEmpty()) continue;
            if (Integer.valueOf(server.getSerialNumber()) <= groupCnt) {
                production1 += server.getInsideIp() + " ansible_ssh_user=" + server.getLoginUser() + " # " + server.getSerialNumber() + "\n";
            } else {
                production2 += server.getInsideIp() + " ansible_ssh_user=" + server.getLoginUser() + " # " + server.getSerialNumber() + "\n";
            }
        }
        addValue(production1 + "\n");
        addValue(production2 + "\n");
    }


    /**
     * 每组2台
     *
     * @param servers
     * @param group
     */
    private void buildHostsProdType1(List<ServerDO> servers, ServerGroupDO group) {
        if (servers.size() <= 1) return;
        int groupCnt = servers.size() / 2;
        String groupName = group.getName().replace("group_", "");
        /** for (ServerDO server : servers) {
         if (server.getSerialNumber() == null || server.getSerialNumber().isEmpty()) return;
         }
         SortList<ServerDO> sortList = new SortList<ServerDO>();
         sortList.Sort(servers, "getSerialNumber", null);
         **/
        for (ServerDO server : servers) invokeIp(server);

        for (int i = 0; i < groupCnt; i++) {
            addValue("[" + groupName + "-production-" + (i + 1) + "]\n");
            if ((i * 2) > servers.size()) continue;
            addValue(servers.get(i * 2).getInsideIp() + " ansible_ssh_user=" + servers.get(i * 2).getLoginUser() + " # " + servers.get(i * 2).getSerialNumber() + "\n");
            if ((i * 2 + 1) > servers.size()) continue;
            addValue(servers.get(i * 2 + 1).getInsideIp() + " ansible_ssh_user=" + servers.get(i * 2 + 1).getLoginUser() + " # " + servers.get(i * 2 + 1).getSerialNumber() + "\n");
        }
    }

    /**
     * 转换按环境分类
     *
     * @return
     */
    private Map<String, List<ServerDO>> convert(List<ServerDO> servers) {
        if (servers == null || servers.get(0) == null) return null;
        Map<String, List<ServerDO>> map = new HashMap<>();
        List<ServerDO> serversDefault = new ArrayList<>();
        List<ServerDO> serversTest = new ArrayList<>();
        List<ServerDO> serversDev = new ArrayList<>();
        List<ServerDO> serversDaily = new ArrayList<>();
        List<ServerDO> serversGray = new ArrayList<>();
        List<ServerDO> serversBack = new ArrayList<>();
        List<ServerDO> serversProduction = new ArrayList<>();
        for (ServerDO server : servers) {
            String envname = ServerDO.EnvTypeEnum.getEnvTypeName(server.getEnvType());
            switch (envname) {
                case "-":
                    serversDefault.add(server);
                    break;
                case "test":
                    serversTest.add(server);
                    break;
                case "dev":
                    serversDev.add(server);
                    break;
                case "daily":
                    serversDaily.add(server);
                    break;
                case "gray":
                    serversGray.add(server);
                    break;
                case "back":
                    serversBack.add(server);
                    break;
                case "production":
                    serversProduction.add(server);
                    break;
            }
        }
        map.put("default", serversDefault);
        map.put("test", serversTest);
        map.put("dev", serversDev);
        map.put("daily", serversDaily);
        map.put("gray", serversGray);
        map.put("back", serversBack);
        map.put("production", serversProduction);
        return map;
    }

    @Override
    public String toString() {
        return getHeadInfo() + tlResult.get();
    }

}
