package com.sdg.cmdb.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ansible.AnsibleCopyReslut;
import com.sdg.cmdb.domain.ansible.AnsibleReslut;
import com.sdg.cmdb.domain.ansible.AnsibleScriptReslut;
import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.copy.*;
import com.sdg.cmdb.domain.nginx.VhostDO;
import com.sdg.cmdb.domain.nginx.VhostEnvDO;
import com.sdg.cmdb.domain.nginx.VhostEnvVO;
import com.sdg.cmdb.domain.nginx.VhostVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CopyServiceImpl implements CopyService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(CopyServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    @Value("#{cmdb['ansible.bin']}")
    private String ansibleBin;

    @Value("#{cmdb['ansible.scripts.path']}")
    private String ansibleScriptsPath;

    public static final String BUSINESS_KEY_NGINX = "NGINX";

    public static final String COPY_LOGS_PATH = "/data/www/logs/copyLogs";

    @Resource
    private CopyDao copyDao;

    @Resource
    private NginxDao nginxDao;

    @Resource
    private AnsibleTaskDao taskDao;

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigService configService;

    @Resource
    private ConfigServerGroupService configServerGroupService;

    @Resource
    private AnsibleServerService ansibleServer;

    @Resource
    private SchedulerManager schedulerManager;

    private HashMap<String, String> ansibleConfigMap;

    private String ansibleHostsPath;

    @Override
    public TableVO<List<CopyVO>> getCopyPage(String businessKey, int page, int length) {

        long size = copyDao.getCopySize(businessKey);
        List<CopyDO> copyDOList = copyDao.getCopyPage(businessKey, page * length, length);
        List<CopyVO> voList = new ArrayList<>();
        for (CopyDO copyDO : copyDOList) {

            CopyVO copyVO = new CopyVO(copyDO);
            invokeCopyVO(copyVO);
            voList.add(copyVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public TableVO<List<CopyLogVO>> getCopyLogPage(String serverName, int envType, int page, int length) {

        long size = copyDao.getCopyLogSize(serverName, envType);
        List<CopyLogDO> copyLogDOList = copyDao.getCopyLogPage(serverName, envType, page * length, length);
        List<CopyLogVO> voList = new ArrayList<>();
        for (CopyLogDO copyLogDO : copyLogDOList) {
            CopyLogVO copyLogVO = new CopyLogVO(copyLogDO);
            copyLogVO.setTimeView(TimeViewUtils.format(copyLogDO.getGmtCreate()));
            // 执行脚本 && 日志存在
            if (copyLogDO.isDoScript() && !StringUtils.isEmpty(copyLogDO.getScriptStdoutPath()))
                copyLogVO.setScriptMsg(IOUtils.readFile(copyLogDO.getScriptStdoutPath()));
            voList.add(copyLogVO);
        }
        return new TableVO<>(size, voList);
    }


    @Override
    public CopyVO getCopy(long id) {
        CopyDO copyDO = copyDao.getCopy(id);
        CopyVO copyVO = new CopyVO(copyDO);
        invokeCopyVO(copyVO);
        return copyVO;
    }

    @Override
    public BusinessWrapper<Boolean> delCopy(long id) {
        try {
            CopyDO copyDO = copyDao.getCopy(id);
            if (copyDO == null) return new BusinessWrapper<Boolean>(false);

            List<CopyServerDO> copyServers = copyDao.queryCopyServerByCopyId(id);
            for (CopyServerDO copyServerDO : copyServers)
                copyDao.delCopyServer(copyServerDO.getId());

            List<CopyLogDO> logs = copyDao.queryCopyLogByCopyId(id);
            for (CopyLogDO log : logs)
                copyDao.delCopyLog(log.getId());

            copyDao.delCopy(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    /**
     * 支持多种类型的Copy配置
     *
     * @param copyVO
     */
    private void invokeCopyVO(CopyVO copyVO) {
        try {
            if (copyVO.getBusinessKey().equalsIgnoreCase(BUSINESS_KEY_NGINX)) {
                VhostEnvDO vhostEnvDO = nginxDao.getVhostEnv(copyVO.getBusinessId());
                copyVO.setVhostEnvDO(vhostEnvDO);

                VhostDO vhostDO = nginxDao.getVhost(vhostEnvDO.getVhostId());

                List<VhostEnvDO> envDOList = nginxDao.queryVhostEnvByVhostId(vhostDO.getId());
                List<VhostEnvVO> envVOList = new ArrayList<>();
                for (VhostEnvDO envDO : envDOList) {
                    VhostEnvVO vhostEnvVO = new VhostEnvVO(envDO, nginxDao.queryEnvFileByEnvId(envDO.getId()));
                    envVOList.add(vhostEnvVO);
                }
                VhostVO vhostVO = new VhostVO(vhostDO, envVOList);
                copyVO.setVhostVO(vhostVO);

                if (copyVO.isDoScript()) {
                    TaskScriptDO taskScriptDO = taskDao.getTaskScript(copyVO.getTaskScriptId());
                    copyVO.setTaskScriptDO(taskScriptDO);
                }

                List<CopyServerDO> copyServerDOList = copyDao.queryCopyServerByCopyId(copyVO.getId());
                List<CopyServerVO> copyServerVOList = new ArrayList<>();
                for (CopyServerDO copyServerDO : copyServerDOList) {
                    CopyServerVO copyServerVO = new CopyServerVO(copyServerDO);
                    ServerDO serverDO = serverDao.getServerInfoById(copyServerDO.getServerId());
                    copyServerVO.setServerDO(serverDO);
                    if (serverDO != null) {
                        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
                        copyServerVO.setServerGroupDO(serverGroupDO);
                    }
                    copyServerVOList.add(copyServerVO);
                }
                copyVO.setCopyServerList(copyServerVOList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveCopy(CopyDO copyDO) {
        try {
            if (copyDO.getId() == 0) {
                copyDao.addCopy(copyDO);
            } else {
                copyDao.updateCopy(copyDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> addCopyServer(long copyId, long serverId) {
        try {
            CopyServerDO copyServerDO = new CopyServerDO(copyId, serverId);
            copyDao.addCopyServer(copyServerDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delCopyServer(long id) {
        try {
            copyDao.delCopyServer(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> doCopy(long copyId) {
        CopyVO copyVO = getCopy(copyId);
        if (copyVO.getCopyServerList().size() == 0) return new BusinessWrapper<Boolean>(false);
        doCopy(copyVO);
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public BusinessWrapper<Boolean> delCopyLog(long id) {
        try {
            copyDao.delCopyLog(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    private void doCopy(CopyVO copyVO) {
        schedulerManager.registerJob(() -> {
            // 取脚本路径
            String scriptPath = "";
            if (copyVO.isDoCopy() && copyVO.isDoScript()) {
                TaskScriptDO taskScriptDO = copyVO.getTaskScriptDO();
                scriptPath = acqScriptPath(taskScriptDO);
            }
            for (CopyServerVO copyServerVO : copyVO.getCopyServerList()) {
                try {
                    ServerDO serverDO = copyServerVO.getServerDO();
                    // 判断是否使用公网ip
                    configServerGroupService.invokeGetwayIp(serverDO);
                    CopyLogDO copyLogDO = doCopy(true, serverDO.getInsideIp(), copyVO);
                    // 服务器完整名称
                    copyLogDO.setServerContent(serverDO.acqServerName());
                    copyLogDO.setServerId(copyServerVO.getServerId());
                    // 写入日志
                    copyDao.addCopyLog(copyLogDO);
                    //// TODO copy成功后执行脚本(满足3个条件:任务配置需要执行脚本 & copy任务成功 & 文件发生变化)
                    if (copyVO.isDoScript() && copyLogDO.isCopySuccess() && copyLogDO.isCopyChanged()) {
                        copyLogDO.setDoScript(true);
                        doScript(true, serverDO.getInsideIp(), scriptPath, copyLogDO);
                        copyDao.updateCopyLog(copyLogDO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 写日志文件
     *
     * @param msg
     * @param copyLogDO
     */
    private void writeLog(String msg, CopyLogDO copyLogDO) {
        String path = COPY_LOGS_PATH + "/" + TimeUtils.nowDateName() + "_id" + copyLogDO.getId() + ".log";
        copyLogDO.setScriptStdoutPath(path);
        IOUtils.writeFile(msg, path);
    }

    /**
     * 调用ansible接口执行copy
     *
     * @param isSudo
     * @param ip
     * @param copyVO
     * @return
     */
    private CopyLogDO doCopy(boolean isSudo, String ip, CopyVO copyVO) {
        String copyArgs = "src=" + copyVO.getSrcPath() + " dest=" + copyVO.getDestPath() + " owner=" + copyVO.getUsername() + " group=" + copyVO.getUsergroup();
        String msg = ansibleServer.call(isSudo, ip, ansibleHostsPath, AnsibleServerServiceImpl.ANSIBLE_MODULE_COPY, copyArgs);
        // 处理返回信息
        CopyLogDO copyLogDO = new CopyLogDO(copyVO);
        copyLogDO.setCopyMsg(msg);
        // 解析原始日志
        translateCopy(copyLogDO);
        return copyLogDO;
    }


    /**
     * 调用ansible接口执行script
     *
     * @param isSudo
     * @param ip
     * @return
     */
    private void doScript(boolean isSudo, String ip, String scriptPath, CopyLogDO copyLogDO) {
        String msg = ansibleServer.call(isSudo, ip, ansibleHostsPath, AnsibleServerServiceImpl.ANSIBLE_MODULE_SCRIPT, scriptPath);
        //System.err.println(msg);
        // 解析原始日志
        translateScript(copyLogDO, msg);
        // 写日志文件
        writeLog(msg, copyLogDO);
    }


    /**
     * 解析Copy返回信息
     *
     * @param copyLogDO
     */
    private void translateCopy(CopyLogDO copyLogDO) {
        try {
            AnsibleReslut ansibleReslut = buildAnsibleReslut(copyLogDO.getCopyMsg());
            copyLogDO.setCopyResult(ansibleReslut.getReslut());
            copyLogDO.setCopySuccess(ansibleReslut.isSuccess());

            Gson gson = new GsonBuilder().create();
            AnsibleCopyReslut copyReslut = gson.fromJson(ansibleReslut.getBody(), AnsibleCopyReslut.class);
            copyLogDO.setCopyChanged(copyReslut.isChanged());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解析Ansible copy返回信息失败！");
        }
    }

    /**
     * 解析Script返回信息
     *
     * @param copyLogDO
     */
    private void translateScript(CopyLogDO copyLogDO, String msg) {
        try {
            AnsibleReslut ansibleReslut = buildAnsibleReslut(msg);
            copyLogDO.setScriptResult(ansibleReslut.getReslut());
            copyLogDO.setScriptSuccess(ansibleReslut.isSuccess());
            Gson gson = new GsonBuilder().create();
            AnsibleScriptReslut scriptReslut = gson.fromJson(ansibleReslut.getBody(), AnsibleScriptReslut.class);

            copyLogDO.setScriptChanged(scriptReslut.isChanged());
            copyLogDO.setScriptRc(scriptReslut.getRc());
            copyLogDO.setScriptStderr(scriptReslut.getStderr());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解析Ansible script返回信息失败！");
        }
    }

    private AnsibleReslut buildAnsibleReslut(String rcMsg) {
        String[] strs = rcMsg.split("=>");
        if (strs.length == 2) {
            return new AnsibleReslut(strs[0].replaceAll(" ", ""), strs[1]);
        } else {
            return new AnsibleReslut();
        }
    }


    /**
     * 获取脚本路径
     *
     * @param taskScriptDO
     * @return
     */
    private String acqScriptPath(TaskScriptDO taskScriptDO) {

        String scriptFile = ansibleScriptsPath + "/" + taskScriptDO.getUsername() + "/id_" + taskScriptDO.getId();
        return scriptFile;
    }

    /**
     * 初始化
     *
     * @return
     */
    private void init() {
        ansibleHostsPath = configService.getAnsibleHostsAllPath();

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

}
