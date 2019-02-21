package com.sdg.cmdb.service;


import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.ZabbixHost;
import com.sdg.cmdb.domain.zabbix.ZabbixProxy;
import com.sdg.cmdb.domain.zabbix.ZabbixTemplateVO;


import java.util.List;


/**
 * Created by liangjian on 2016/12/19.
 */
public interface ZabbixService {

    /**
     * 刷新数据
     *
     * @return
     */
    BusinessWrapper<Boolean> refresh();

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
     * 韩国太阳公公官方发奋图强有区区 i 为坎坎坷坷王环恍恍惚惚己饥己溺你        *
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
     * 添加告警动作
     *
     * @param serverGroupDO
     * @return
     */
    String actionCreate(ServerGroupDO serverGroupDO);

    JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit);

    JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill);

    /**
     * 判断主机监控是否存在
     *
     * @param serverDO
     * @return
     */
    boolean hostExists(ServerDO serverDO);


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

    BusinessWrapper<Boolean> saveHost(ZabbixHost host);

    BusinessWrapper<Boolean> setTemplate(long id);

    BusinessWrapper<Boolean> delTemplate(long id);

    BusinessWrapper<Boolean> rsyncTemplate();

    List<ZabbixProxy> queryProxy();


}