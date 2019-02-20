package com.sdg.cmdb.service;

import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.ZabbixHost;
import com.sdg.cmdb.domain.zabbix.ZabbixTemplateVO;
import com.sdg.cmdb.domain.zabbix.ZabbixVersion;
import com.sdg.cmdb.domain.zabbix.response.*;

import java.util.List;

public interface ZabbixServerService {


    TableVO<List<ZabbixTemplateVO>> getTemplatePage(String templateName, int enabled, int page, int length);

    /**
     * 查询主机详情
     *
     * @param serverDO
     * @return
     */
    ZabbixResponseHost getHost(ServerDO serverDO);

    /**
     * 更新主机的模版
     * @param serverDO
     */
    void updateHostTemplates(ServerDO serverDO);

    ZabbixResponseHost getHost(long serverId);

    void updateHostMacros(ServerDO serverDO);

    ZabbixHost getZabbixHost(long serverId);

    /**
     * 查询主机模版列表
     *
     * @param
     * @return
     */

    List<ZabbixResponseTemplate> getHostTemplates(ServerDO serverDO);

    ZabbixResponseTemplate getTemplate(String name);

    List<ZabbixResponseTemplate> queryTemplates();

    ZabbixResponseUsergroup getUsergroup(String usergroup);

    String createUsergroup(ServerGroupDO serverGroupDO);

    String createAction(ServerGroupDO serverGroupDO);

    ZabbixResponseAction getAction(ServerGroupDO serverGroupDO);

    String createHostgroup(ServerDO serverDO);

    String createHostgroup(String hostgroupName);

    boolean createHost(ServerDO serverDO, ZabbixHost host);

    boolean deleteHost(ServerDO serverDO);

    ZabbixResponseHostgroup getHostgroup(String name);

    boolean updateHostStatus(ServerDO serverDO, int status);

    ZabbixResponseUser getUser(UserDO userDO);

    List<ZabbixResponseUser> queryUser();

    BusinessWrapper<Boolean> deleteUser(UserDO userDO);

    BusinessWrapper<Boolean> createUser(UserDO userDO);

    BusinessWrapper<Boolean> updateUser(UserDO userDO);

    ZabbixResponseItem getItem(ServerDO serverDO, String itemName, String itemKey);

    List<ZabbixResponseProxy> queryProxys();

    ZabbixResponseProxy getProxy(String hostname);

    ZabbixVersion getZabbixVersion(String zabbixServerName);

    JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit);

    JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill);

    int checkUserInUsergroup(UserDO userDO, ServerGroupDO serverGroupDO);

}
