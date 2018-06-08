package com.sdg.cmdb.zabbix;


import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.domain.zabbix.*;

import java.util.List;


/**
 * Created by liangjian on 2016/12/19.
 */
public interface ZabbixService {

    /**
     * 获取API版本
     *
     * @return
     */
    String getApiVersion();


    /**
     * 刷新数据
     *
     * @return
     */
    BusinessWrapper<Boolean> refresh();

    /**
     * 添加主机监控
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> addMonitor(long serverId);

    /**
     * 删除主机监控
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> delMonitor(long serverId);


    /**
     * 修复主机监控数据
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> repair(long serverId);

    /**
     * 禁止主机监控
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> disableMonitor(long serverId);

    /**
     * 启用主机监控
     *
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> enableMonitor(long serverId);

    /**
     * 持续集成发布接口
     * 用于发布统计&发布中关闭监控
     *
     * @param ci
     * @param type
     * @param project
     * @param group
     * @param env
     * @param deployId
     * @param bambooDeployVersion
     * @param bambooBuildNumber
     * @param bambooDeployProject
     * @param bambooDeployRollback
     * @param bambooManualBuildTriggerReasonUserName
     * @param errorCode
     * @param branchName
     * @param deployType
     * @return
     */
    BusinessWrapper<Boolean> ci(String ci, int type, String project, String group, String env, Long deployId, String bambooDeployVersion,
                                int bambooBuildNumber, String bambooDeployProject, boolean bambooDeployRollback,
                                String bambooManualBuildTriggerReasonUserName, int errorCode, String branchName, int deployType);

    /**
     * 添加zabbix用户
     *
     * @param userDO
     * @return
     */
    BusinessWrapper<Boolean> userCreate(UserDO userDO);

    /**
     * 删除zabbix用户
     *
     * @param userDO
     * @return
     */
    BusinessWrapper<Boolean> userDelete(UserDO userDO);

    /**
     * 校验用户
     *
     * @return
     */
    BusinessWrapper<Boolean> userUpdate(UserDO userDO);

    /**
     * 同步用户
     *
     * @return
     */
    BusinessWrapper<Boolean> syncUser();

    /**
     * 校验用户
     *
     * @return
     */
    BusinessWrapper<Boolean> checkUser();

    /**
     * 获取用户id
     */
    int userGet(UserDO userDO);

    /**
     * 添加告警动作
     *
     * @param serverGroupDO
     * @return
     */
    int actionCreate(ServerGroupDO serverGroupDO);

    /**
     * 添加用户组（权限为只读）
     *
     * @param serverGroupDO
     * @return
     */
    int usergroupCreate(ServerGroupDO serverGroupDO);

    /**
     * 查询主机监控状态
     *
     * @param serverDO
     * @return
     */
    int hostGetStatus(ServerDO serverDO);

    JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit);

    JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill);

    /**
     * 判断主机监控是否存在
     *
     * @param serverDO
     * @return
     */
    boolean hostExists(ServerDO serverDO);

    ZabbixVersion getZabbixVersion(String zabbixServerName);

    /**
     * 判断用户是否在用户组中（服务器组和用户组会转换名称  group_test --> users_test)
     *
     * @param userDO
     * @param serverGroupDO
     * @return
     */
    int checkUserInUsergroup(UserDO userDO, ServerGroupDO serverGroupDO);


    /**
     * 查询zabbix模版详情页
     *
     * @param templateName
     * @param enabled
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ZabbixTemplateVO>> getTemplatePage(String templateName, int enabled, int page, int length);


    ZabbixHost getHost(long serverId);


    BusinessWrapper<Boolean>  saveHost(ZabbixHost host);


    BusinessWrapper<Boolean> setTemplate(long id);

    BusinessWrapper<Boolean> delTemplate(long id);

    BusinessWrapper<Boolean> rsyncTemplate();

    List<ZabbixProxy> queryProxy();



}