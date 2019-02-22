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
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.ConfigFilePlaybookDO;
import com.sdg.cmdb.domain.config.ConfigFilePlaybookVO;
import com.sdg.cmdb.domain.nginx.*;
import com.sdg.cmdb.domain.server.HostPattern;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.service.NginxService;
import com.sdg.cmdb.service.ServerGroupService;
import com.sdg.cmdb.service.configurationProcessor.NginxFileProcessorService;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NginxServiceImpl implements NginxService {

    @Resource
    private NginxDao nginxDao;
    @Resource
    private ServerGroupDao serverGroupDao;
    @Resource
    private AnsibleTaskDao ansibleTaskDao;

    @Resource
    private NginxFileProcessorService nginxFileProcessorService;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private UserDao userDao;

    @Resource
    private AnsibleTaskService ansibleTaskService;

    @Resource
    private SchedulerManager schedulerManager;

    private static final Logger logger = LoggerFactory.getLogger(NginxServiceImpl.class);

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
            logger.error(e.getMessage(), e);
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
        if(nginxPlaybookDO == null ) return new BusinessWrapper<Boolean>(false);
        try{
            nginxDao.delPlaybook(id);
            return new BusinessWrapper<Boolean>(true);
        }catch (Exception e){
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }



}
