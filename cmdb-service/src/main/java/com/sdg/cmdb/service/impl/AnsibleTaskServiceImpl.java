package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.ansibleTask.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.ConfigFileCopyDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.domain.task.CmdVO;
import com.sdg.cmdb.domain.task.DoPlaybook;
import com.sdg.cmdb.domain.task.PlaybookHostPattern;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.CmdUtils;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("#{cmdb['ansible.bin']}")
    private String ansibleBin;

    @Value("#{cmdb['ansible.playbook.bin']}")
    private String ansiblePlaybookBin;

    @Value("#{cmdb['ansible.scripts.path']}")
    private String ansibleScriptsPath;

    // ansible.logs.path=/data/www/logs
    @Value("#{cmdb['ansible.logs.path']}")
    private String ansibleLogsPath;

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

    @Autowired
    private ConfigServerGroupService configServerGroupService;

    @Resource
    private SchedulerManager schedulerManager;

    public static final String GETWAY_ACCOUNT = "getway_account";

    public static final String GROUP_GETWAY = "group_getway";

    public static final String GETWAY = "getway";

    public static final String GETWAY_SET_LOGIN = "getway_set_login";

    private HashMap<String, String> getwayConfigMap;


    public String task(boolean isSudo, ServerGroupDO serverGroupDO, int envType, String cmd) {
        String groupName = serverGroupDO.getName().replace("group_", "");
        String hostgroupName = groupName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType);
        return task(isSudo, hostgroupName, cmd);
    }

    @Override
    public String task(boolean isSudo, String hostgroupName, String cmd) {
        String ansible_hosts_path = configService.getAnsibleHostsAllPath();
        CommandLine c = new CommandLine(ansibleBin);
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


    private String taskCopy(boolean isSudo, String hostgroupName, ConfigFileCopyDO configFileCopyDO) {
        String ansible_hosts_path = configService.getAnsibleHostsAllPath();
        //String ansible_bin = "/usr/local/Cellar/ansible/2.4.3.0/libexec/bin/ansible";
        //String ansible_hosts_path = configMap.get(AnsibleItemEnum.ANSIBLE_HOSTS_PATH.getItemKey());
        //String ansible_hosts_path = configMap.get(AnsibleItemEnum.ANSIBLE_ALL_HOSTS_PATH.getItemKey());
        CommandLine c = new CommandLine(ansibleBin);
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


    private String taskScript(boolean isSudo, String hostgroupName, String cmd) {
        String ansible_hosts_path = configService.getAnsibleHostsAllPath();

        CommandLine c = new CommandLine(ansibleBin);
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
        CommandLine c = new CommandLine("");
        c.addArgument("-servername=" + serverDO.acqServerName());
        c.addArgument("-ip=" + serverDO.getInsideIp());
        c.addArgument("-history=" + history);
        String rt = CmdUtils.run(c);
        return rt;
    }


    @Override
    public TableVO<List<TaskScriptVO>> getTaskScriptPage(String scriptName, int sysScript, int page, int length) {
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        if (userDO == null) return new TableVO<>(0, null);
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
    public List<TaskScriptDO> getTaskScriptPlaybook() {
        return ansibleTaskDao.getTaskScriptByPlaybook();
    }

    @Override
    public List<TaskScriptDO> queryPlaybook(String playbookName) {
        return ansibleTaskDao.queryScriptByPlaybook(playbookName);
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

        String scriptFile = ansibleScriptsPath + "/" + taskScriptDO.getUsername() + "/id_" + taskScriptDO.getId();
        return scriptFile;
    }

    @Override
    public AnsibleVersionInfo acqAnsibleVersion() {
        return acqVersion(ansibleBin, "ansible");
    }

    @Override
    public AnsibleVersionInfo acqAnsiblePlaybookVersion() {
        return acqVersion(ansiblePlaybookBin, "ansible-playbook");
    }

    private AnsibleVersionInfo acqVersion(String bin, String name) {
        try {
            CommandLine c = new CommandLine(bin);
            c.addArgument("--version");
            // 空格添加引号
            //c.addArgument(cmd, true);
            logger.info("ansible task :" + c.toString());
            String versionInfo = CmdUtils.run(c);
            AnsibleVersionInfo info = new AnsibleVersionInfo();
            info.setVersionInfo(versionInfo);
            String[] infoLine = versionInfo.split("\n");
            String v = infoLine[0];
            String version = v.replace(name + " ", "");
            info.setVersion(version);
            info.setAnsibleBinPath(bin);
            info.setAnsibleTaskScriptPath(ansibleScriptsPath);
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


    @Override
    public BusinessWrapper<Boolean> scriptTask(CmdVO cmdVO) {
        try {
            List<ServerVO> serverList = cmdVO.getServerList();

            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            if (userDO == null) {
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
                        CmdUtils.invokeAnsibleScriptTaskServer(taskServerDO, invokeStr);
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
    public PlaybookTaskDO playbookTask(DoPlaybook doPlaybook) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        PlaybookTaskDO playbookTaskDO = new PlaybookTaskDO(doPlaybook, userDO);
        TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScript(doPlaybook.getTaskScriptId());
        String playbookPath = getPlaybookPath(taskScriptDO);

        // TODO 插入任务记录
        ansibleTaskDao.addPlaybookTask(playbookTaskDO);
        schedulerManager.registerJob(() -> {
            for (PlaybookHostPattern hostPattern : doPlaybook.getPlaybookServerGroupList()) {
                // TODO 插入PlaybookLog
                PlaybookLogDO playbookLogDO = new PlaybookLogDO(taskScriptDO, userDO);
                ansibleTaskDao.addPlaybookLog(playbookLogDO);
                // TODO 插入TaskHost
                PlaybookTaskHostDO playbookTaskHostDO = new PlaybookTaskHostDO(playbookTaskDO.getId(), hostPattern, playbookLogDO.getId());
                ansibleTaskDao.addPlaybookTaskHost(playbookTaskHostDO);
                String extraVars = "hosts=" + hostPattern.getHostPatternSelected();
                if (!StringUtils.isEmpty(doPlaybook.getExtraVars())) {
                    extraVars += " " + doPlaybook.getExtraVars();
                }
                playbook(true, hostPattern.getHostPatternSelected(), playbookPath, extraVars, playbookLogDO);
                // TODO 更新TaskHost状态
                playbookTaskHostDO.setComplete(true);
                ansibleTaskDao.updatePlaybookTaskHost(playbookTaskHostDO);
            }
            playbookTaskDO.setComplete(true);
            ansibleTaskDao.updatePlaybookTask(playbookTaskDO);
        });

        return playbookTaskDO;
    }

    @Override
    public PlaybookTaskVO getPlaybookTask(long id) {
        PlaybookTaskDO playbookTaskDO = ansibleTaskDao.getPlaybookTask(id);
        PlaybookTaskVO playbookTaskVO = BeanCopierUtils.copyProperties(playbookTaskDO, PlaybookTaskVO.class);
        // TODO 任务没完成直接返回
        if (!playbookTaskVO.isComplete())
            return playbookTaskVO;
        List<PlaybookTaskHostDO> taskHostDOList = ansibleTaskDao.queryPlaybookTaskHost(playbookTaskVO.getId());
        List<PlaybookTaskHostVO> taskHostList = new ArrayList<>();
        for (PlaybookTaskHostDO playbookTaskHostDO : taskHostDOList) {
            PlaybookLogVO playbookLogVO = configService.getPlaybookLog(playbookTaskHostDO.getLogId());
            PlaybookTaskHostVO playbookTaskHostVO = BeanCopierUtils.copyProperties(playbookTaskHostDO, PlaybookTaskHostVO.class);
            playbookTaskHostVO.setPlaybookLogVO(playbookLogVO);
            taskHostList.add(playbookTaskHostVO);
        }
        playbookTaskVO.setTaskHostList(taskHostList);
        return playbookTaskVO;
    }

    /**
     * 执行 Playbook
     *
     * @param isSudo
     * @param hostPattern 分组
     * @param playbook    playbook 完整路径+文件名
     * @param extraVars   外部变量 参考 -e "hosts=ss-prod src=/data/www/data/shadowsocks/ dest=/data/www/data/shadowsocks/"
     * @return
     */
    @Override
    public TaskResult doPlaybook(boolean isSudo, String hostPattern, String playbook, String extraVars) {
        String ansible_hosts_path = configService.getAnsibleHostsAllPath();
        CommandLine c = new CommandLine(ansiblePlaybookBin);
        //c.addArgument(hostPattern);
        c.addArgument("-i");
        c.addArgument(ansible_hosts_path);
        if (isSudo)
            c.addArgument("-sudo");
        c.addArgument(playbook);
        if (!StringUtils.isEmpty(extraVars)) {
            c.addArgument("-e");
            c.addArgument(extraVars, false);
        }
        // System.err.println(c.toString());
        logger.info("Ansible Playbook :" + c.toString());
        String rt = CmdUtils.run(c);
        return new TaskResult(ansiblePlaybookBin, c.toString(), rt);
    }

    @Override
    public void playbook(boolean isSudo, String hostPattern, String playbook, String extraVars, PlaybookLogDO playbookLogDO) {
        TaskResult tr = doPlaybook(isSudo, hostPattern, playbook, extraVars);
        String logPath = getPlaybookLogPath(playbookLogDO);
        playbookLogDO.setLogPath(logPath);
        playbookLogDO.setComplete(true);
        playbookLogDO.setContent(tr.toString());
        IOUtils.writeFile(tr.getResult(), logPath);
        ansibleTaskDao.updatePlaybookLog(playbookLogDO);
    }

    private String getPlaybookLogPath(PlaybookLogDO playbookLogDO) {
        String logPath = ansibleLogsPath + "/configPlaybook/id_" + playbookLogDO.getId() + "_" + playbookLogDO.getPlaybookName();
        return logPath;
    }

    @Override
    public String getPlaybookPath(TaskScriptDO taskScriptDO) {
        String playbookPath = ansibleScriptsPath;
        playbookPath += "/" + taskScriptDO.getUsername();
        playbookPath += "/id_" + taskScriptDO.getId();
        return playbookPath;
    }



    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
