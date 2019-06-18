package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.AnsibleTaskDao;
import com.sdg.cmdb.dao.cmdb.NginxDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ansibleTask.PlaybookLogDO;
import com.sdg.cmdb.domain.ansibleTask.PlaybookLogVO;
import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceCluster;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceDO;
import com.sdg.cmdb.domain.nginx.*;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.HostPattern;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.configurationProcessor.ConfigurationProcessorAbs;
import com.sdg.cmdb.service.configurationProcessor.NginxFileProcessorService;
import com.sdg.cmdb.service.configurationProcessor.NginxTcpProcessorService;
import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class NginxServiceImpl implements NginxService {


    @Value("#{cmdb['nginx.tcp.test.vhost.path']}")
    private String nginxTcpTestVhost;

    @Value("#{cmdb['nginx.tcp.pre.vhost.path']}")
    private String nginxTcpPreVhost;

    @Autowired
    private NginxDao nginxDao;
    @Autowired
    private ServerGroupDao serverGroupDao;
    @Autowired
    private AnsibleTaskDao ansibleTaskDao;

    @Autowired
    private NginxFileProcessorService nginxFileProcessorService;

    @Autowired
    private ServerGroupService serverGroupService;

    @Autowired
    private KubernetesService kubernetesService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnsibleTaskService ansibleTaskService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SchedulerManager schedulerManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private NginxTcpProcessorService nginxTcpProcessorService;

    @Override
    public TableVO<List<VhostVO>> getVhostPage(String serverName, int page, int length) {

        long size = nginxDao.getVhostSize(serverName);
        List<VhostDO> list = nginxDao.getVhostPage(serverName, page * length, length);
        List<VhostVO> voList = new ArrayList<>();
        for (VhostDO vhostDO : list)
            voList.add(getVhostVO(vhostDO));
        return new TableVO<>(size, voList);
    }


    @Override
    public BusinessWrapper<Boolean> delVhost(long id) {
        try {
            VhostDO vhostDO = nginxDao.getVhost(id);
            if (vhostDO == null)
                return new BusinessWrapper<Boolean>(false);

            List<VhostEnvDO> vhostEnvList = nginxDao.queryVhostEnvByVhostId(id);
            for (VhostEnvDO envDO : vhostEnvList)
                delEnvFileByEnvId(envDO.getId());

            List<VhostServerGroupDO> groups = nginxDao.queryVhostServerGroupByVhostId(id);
            for (VhostServerGroupDO group : groups)
                delServerGroup(group.getId());

            nginxDao.delVhost(id);
            return new BusinessWrapper<Boolean>(true);

        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public VhostVO getVhost(long id) {
        VhostDO vhostDO = nginxDao.getVhost(id);
        return getVhostVO(vhostDO);

    }

    private VhostVO getVhostVO(VhostDO vhostDO) {
        List<VhostEnvVO> envVOList = new ArrayList<>();
        List<VhostEnvDO> envList = nginxDao.queryVhostEnvByVhostId(vhostDO.getId());
        for (VhostEnvDO vhostEnvDO : envList) {
            envVOList.add(getVhostEnvVO(vhostEnvDO));
        }
        VhostVO vhostVO = new VhostVO(vhostDO, envVOList);
        return vhostVO;
    }

    private VhostEnvVO getVhostEnvVO(VhostEnvDO vhostEnvDO) {
        List<EnvFileDO> envFiles = nginxDao.queryEnvFileByEnvId(vhostEnvDO.getId());
        VhostEnvVO vhostEnvVO = new VhostEnvVO(vhostEnvDO, envFiles);
        return vhostEnvVO;
    }


    @Override
    public BusinessWrapper<Boolean> saveVhost(VhostVO vhostVO) {
        VhostDO vhostDO = new VhostDO(vhostVO);
        if (!saveVhost(vhostDO))
            return new BusinessWrapper<Boolean>(false);

        if (vhostVO.getEnvList() == null || vhostVO.getEnvList().size() == 0)
            return new BusinessWrapper<Boolean>(true);

        for (VhostEnvDO envDO : vhostVO.getEnvList()) {
            if (!saveEnv(envDO))
                return new BusinessWrapper<Boolean>(false);
        }
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public BusinessWrapper<Boolean> saveVhostEnv(VhostEnvDO vhostEnvDO) {
        return new BusinessWrapper<Boolean>(saveEnv(vhostEnvDO));
    }

    @Override
    public BusinessWrapper<Boolean> delVhostEnv(long id) {
        try {
            if (delEnvFileByEnvId(id)) {
                nginxDao.delVhostEnv(id);
                return new BusinessWrapper<Boolean>(true);
            } else {
                return new BusinessWrapper<Boolean>(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    private boolean delEnvFileByEnvId(long envId) {
        try {
            List<EnvFileDO> envFiles = nginxDao.queryEnvFileByEnvId(envId);
            for (EnvFileDO envFileDO : envFiles)
                nginxDao.delEnvFile(envFileDO.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public BusinessWrapper<Boolean> saveEnvFile(EnvFileDO envFileDO) {
        try {
            nginxDao.updateEnvFile(envFileDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<NginxFile> launchEnvFile(long envFileId, int type) {

        try {

            NginxFile nf = getNginxFileById(envFileId);
            String file;
            if (type == 0) {
                nginxFileProcessorService.invokeFile(nf);
            } else {
                file = IOUtils.readFile(nf.getPath());
                nf.setFile(file);
            }
            return new BusinessWrapper<>(nf);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure.getCode(), "文件不存在!");
        }
    }

    @Override
    public BusinessWrapper<Boolean> buildEnvFile(long envFileId) {
        try {
            NginxFile nf = getNginxFileById(envFileId);
            nginxFileProcessorService.invokeFile(nf);
            IOUtils.writeFile(nf.getFile(), nf.getPath());
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> buildEnvFile(long envFileId, boolean auto) {
        if (!auto)
            return buildEnvFile(envFileId);
        EnvFileDO envFileDO = nginxDao.getEnvFile(envFileId);
        if (envFileDO == null) return new BusinessWrapper<Boolean>(false);
        if (envFileDO.getFileType() == EnvFileDO.FileTypeEnum.location.getCode())
            return buildEnvFile(envFileId);
        VhostEnvDO vhostEnvDO = nginxDao.getVhostEnv(envFileDO.getEnvId());
        if (vhostEnvDO.isAutoBuild())
            return buildEnvFile(envFileId);
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public BusinessWrapper<Boolean> addServerGroup(long vhostId, long serverGroupId) {
        try {
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverGroupId);
            VhostServerGroupDO vhostServerGroupDO = new VhostServerGroupDO(vhostId, serverGroupDO);
            nginxDao.addVhostServerGroup(vhostServerGroupDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delServerGroup(long id) {
        try {
            nginxDao.delVhostServerGroup(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public List<VhostServerGroupVO> queryServerGroup(long vhostId) {
        List<VhostServerGroupVO> voList = new ArrayList<>();
        try {
            List<VhostServerGroupDO> list = nginxDao.queryVhostServerGroupByVhostId(vhostId);
            //List<VhostServerGroupVO> voList = new ArrayList<>();
            for (VhostServerGroupDO vhostServerGroupDO : list) {
                ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(vhostServerGroupDO.getServerGroupId());
                if (serverGroupDO == null) {
                    nginxDao.delVhostServerGroup(vhostServerGroupDO.getId());
                } else {
                    VhostServerGroupVO vhostServerGroupVO = new VhostServerGroupVO(vhostServerGroupDO, serverGroupDO);
                    voList.add(vhostServerGroupVO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voList;
    }


    private NginxFile getNginxFileById(long envFileId) {
        try {
            EnvFileDO envFileDO = nginxDao.getEnvFile(envFileId);
            VhostEnvDO vhostEnvDO = nginxDao.getVhostEnv(envFileDO.getEnvId());
            VhostDO vhostDO = nginxDao.getVhost(vhostEnvDO.getVhostId());
            String filePath = nginxFileProcessorService.getFilePath(vhostEnvDO, envFileDO);
            NginxFile nf = new NginxFile(vhostDO, vhostEnvDO, envFileDO, filePath);
            return nf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NginxFile();
    }


    private boolean saveVhost(VhostDO vhostDO) {
        try {
            if (vhostDO.getId() == 0) {
                nginxDao.addVhost(vhostDO);
            } else {
                nginxDao.updateVhost(vhostDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean saveEnv(VhostEnvDO vhostEnvDO) {
        try {
            if (vhostEnvDO.getId() == 0) {
                nginxDao.addVhostEnv(vhostEnvDO);
                createEnvFile(vhostEnvDO.getId());
            } else {
                nginxDao.updateVhostEnv(vhostEnvDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean createEnvFile(long envId) {
        try {
            EnvFileDO envFileLocation = new EnvFileDO(envId, EnvFileDO.FileTypeEnum.location.getCode());
            nginxDao.addEnvFile(envFileLocation);

            EnvFileDO envFileUpstream = new EnvFileDO(envId, EnvFileDO.FileTypeEnum.upstream.getCode());
            nginxDao.addEnvFile(envFileUpstream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<NginxPlaybookVO> getPlaybookPage() {
        List<NginxPlaybookDO> playbookList = nginxDao.queryPlaybookPage();
        List<NginxPlaybookVO> voList = new ArrayList<>();
        for (NginxPlaybookDO nginxPlaybookDO : playbookList)
            voList.add(getNginxPlaybookVO(nginxPlaybookDO));
        return voList;
    }

    private NginxPlaybookVO getNginxPlaybookVO(NginxPlaybookDO nginxPlaybookDO) {
        VhostVO vhostVO = getVhost(nginxPlaybookDO.getVhostId());
        TaskScriptDO taskScriptDO = ansibleTaskDao.getTaskScript(nginxPlaybookDO.getPlaybookId());
        List<HostPattern> hostPattern = serverGroupService.getHostPattern(nginxPlaybookDO.getServerGroupId());
        NginxPlaybookVO nginxPlaybookVO = new NginxPlaybookVO(nginxPlaybookDO, vhostVO, taskScriptDO);
        nginxPlaybookVO.setGroupHostPattern(hostPattern);
        return nginxPlaybookVO;
    }

    @Override
    public BusinessWrapper<Boolean> savePlaybook(NginxPlaybookDO nginxPlaybookDO) {
        try {
            if (nginxPlaybookDO.getId() == 0) {
                nginxDao.addPlaybook(nginxPlaybookDO);
            } else {
                nginxDao.updatePlaybook(nginxPlaybookDO);
            }
            return new BusinessWrapper<Boolean>(true);

        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    /**
     * @param id
     * @param doType 0 系统 1 人工
     * @return
     */
    @Override
    public PlaybookLogVO doPlaybook(long id, int doType) {
        NginxPlaybookDO nginxPlaybookDO = nginxDao.getPlaybook(id);

        NginxPlaybookVO nginxPlaybookVO = getNginxPlaybookVO(nginxPlaybookDO);
        String playbookPath = ansibleTaskService.getPlaybookPath(nginxPlaybookVO.getTaskScriptDO());
        String extraVars = "hosts=" + nginxPlaybookVO.getHostPattern() + " src=" + nginxPlaybookVO.getSrc() + " dest=" + nginxPlaybookVO.getDest();
        PlaybookLogDO pl;
        if (doType == 0) {
            pl = new PlaybookLogDO(nginxPlaybookVO);
        } else {
            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            pl = new PlaybookLogDO(nginxPlaybookVO, userDO);
        }
        ansibleTaskDao.addPlaybookLog(pl);
        schedulerManager.registerJob(() -> {
            ansibleTaskService.playbook(true, nginxPlaybookVO.getHostPattern(), playbookPath, extraVars, pl);
        });
        return new PlaybookLogVO(pl);
    }

    @Override
    public BusinessWrapper<Boolean> delPlaybook(long id) {
        NginxPlaybookDO nginxPlaybookDO = nginxDao.getPlaybook(id);
        if (nginxPlaybookDO == null) return new BusinessWrapper<Boolean>(false);
        try {
            nginxDao.delPlaybook(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public boolean addNginxTcp(long serverGroupId, int env, String portName) {
        KubernetesServiceCluster kubernetesServiceCluster = kubernetesService.getServerList(serverGroupId, env, portName, 0);
        if (kubernetesServiceCluster.getServerList() == null) return false;
        NginxTcpVO nginxTcpVO = NginxTcpVO.builder(kubernetesServiceCluster, EnvType.EnvTypeEnum.test.getCode(), portName);
        try {
            if (checkNginxTcp(nginxTcpVO)) {
                nginxDao.addNginxTcp(nginxTcpVO);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public BusinessWrapper<Boolean> autoNginxTcp() {
        // 同步dubbo数据
        kubernetesService.syncDubbo();
        List<ServerGroupDO> list = serverGroupDao.queryServerGroup();
        for (ServerGroupDO serverGroupDO : list) {
            addNginxTcp(serverGroupDO.getId(), EnvType.EnvTypeEnum.test.getCode(), "dubbo");
            addNginxTcp(serverGroupDO.getId(), EnvType.EnvTypeEnum.test.getCode(), "debug");
        }
        configService.invokeNginxTcpConfig(EnvType.EnvTypeEnum.test.getCode());
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public BusinessWrapper<Boolean> scanNginxTcp() {
        cleanNginxTcpDubbo();
        // 重新生成dubbo直连配置
        scanNginxTcpDubbo();
        return new BusinessWrapper<Boolean>(true);
    }

    /**
     * 清理所有NginxTcpDubbo配置
     */
    private void cleanNginxTcpDubbo(){
        String clusterName = KubernetesServiceImpl.DUBBO_CLUSTER + ":" + KubernetesServiceImpl.DUBBO_CLUSTER_NAMESPACE;
        List<NginxTcpDubboDO> list = nginxDao.queryNginxTcpDubbo(clusterName);
        // 删除所有dubbo配置缓存
        for (NginxTcpDubboDO nginxTcpDubboDO : list)
            nginxDao.delNginxTcpDubbo(nginxTcpDubboDO.getId());
        autoNginxTcp();
        // 清理缓存
        nginxTcpProcessorService.cleanTcpDubboCache(clusterName);
    }

    @Override
    public NginxTcpVO getNginxTcp(long serverGroupId, int env, String portName) {
        KubernetesServiceCluster kubernetesServiceCluster = kubernetesService.getServerList(serverGroupId, env, portName, 0);
        if (kubernetesServiceCluster.getServerList() == null) return new NginxTcpVO();
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        NginxTcpVO nginxTcpVO = NginxTcpVO.builder(kubernetesServiceCluster, env, portName, userDO);
        NginxTcpDO check = nginxDao.checkNginxTcp(nginxTcpVO);
        if (check == null) {
            return nginxTcpVO;
        } else {
            return BeanCopierUtils.copyProperties(check, NginxTcpVO.class);
        }
    }

    /**
     * 校验NginxTcp配置是否存在或过期
     *
     * @param nginxTcpDO
     * @return true 通过校验(可插入数据)
     */
    private boolean checkNginxTcp(NginxTcpDO nginxTcpDO) {
        NginxTcpDO check = nginxDao.checkNginxTcp(nginxTcpDO);
        if (check == null) {
            return true;
        } else {
            return isExpired(check);
        }
    }

    /**
     * 是否过期
     *
     * @param nginxTcpDO
     * @return true 过期
     */
    private boolean isExpired(NginxTcpDO nginxTcpDO) {
        // 过期更新字段
        if (TimeUtils.calculateDateExpired(nginxTcpDO.getGmtExpired())) {
            nginxDao.updateNginxTcpFinished(nginxTcpDO);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String getNginxTcpServerConf(int envType) {
        List<NginxTcpDO> list = nginxDao.queryNginxTcpAll(envType);
        String body = "";
        for (NginxTcpDO nginxTcpDO : list) {
            if (!isExpired(nginxTcpDO)) {
                KubernetesServiceDO kubernetesServiceDO = kubernetesService.getKubernetesService(nginxTcpDO.getServerGroupId(), envType, nginxTcpDO.getPortName());
                body += nginxTcpProcessorService.getTcpFile(kubernetesServiceDO, nginxTcpDO) + "\n";
            }
        }
        return ConfigurationProcessorAbs.getHeadInfo() + body;
    }

    @Override
    public TableVO<List<NginxTcpVO>> getNginxTcpPage(String serviceName, int envType, int page, int length) {
        String username = SessionUtils.getUsername();
        boolean isAdmin = authService.isRole(username, RoleDO.roleAdmin);
        int size = nginxDao.getNginxTcpSize(isAdmin, username, serviceName, envType);
        List<NginxTcpDO> list = nginxDao.getNginxTcpPage(isAdmin, username, serviceName, envType, page * length, length);
        List<NginxTcpVO> voList = new ArrayList<>();
        for (NginxTcpDO nginxTcpDO : list) {
            NginxTcpVO nginxTcpVO = BeanCopierUtils.copyProperties(nginxTcpDO, NginxTcpVO.class);
            nginxTcpVO.setEnv(new EnvType(nginxTcpDO.getEnvType()));
            nginxTcpVO.setDomain(NginxTcpVO.DomainEnum.getDomainName(nginxTcpVO.getEnvType()));
            voList.add(nginxTcpVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public NginxTcpVO createNginxTcp(NginxTcpVO nginxTcpVO) {
        long time = new Date().getTime() + nginxTcpVO.getPeriod() * 60 * 1000;
        nginxTcpVO.setGmtExpired(TimeUtils.stampToDate(String.valueOf(time)));
        try {
            if (checkNginxTcp(nginxTcpVO)) {
                nginxDao.addNginxTcp(nginxTcpVO);
                configService.invokeNginxTcpConfig(nginxTcpVO.getEnvType());
                return nginxTcpVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nginxTcpVO;
    }

    @Override
    public BusinessWrapper<Boolean> delNginxTcp(long id) {
        try {
            NginxTcpDO nginxTcpDO = nginxDao.getNginxTcp(id);
            if (nginxTcpDO == null) return new BusinessWrapper<Boolean>(false);
            nginxDao.updateNginxTcpFinished(nginxTcpDO);
            configService.invokeNginxTcpConfig(nginxTcpDO.getEnvType());
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public String scanNginxTcpDubbo() {
        // 只扫描测试集群
        return nginxTcpProcessorService.getTcpDubboFile("k8s-test:test");
    }

}
