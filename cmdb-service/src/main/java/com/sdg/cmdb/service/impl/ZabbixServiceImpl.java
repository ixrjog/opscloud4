package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.*;
import com.sdg.cmdb.domain.zabbix.response.*;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

import java.util.List;


/**
 * Created by liangjian on 2016/12/16.
 */
@Service
public class ZabbixServiceImpl implements ZabbixService {

    @Resource
    private ZabbixServerDao zabbixDao;

    @Resource
    private SchedulerManager schedulerManager;


    public static final String ZABBIX_SERVER_DEFAULT_NAME = "Zabbix server";

    /**
     * 不处理的账户
     */
    private String[] excludeUsers = {"Admin", "guest", "zabbix_admin", "zabbix", "baiyi"};

    private static final Logger logger = LoggerFactory.getLogger(ZabbixServiceImpl.class);

    public static final String zabbix_host_monitor_public_ip = "ZABBIX_HOST_MONITOR_PUBLIC_IP";

    public static final String zabbix_proxy_id = "ZABBIX_PROXY_ID";

    public static final String zabbix_proxy_name = "ZABBIX_PROXY_NAME";


    @Resource
    protected ServerDao serverDao;

    @Resource
    protected ConfigDao configDao;

    @Resource
    protected UserDao userDao;

    @Autowired
    protected ConfigServerGroupService configServerGroupService;

    //停用主机监控
    public static final int hostStatusDisable = 1;

    //启用主机监控
    public static final int hostStatusEnable = 0;

    //主机监控存在
    public static final int isMonitor = 1;

    //主机监控不存在
    public static final int noMonitor = 0;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ZabbixServerService zabbixServerService;

    @Resource
    private KeyboxDao keyboxDao;

    private String getServerGroupName(ServerDO serverDO) {
        if (serverDO == null) return null;
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        return serverGroupDO.getName();
    }


    /**
     * 主机是否被监控
     *
     * @return
     */
    @Override
    public boolean hostExists(ServerDO serverDO) {
        if (StringUtils.isEmpty(hostGet(serverDO)))
            return false;
        return true;
    }

    /**
     * 内网ip查询hostid
     *
     * @return
     */
    public String hostGet(ServerDO serverDO) {
        ZabbixResponseHost host = zabbixServerService.getHost(serverDO);
        if (host == null || StringUtils.isEmpty(host.getHostid()))
            return "";
        return host.getHostid();
    }


    private boolean hostUpdateStatus(ServerDO serverDO, int status) {
        return zabbixServerService.updateHostStatus(serverDO, status);
    }

    /**
     * zabbix监控主机名称
     *
     * @param serverDO
     * @return
     */
    private String acqZabbixServerName(ServerDO serverDO) {
        String name = serverDO.getServerName();
        String serialNumberName = "";
        if (serverDO.getSerialNumber() != null && !serverDO.getSerialNumber().isEmpty()) {
            serialNumberName = "-" + serverDO.getSerialNumber();
        }
        String envName = "";
        if (serverDO.getEnvType() != ServerDO.EnvTypeEnum.prod.getCode()) {
            envName = "-" + ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType());
        }
        return name + envName + serialNumberName;
    }


    private boolean hostCreate(ServerDO serverDO, ZabbixHost host) {
        return zabbixServerService.createHost(serverDO, host);
    }

    private boolean hostDelete(ServerDO serverDO) {
        return zabbixServerService.deleteHost(serverDO);
    }


    private String hostgroupGet(ServerDO serverDO) {
        return hostgroupGet(getServerGroupName(serverDO));

    }

    private String hostgroupGet(String name) {
        ZabbixResponseHostgroup hostgroup = zabbixServerService.getHostgroup(name);
        if (hostgroup == null || StringUtils.isEmpty(hostgroup.getGroupid()))
            return null;
        return hostgroup.getGroupid();

    }


    /**
     * 创建服务器组
     *
     * @param name
     * @return
     */
    public boolean hostgroupCreate(String name) {
        String groupid = zabbixServerService.createHostgroup(name);
        if (StringUtils.isEmpty(groupid))
            return false;
        return true;
    }


    public List<ZabbixTemplateDO> templateQueryAll() {
        List<ZabbixResponseTemplate> templateList = zabbixServerService.queryTemplates();
        List<ZabbixTemplateDO> list = new ArrayList<>();
        for (ZabbixResponseTemplate template : templateList)
            list.add(new ZabbixTemplateDO(template, 0));
        return list;
    }

    //     public List<ZabbixProxy> proxyQueryAll() {

    public List<ZabbixProxy> proxyQueryAll() {
        List<ZabbixResponseProxy> proxyList = zabbixServerService.queryProxys();
        List<ZabbixProxy> proxys = new ArrayList<>();
        for(ZabbixResponseProxy proxy:proxyList){
            proxys.add(new ZabbixProxy());
        }
        return proxys;
    }

    /**
     * 查询用户群组
     *
     * @param usergroup
     * @return ZabbixResponse
     */
    public String usergroupGet(String usergroup) {
        ZabbixResponseUsergroup zabbixResponseUsergroup = zabbixServerService.getUsergroup(usergroup);
        if (zabbixResponseUsergroup == null || StringUtils.isEmpty(zabbixResponseUsergroup.getUsrgrpid()))
            return null;
        return zabbixResponseUsergroup.getUsrgrpid();
    }

    /**
     * 创建用户
     *
     * @param userDO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> userCreate(UserDO userDO) {
        return zabbixServerService.createUser(userDO);
    }





    /**
     * 删除用户
     *
     * @param userDO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> userDelete(UserDO userDO) {
        return zabbixServerService.deleteUser(userDO);
    }

    /**
     * 同步zabbix用户
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> syncUser() {
        List<UserDO> listUserDO = userDao.getAllUser();
        for (UserDO userDO : listUserDO) {
            // 未授权的用户跳过
            if (userDO.getAuthed() == UserDO.AuthType.noAuth.getCode()) continue;
            zabbixServerService.getUser(userDO);
            ZabbixResponseUser user = zabbixServerService.getUser(userDO);
            if (user != null) {
                //更新zabbix授权状态
                userDO.setZabbixAuthed(UserDO.AuthType.authed.getCode());
                userDao.updateUserZabbixAuthed(userDO);
                continue;
            }
            userCreate(userDO);
            logger.info("Zabbix : add user " + userDO.getUsername());
        }
        cleanZabbixUser();
        return new BusinessWrapper<>(true);
    }

    /**
     * 清理zabbix中的账户
     */
    public void cleanZabbixUser() {
        List<ZabbixResponseUser> userList = zabbixServerService.queryUser();
        for (ZabbixResponseUser user : userList) {
            UserDO userDO = userDao.getUserByName(user.getAlias());
            if (userDO != null) continue;
            for (String userName : excludeUsers) {
                if (user.getAlias().equals(userName)) {
                    userDO = null;
                    break;
                }
            }
            if (userDO != null) {
                logger.info("Zabbix : del user " + userDO.getUsername());
                this.userDelete(userDO);
            }
        }
    }

    /**
     * 校验账户
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> checkUser() {
        List<UserDO> listUserDO = userDao.getAllUser();
        for (UserDO userDO : listUserDO) {
            // 未授权的用户跳过
            if (userDO.getZabbixAuthed() == UserDO.ZabbixAuthType.noAuth.getCode()) continue;
            userUpdate(userDO);
            logger.info("Zabbix : update user " + userDO.getUsername());
        }
        return new BusinessWrapper<>(true);
    }

    /**
     * 更新用户
     *
     * @param userDO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> userUpdate(UserDO userDO) {
        return zabbixServerService.updateUser(userDO);
    }


    /**
     * 按名称查询主机的监控项
     *
     * @param itemName
     * @param itemKey
     */
    public String itemGet(ServerDO serverDO, String itemName, String itemKey) {
        return zabbixServerService.getItem(serverDO, itemName, itemKey).getItemid();
    }

    @Override
    public String actionCreate(ServerGroupDO serverGroupDO) {
        return zabbixServerService.createAction(serverGroupDO);
    }


    /**
     * 查询主机监控项历史数据
     *
     * @param itemName
     * @param itemKey
     * @param historyType
     * @param limit
     * @return
     */
    @Override
    public JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit) {
        return zabbixServerService.getHistory(serverDO,itemName,itemKey,historyType,limit);
    }

    @Override
    public JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill) {
        return zabbixServerService.getHistory(serverDO,itemName,itemKey,historyType,timestampFrom,timestampTill);
    }

    /**
     * shu xin
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> refresh() {
        schedulerManager.registerJob(() -> {
            List<ServerDO> servers = new ArrayList<>();
            servers.addAll(serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.ecs.getCode()));
            //servers.addAll(serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.vm.getCode()));
            for (ServerDO serverDO : servers) {
                if (hostExists(serverDO)) {
                    serverDO.setZabbixMonitor(this.isMonitor);
                    serverDO.setZabbixStatus(Integer.valueOf(zabbixServerService.getHost(serverDO).getStatus()));
                    // 更新模版
                    zabbixServerService.updateHostTemplates(serverDO);
                    // 更新宏
                    zabbixServerService.updateHostMacros(serverDO);
                } else {
                    serverDO.setZabbixMonitor(this.noMonitor);
                    serverDO.setZabbixStatus(-1);
                }
                serverDao.updateServerGroupServer(serverDO);
            }
        });

        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> delMonitor(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (hostDelete(serverDO)) {
            serverDO.setZabbixMonitor(noMonitor);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostDelete.getCode(), ErrorCode.zabbixHostDelete.getMsg());
        }
    }

    @Override
    public BusinessWrapper<Boolean> repair(long serverId) {
        try {
            ServerDO serverDO = serverDao.getServerInfoById(serverId);
            // 检查服务器监控配置
            if (hostExists(serverDO)) {
                serverDO.setZabbixMonitor(1);

                serverDO.setZabbixStatus(Integer.valueOf(zabbixServerService.getHost(serverDO).getStatus()));
            } else {
                serverDO.setZabbixMonitor(0);
                // hostGetStatus(serverDO);
                serverDO.setZabbixMonitor(1);
            }
            serverDao.updateServerGroupServer(serverDO);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> disableMonitor(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (hostUpdateStatus(serverDO, hostStatusDisable)) {
            serverDO.setZabbixMonitor(hostStatusDisable);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostDisable.getCode(), ErrorCode.zabbixHostDisable.getMsg());
        }
    }

    @Override
    public BusinessWrapper<Boolean> enableMonitor(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (hostUpdateStatus(serverDO, hostStatusEnable)) {
            serverDO.setZabbixMonitor(hostStatusEnable);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostEisable.getCode(), ErrorCode.zabbixHostEisable.getMsg());
        }
    }

    /**
     * 持续集成调用接口
     *
     * @param key
     * @param type
     * @param project
     * @param group
     * @param env
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> ci(String key, int type, String project, String group, String env, Long deployId, String bambooDeployVersion,
                                       int bambooBuildNumber, String bambooDeployProject, boolean bambooDeployRollback,
                                       String bambooManualBuildTriggerReasonUserName, int errorCode, String branchName, int deployType) {

        CiDeployStatisticsDO ciDeployStatisticsDO = new CiDeployStatisticsDO(project, group, env, type, deployId, bambooDeployVersion,
                bambooBuildNumber, bambooDeployProject, bambooDeployRollback,
                bambooManualBuildTriggerReasonUserName, errorCode, branchName, deployType);
        //ciService.doCiDeployStatistics(ciDeployStatisticsDO);
        // 只有war类部署才关闭监控
        if (deployType != CiDeployStatisticsDO.DeployTypeEnum.war.getCode()) return new BusinessWrapper<>(true);
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + project);
        if (serverGroupDO == null)
            return new BusinessWrapper<>(ErrorCode.zabbixCIServerGroupNotExist);
        List<ServerDO> listServerDO = serverDao.acqServersByGroupId(serverGroupDO.getId());
        if (listServerDO == null || listServerDO.size() == 0)
            return new BusinessWrapper<>(ErrorCode.serverGroupServerNull);
        for (ServerDO serverDO : listServerDO) {
            String serverEnv = ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType());
            if (!serverEnv.equals(env)) continue;
            if (hostUpdateStatus(serverDO, type)) {
                logger.info("Zabbix : ci update " + serverDO.getServerName() + "/" + serverDO.getInsideIp() + "/" + serverEnv + " status " + type);
            } else {
                logger.error("Zabbix : ci update " + serverDO.getServerName() + "/" + serverDO.getInsideIp() + "/" + serverEnv + " status " + type);
            }
        }
        return new BusinessWrapper<>(true);
    }


    @Override
    public TableVO<List<ZabbixTemplateVO>> getTemplatePage(String templateName, int enabled, int page, int length) {
        long size = zabbixDao.getTemplateSize(templateName, enabled);
        List<ZabbixTemplateDO> list = zabbixDao.getTemplatePage(templateName, enabled, page * length, length);
        List<ZabbixTemplateVO> voList = new ArrayList<>();
        for (ZabbixTemplateDO template : list)
            voList.add(new ZabbixTemplateVO(template));
        return new TableVO<>(size, voList);
    }


    @Override
    public BusinessWrapper<Boolean> saveHost(ZabbixHost host) {
        ServerDO serverDO = serverDao.getServerInfoById(host.getServerVO().getId());
       // ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());

        zabbixServerService.createHostgroup(serverDO);
        // 添加服务器监控
        if (zabbixServerService.createHost(serverDO, host)) {
            serverDO.setZabbixMonitor(isMonitor);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostCreate.getCode(), ErrorCode.zabbixHostCreate.getMsg());
        }
    }


    @Override
    public List<ZabbixProxy> queryProxy() {
        return proxyQueryAll();
    }

    @Override
    public BusinessWrapper<Boolean> setTemplate(long id) {
        try {
            ZabbixTemplateDO zabbixTemplateDO = zabbixDao.getTemplate(id);
            if (zabbixTemplateDO.getEnabled() == 0) {
                zabbixTemplateDO.setEnabled(1);
            } else {
                zabbixTemplateDO.setEnabled(0);
            }
            zabbixDao.updateTemplate(zabbixTemplateDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delTemplate(long id) {
        try {
            zabbixDao.delTemplate(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public BusinessWrapper<Boolean> rsyncTemplate() {
        List<ZabbixTemplateDO> list = templateQueryAll();
        for (ZabbixTemplateDO zabbixTemplateDO : list) {
            ZabbixTemplateDO template = zabbixDao.getTemplateByName(zabbixTemplateDO.getTemplateName());
            if (template == null) {
                try {
                    zabbixDao.addTemplate(zabbixTemplateDO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new BusinessWrapper<Boolean>(false);
                }
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }


}
