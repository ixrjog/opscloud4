package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.ansibleTask.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.ConfigFileCopyDO;
import com.sdg.cmdb.domain.config.ConfigFileCopyDoScriptDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AnsibleItemEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.GetwayItemEnum;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.domain.task.CmdVO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.control.configurationfile.ConfigurationFileControlService;
import com.sdg.cmdb.util.CmdUtils;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/6/2.
 */
@Service
public class AnsibleTaskServiceImpl implements AnsibleTaskService, InitializingBean {

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private static final Logger logger = LoggerFactory.getLogger(AnsibleTaskServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private AnsibleTaskDao ansibleTaskDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ConfigDao configDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigService configService;

    @Resource
    private ServerService serverService;

    @Resource
    private AuthService authService;


    @Resource
    private SchedulerManager schedulerManager;

    public static final String GETWAY_ACCOUNT = "getway_account";

    public static final String GROUP_GETWAY = "group_getway";

    public static final String GETWAY = "getway";

    public static final String GETWAY_SET_LOGIN = "getway_set_login";


    private HashMap<String, String> ansibleConfigMap;

    private HashMap<String, String> getwayConfigMap;

//    private HashMap<String, String> acqAnsibleConfigMap() {
//        if (ansibleConfigMap != null) return ansibleConfigMap;
//        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ANSIBLE.getItemKey());
//    }
//
//    private HashMap<String, String> acqGetwayConfigMap() {
//        if (getwayConfigMap != null) return getwayConfigMap;
//        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.GETWAY.getItemKey());
//    }

    public String task(boolean isSudo, ServerGroupDO serverGroupDO, int envType, String cmd) {
        String groupName = serverGroupDO.getName().replace("group_", "");
        String hostgroupName = groupName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType);
        return task(isSudo, hostgroupName, cmd);
    }

    @Override
    public String task(boolean isSudo, String hostgroupName, String cmd) {
        String ansible_bin = ansibleConfigMap.get(AnsibleItemEnum.ANSIBLE_BIN.getItemKey());
        //String ansible_hosts_path = configMap.get(AnsibleItemEnum.ANSIBLE_ALL_HOSTS_PATH.getItemKey());
        String ansible_hosts_path = configService.getAnsibleHostsAllPath();
        CommandLine c = new CommandLine(ansible_bin);
        c.addArgument(hostgroupName);
        // 调试日志
        //c.addArgument("-vvv -i " +ansible_hosts_path + " -sudo -a '" +cmd + "'",false);
        c.addArgument("-i");
        c.addArgument(ansible_hosts_path);
        if (isSudo)
            c.addArgument("-sudo");
        c.addArgument("-m");
        c.addArgument("shell");
        c.addArgument("-a");
        c.addArgument(cmd, false);
        // 空格添加引号
        //c.addArgument(cmd, true);
        logger.info("ansible task :" + c.toString());

        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public String taskCopy(boolean isSudo, String hostgroupName, ConfigFileCopyDO configFileCopyDO) {

        String ansible_bin = ansibleConfigMap.get(AnsibleItemEnum.ANSIBLE_BIN.getItemKey());
        String ansible_hosts_path = configService.getAnsibleHostsAllPath();
        //String ansible_bin = "/usr/local/Cellar/ansible/2.4.3.0/libexec/bin/ansible";
        //String ansible_hosts_path = configMap.get(AnsibleItemEnum.ANSIBLE_HOSTS_PATH.getItemKey());
        //String ansible_hosts_path = configMap.get(AnsibleItemEnum.ANSIBLE_ALL_HOSTS_PATH.getItemKey());
        CommandLine c = new CommandLine(ansible_bin);
        c.addArgument(hostgroupName);
        c.addArgument("-i");
        c.addArgument(ansible_hosts_path);
        if (isSudo)
            c.addArgument("-sudo");
        c.addArgument("-m");
        c.addArgument("copy");
        c.addArgument("-a");

        String cmdLine = "src=" + configFileCopyDO.getSrc() + " dest=" + configFileCopyDO.getDest() + " owner=" + configFileCopyDO.getUsername() + " group=" + configFileCopyDO.getUsergroup();
        c.addArgument(cmdLine, false);
        // 空格添加引号
        //c.addArgument(cmd, true);
        logger.info("ansible copy task :" + c.toString());

        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public String taskScript(boolean isSudo, String hostgroupName, String cmd) {
        String ansible_bin = ansibleConfigMap.get(AnsibleItemEnum.ANSIBLE_BIN.getItemKey());
        String ansible_hosts_path = configService.getAnsibleHostsAllPath();

        CommandLine c = new CommandLine(ansible_bin);
        c.addArgument(hostgroupName);
        // 调试日志
        //c.addArgument("-vvv -i " +ansible_hosts_path + " -sudo -a '" +cmd + "'",false);
        c.addArgument("-i");
        c.addArgument(ansible_hosts_path);
        if (isSudo)
            c.addArgument("-sudo");
        c.addArgument("-m");
        c.addArgument("script");
        c.addArgument("-a");
        c.addArgument(cmd, false);
        // 空格添加引号
        //c.addArgument(cmd, true);
        logger.info("ansible script task :" + c.toString());

        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public BusinessWrapper<Boolean> cmdTask(CmdVO cmdVO) {
        try {
            List<ServerVO> serverList = cmdVO.getServerList();

            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            AnsibleTaskDO ansibleTaskDO = new AnsibleTaskDO(userDO, serverList.size(), cmdVO.getCmd());
            ansibleTaskDao.addAnsibleTask(ansibleTaskDO);

            for (ServerVO serverVO : serverList) {
                // 异步处理任务
                schedulerManager.registerJob(() -> {
                    AnsibleTaskServerDO taskServerDO = new AnsibleTaskServerDO(serverVO, ansibleTaskDO.getId());
                    try {
                        String invokeStr = task(true, serverVO.getInsideIP().getIp(), cmdVO.getCmd());
                        //System.err.println(invokeStr);
                        CmdUtils.invokeAnsibleTaskServer(taskServerDO, invokeStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ansibleTaskDao.addAnsibleTaskServer(taskServerDO);
                });
            }
            BusinessWrapper wrapper = new BusinessWrapper<>(true);
            wrapper.setBody(ansibleTaskDO);

            return wrapper;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> doFileCopy(long id) {

        AnsibleTaskDO ansibleTaskDO = new AnsibleTaskDO();
        ansibleTaskDO.setCmd("copy");
        ansibleTaskDO.setUserId(0);
        ansibleTaskDO.setUserName("SysTask");
        ansibleTaskDO.setServerCnt(1);
        ansibleTaskDO.setTaskType(AnsibleTaskDO.TASK_TYPE_COPY);

        ConfigFileCopyDO configFileCopyDO = configDao.getConfigFileCopy(id);

        ServerDO serverDO = serverDao.getServerInfoById(configFileCopyDO.getServerId());

        try {
            ansibleTaskDao.addAnsibleTask(ansibleTaskDO);
            // 异步处理任务
            schedulerManager.registerJob(() -> {
                AnsibleTaskServerDO taskServerDO = new AnsibleTaskServerDO(serverDO, ansibleTaskDO.getId());
                try {
                    String invokeStr = taskCopy(true, serverDO.getInsideIp(), configFileCopyDO);
                    CmdUtils.invokeAnsibleScriptTaskServer(taskServerDO, invokeStr);
                    doCopyScript(configFileCopyDO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ansibleTaskDao.addAnsibleTaskServer(taskServerDO);
            });

            BusinessWrapper wrapper = new BusinessWrapper<>(true);
            wrapper.setBody(ansibleTaskDO);
            return wrapper;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(false);
        }
    }

    // 复制文件完成后执行Script
    private void doCopyScript(ConfigFileCopyDO configFileCopyDO){
        ConfigFileCopyDoScriptDO configFileCopyDoScriptDO = configDao.getConfigFileCopyDoScriptByCopyId(configFileCopyDO.getId());
        if(configFileCopyDoScriptDO != null){
            logger.info("ansible script task :" + configFileCopyDoScriptDO);
            doScriptByCopyServer(configFileCopyDoScriptDO.getId());
        }
    }

    @Override
    public BusinessWrapper<Boolean> doFileCopyByFileGroupName(String groupName) {

        List<ConfigFileCopyDO> list = configDao.queryConfigFileCopyByGroupName(groupName);
        if (list == null || list.size() == 0)
            return new BusinessWrapper<Boolean>(true);

        AnsibleTaskDO ansibleTaskDO = new AnsibleTaskDO();
        ansibleTaskDO.setCmd("copy");
        ansibleTaskDO.setUserId(0);
        ansibleTaskDO.setUserName("SysTask");
        ansibleTaskDO.setServerCnt(list.size());
        ansibleTaskDO.setTaskType(AnsibleTaskDO.TASK_TYPE_COPY);

        try {
            for (ConfigFileCopyDO configFileCopyDO : list) {
                logger.info("ansible copy task :" + configFileCopyDO);
                ansibleTaskDao.addAnsibleTask(ansibleTaskDO);
                // 异步处理任务
                schedulerManager.registerJob(() -> {
                    ServerDO serverDO = serverDao.getServerInfoById(configFileCopyDO.getServerId());
                    AnsibleTaskServerDO taskServerDO = new AnsibleTaskServerDO(serverDO, ansibleTaskDO.getId());
                    try {
                        String invokeStr = taskCopy(true, serverDO.getInsideIp(), configFileCopyDO);
                        //System.err.println(invokeStr);
                        CmdUtils.invokeAnsibleScriptTaskServer(taskServerDO, invokeStr);
                        doCopyScript(configFileCopyDO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ansibleTaskDao.addAnsibleTaskServer(taskServerDO);
                });
            }
            BusinessWrapper wrapper = new BusinessWrapper<>(true);
            wrapper.setBody(ansibleTaskDO);
            return wrapper;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> scriptTask(CmdVO cmdVO) {
        try {
            List<ServerVO> serverList = cmdVO.getServerList();

            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            if(userDO ==null){
                userDO = new UserDO();
                userDO.setId(0);
                userDO.setUsername("SystemTask");

            }


            TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScript(cmdVO.getTaskScriptId());

            String scriptFile = acqScriptPath(taskScriptDO);

            String cmd;
            if (StringUtils.isEmpty(cmdVO.getParams())) {
                cmd = scriptFile;
            } else {
                cmd = scriptFile + " " + cmdVO.getParams();
            }

            AnsibleTaskDO ansibleTaskDO = new AnsibleTaskDO(userDO, serverList.size(), cmd);

            ansibleTaskDO.setTaskType(AnsibleTaskDO.TASK_TYPE_SCRIPT);
            ansibleTaskDO.setTaskScriptId(taskScriptDO.getId());
            ansibleTaskDao.addAnsibleTask(ansibleTaskDO);

            // 检查script是否存在
            if (!existScript(taskScriptDO)) {
                if (!writeScript(taskScriptDO))
                    return new BusinessWrapper<>(false);
            }

            for (ServerVO serverVO : serverList) {
                // 异步处理任务
                schedulerManager.registerJob(() -> {

                    String ip = "";
                    if (serverVO.getInsideIP() != null) {
                        ip = serverVO.getInsideIP().getIp();
                    } else {
                        ip = serverDao.getServerInfoById(serverVO.getId()).getInsideIp();
                    }
                    ServerDO serverDO = new ServerDO(serverVO);
                    serverDO.setInsideIp(ip);

                    AnsibleTaskServerDO taskServerDO = new AnsibleTaskServerDO(serverDO, ansibleTaskDO.getId());
                    try {


                        String invokeStr = taskScript(true, ip, cmd);
                        //System.err.println("cmd:" + cmd);
                        //System.err.println(invokeStr);
                        CmdUtils.invokeAnsibleScriptTaskServer(taskServerDO, invokeStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.err.println(taskServerDO);
                    ansibleTaskDao.addAnsibleTaskServer(taskServerDO);
                });
            }
            BusinessWrapper wrapper = new BusinessWrapper<>(true);
            wrapper.setBody(ansibleTaskDO);

            return wrapper;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(false);
        }
    }


    @Override
    public BusinessWrapper<Boolean> doScriptByCopyServer(long id) {
        ConfigFileCopyDoScriptDO doScriptDO = configDao.getConfigFileCopyDoScript(id);
        ConfigFileCopyDO configFileCopyDO = configDao.getConfigFileCopy(doScriptDO.getCopyId());

        CmdVO cmdVO = new CmdVO();
        cmdVO.setTaskScriptId(doScriptDO.getTaskScriptId());
        cmdVO.setParams(doScriptDO.getParams());
        ServerVO serverVO = new ServerVO(serverDao.getServerInfoById(configFileCopyDO.getServerId()));

        List<ServerVO> servers = new ArrayList<>();
        servers.add(serverVO);
        cmdVO.setServerList(servers);
        return scriptTask(cmdVO);
    }

    @Override
    public BusinessWrapper<Boolean> doScriptByCopyByGroup(String groupName) {
        List<ConfigFileCopyDoScriptDO> list = configDao.getConfigFileCopyDoScriptPage(groupName, 0, 100);
        for (ConfigFileCopyDoScriptDO configFileCopyDoScriptDO : list) {
            doScriptByCopyServer(configFileCopyDoScriptDO.getId());
        }
        return new BusinessWrapper<>(true);
    }


    @Override
    public BusinessWrapper<Boolean> scriptTaskUpdateGetway() {
        TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScriptByScriptName(GETWAY);
        CmdVO cmdVO = new CmdVO();
        cmdVO.setTaskScriptId(taskScriptDO.getId());
        List<ServerVO> servers = acqGetwayServers();
        if (servers.size() == 0)
            return new BusinessWrapper<>(false);
        cmdVO.setServerList(servers);
        String params = "-i";
        cmdVO.setParams(params);
        return this.scriptTask(cmdVO);
    }

    @Override
    public BusinessWrapper<Boolean> scriptTaskGetwaySetLogin(){
        TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScriptByScriptName(GETWAY_SET_LOGIN);
        CmdVO cmdVO = new CmdVO();
        cmdVO.setTaskScriptId(taskScriptDO.getId());
        List<ServerVO> servers = acqGetwayServers();
        if (servers.size() == 0)
            return new BusinessWrapper<>(false);
        cmdVO.setServerList(servers);
        return this.scriptTask(cmdVO);
    }

    @Override
    public BusinessWrapper<Boolean> taskQuery(long taskId) {
        AnsibleTaskDO ansibleTaskDO = ansibleTaskDao.getAnsibleTask(taskId);
        if (ansibleTaskDO == null) return new BusinessWrapper<Boolean>(false);
        int serverCnt = ansibleTaskDao.cntAnsibleTaskServerByTaskId(taskId);
        BusinessWrapper wrapper = new BusinessWrapper<>(true);
        // 服务器任务全部完成
        if (serverCnt == ansibleTaskDO.getServerCnt()) {
            ansibleTaskDO.setFinalized(true);
            ansibleTaskDao.updateAnsibleTask(ansibleTaskDO);
            List<AnsibleTaskServerDO> taskList = ansibleTaskDao.queryAnsibleTaskServerByTaskId(taskId);
            List<AnsibleTaskServerVO> taskServerList = new ArrayList();
            for (AnsibleTaskServerDO ansibleTaskServerDO : taskList) {
                ServerDO serverDO = serverDao.getServerInfoById(ansibleTaskServerDO.getServerId());
                AnsibleTaskServerVO ansibleTaskServerVO = new AnsibleTaskServerVO(ansibleTaskServerDO, serverDO);
                taskServerList.add(ansibleTaskServerVO);
            }
            AnsibleTaskVO taskVO = new AnsibleTaskVO(ansibleTaskDO, taskServerList);
            wrapper.setBody(taskVO);
        } else {
            wrapper.setBody(new AnsibleTaskVO(ansibleTaskDO));
        }
        return wrapper;
    }

    @Override
    public String taskLogCleanup(ServerDO serverDO, int history) {
        String ansible_logcleanup_invoke = ansibleConfigMap.get(AnsibleItemEnum.ANSIBLE_LOGCLEANUP_INVOKE.getItemKey());

        CommandLine c = new CommandLine(ansible_logcleanup_invoke);
        c.addArgument("-servername=" + serverDO.acqServerName());
        c.addArgument("-ip=" + serverDO.getInsideIp());
        c.addArgument("-history=" + history);
        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public BusinessWrapper<Boolean> taskGetwayAddAccount(String username, String pwd) {
        TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScriptByScriptName(GETWAY_ACCOUNT);
        CmdVO cmdVO = new CmdVO();
        cmdVO.setTaskScriptId(taskScriptDO.getId());

        List<ServerVO> servers = acqGetwayServers();
        if (servers.size() == 0)
            return new BusinessWrapper<>(false);

        cmdVO.setServerList(servers);

        String getway_key_path = getwayConfigMap.get(GetwayItemEnum.GETWAY_KEY_PATH.getItemKey());
        String getway_user_conf_path = getwayConfigMap.get(GetwayItemEnum.GETWAY_USER_CONF_PATH.getItemKey());

        String params = "-u=" + username + " -p=" + pwd + " -kp=" + getway_key_path + " -up=" + getway_user_conf_path;

        cmdVO.setParams(params);

        return this.scriptTask(cmdVO);
    }

    private List<ServerVO> acqGetwayServers() {
        // 查询getway服务器
        // 先查询copy服务器配置
        List<ConfigFileCopyDO> copys = configDao.queryConfigFileCopyByGroupName(ConfigurationFileControlService.GROUP_GETWAY);
        List<ServerVO> servers = new ArrayList<>();
        if (copys.size() != 0) {
            for (ConfigFileCopyDO configFileCopyDO : copys) {
                ServerDO serverDO = serverDao.getServerInfoById(configFileCopyDO.getServerId());
                servers.add(serverService.acqServerVO(serverDO));
            }
        } else {
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName(GROUP_GETWAY);
            if (serverGroupDO != null) {
                List<ServerDO> serverList = serverDao.acqServersByGroupId(serverGroupDO.getId());
                if (serverList.size() != 0) {
                    for (ServerDO serverDO : serverList)
                        servers.add(serverService.acqServerVO(serverDO));
                }
            }
        }
        return servers;
    }

    @Override
    public BusinessWrapper<Boolean> taskGetwayDelAccount(String username) {
        List<ServerVO> servers = acqGetwayServers();
        if (servers.size() == 0)
            return new BusinessWrapper<>(false);
        TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScriptByScriptName(GETWAY_ACCOUNT);
        CmdVO cmdVO = new CmdVO();
        cmdVO.setTaskScriptId(taskScriptDO.getId());
        cmdVO.setServerList(servers);
        String params = "-d=" + username;
        cmdVO.setParams(params);
        return this.scriptTask(cmdVO);
    }


    @Override
    public TableVO<List<TaskScriptVO>> getTaskScriptPage(String scriptName, int sysScript, int page, int length) {
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        long size = 0;
        List<TaskScriptDO> list;
        List<TaskScriptVO> listVO = new ArrayList<TaskScriptVO>();
        if (authService.isRole(userDO.getUsername(), RoleDO.roleAdmin)) {
            size = ansibleTaskDao.getTaskScriptSizeByAdmin(scriptName, sysScript);
            list = ansibleTaskDao.queryTaskScriptPageByAdmin(scriptName, sysScript, page * length, length);
        } else {
            size = ansibleTaskDao.getTaskScriptSize(scriptName, userDO.getId(), sysScript);
            list = ansibleTaskDao.queryTaskScriptPage(scriptName, userDO.getId(), sysScript, page * length, length);
        }
        for (TaskScriptDO taskScriptDO : list) {
            TaskScriptVO taskScriptVO;
            if (taskScriptDO.getUserId() == userDO.getId()) {
                taskScriptVO = new TaskScriptVO(taskScriptDO, userDO);
            } else {
                UserDO user = userDao.getUserById(taskScriptDO.getUserId());
                taskScriptVO = new TaskScriptVO(taskScriptDO, user);
            }
            listVO.add(taskScriptVO);
        }
        return new TableVO<>(size, listVO);
    }


    @Override
    public TaskScriptDO saveTaskScript(TaskScriptDO taskScriptDO) {
        if (taskScriptDO.getId() == 0) {
            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            taskScriptDO.setUserId(userDO.getId());
            taskScriptDO.setUsername(userDO.getUsername());
            try {
                ansibleTaskDao.addTaskScript(taskScriptDO);
                writeScript(taskScriptDO);
                return taskScriptDO;
            } catch (Exception e) {
                e.printStackTrace();
                return new TaskScriptDO();
            }
        } else {
            try {
                ansibleTaskDao.updateTaskScript(taskScriptDO);
                writeScript(taskScriptDO);
                return taskScriptDO;
            } catch (Exception e) {
                e.printStackTrace();
                return new TaskScriptDO();
            }
        }
    }


    public boolean existScript(TaskScriptDO taskScriptDO) {
        String scriptFile = acqScriptPath(taskScriptDO);
        try {
            String file = IOUtils.readFile(scriptFile);
            if (file == null) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean writeScript(TaskScriptDO taskScriptDO) {
        String scriptFile = acqScriptPath(taskScriptDO);
        try {
            IOUtils.writeFile(taskScriptDO.getScript(), scriptFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private String acqScriptPath(TaskScriptDO taskScriptDO) {
        String scriptsPath = ansibleConfigMap.get(AnsibleItemEnum.ANSIBLE_TASK_SCRIPTS_PATH.getItemKey());
        String scriptFile = scriptsPath + "/" + taskScriptDO.getUsername() + "/id_" + taskScriptDO.getId();
        return scriptFile;
    }

    @Override
    public AnsibleVersionInfo acqAnsibleVersion() {

        try {

            String ansible_bin = ansibleConfigMap.get(AnsibleItemEnum.ANSIBLE_BIN.getItemKey());

            String ansible_task_script_path = ansibleConfigMap.get(AnsibleItemEnum.ANSIBLE_TASK_SCRIPTS_PATH.getItemKey());
            CommandLine c = new CommandLine(ansible_bin);
            c.addArgument("--version");
            // 空格添加引号
            //c.addArgument(cmd, true);
            logger.info("ansible task :" + c.toString());

            String versionInfo = CmdUtils.run(c);

            AnsibleVersionInfo info = new AnsibleVersionInfo();
            info.setVersionInfo(versionInfo);
            String[] infoLine = versionInfo.split("\n");

            String v = infoLine[0];
            String version = v.replace("ansible ", "");
            info.setVersion(version);
            info.setAnsibleBinPath(ansible_bin);
            info.setAnsibleTaskScriptPath(ansible_task_script_path);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return new AnsibleVersionInfo();
        }
    }

    @Override
    public TableVO<List<AnsibleTaskVO>> getAnsibleTaskPage(String cmd, int page, int length) {
        long size = ansibleTaskDao.getAnsibleTaskSize(cmd);
        List<AnsibleTaskDO> list = ansibleTaskDao.queryAnsibleTaskPage(cmd, page * length, length);

        List<AnsibleTaskVO> voList = new ArrayList<>();

        for (AnsibleTaskDO ansibleTaskDO : list) {
            AnsibleTaskVO ansibleTaskVO = new AnsibleTaskVO(ansibleTaskDO);

            if (ansibleTaskDO.getTaskType() == AnsibleTaskDO.TASK_TYPE_SCRIPT) {
                if (ansibleTaskDO.getTaskScriptId() != 0) {
                    TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScript(ansibleTaskDO.getTaskScriptId());
                    ansibleTaskVO.setTaskScriptDO(taskScriptDO);
                }
            }
            voList.add(ansibleTaskVO);
        }
        return new TableVO<>(size, voList);
    }

    /**
     * 初始化
     *
     * @return
     */
    private void init() {
        ansibleConfigMap = configCenterService.queryItemGroup(ConfigCenterItemGroupEnum.ANSIBLE.getItemKey(), "dev");
        getwayConfigMap = configCenterService.queryItemGroup(ConfigCenterItemGroupEnum.GETWAY.getItemKey(), ConfigCenterServiceImpl.DEFAULT_ENV);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

}
