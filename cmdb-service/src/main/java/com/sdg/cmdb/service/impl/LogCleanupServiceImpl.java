package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.AnsibleTaskDao;
import com.sdg.cmdb.dao.cmdb.LogcleanupDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ansibleTask.AnsibleTaskServerDO;
import com.sdg.cmdb.domain.logCleanup.LogcleanupDO;
import com.sdg.cmdb.domain.logCleanup.LogcleanupVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.service.LogcleanupService;
import com.sdg.cmdb.service.ServerService;
import com.sdg.cmdb.service.ZabbixServerService;
import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.ByteUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogcleanupServiceImpl implements LogcleanupService {

    private static final Logger logger = LoggerFactory.getLogger(LogcleanupServiceImpl.class);

    /**
     * 磁盘清理阈值
     */
    public static final int LOGCLEANUP_FREE_RATE = 20;

    public static final int LOGCLEANUP_HISTORY_MAX_DAY = 30;
    public static final int LOGCLEANUP_HISTORY_MIN_DAY = 7;

    @Autowired
    private LogcleanupDao logcleanupDao;

    @Autowired
    private ZabbixServerService zabbixServerService;

    @Autowired
    private AnsibleTaskService ansibleTaskService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private AnsibleTaskDao ansibleTaskDao;

    @Autowired
    private ServerDao serverDao;

    @Override
    public TableVO<List<LogcleanupVO>> getLogcleanupPage(long serverGroupId, String serverName, int enabled, int page, int length) {
        long size = logcleanupDao.getLogcleanupSize(serverGroupId, serverName, enabled);
        List<LogcleanupDO> list = logcleanupDao.getLogcleanupPage(serverGroupId, serverName, enabled, page * length, length);
        List<LogcleanupVO> voList = new ArrayList<>();
        for (LogcleanupDO logcleanupDO : list) {
            LogcleanupVO logcleanupVO = BeanCopierUtils.copyProperties(logcleanupDO, LogcleanupVO.class);
            logcleanupVO.setServerDO(serverService.getServerById(logcleanupDO.getServerId()));
            if (!StringUtils.isEmpty(logcleanupVO.getUpdateTime()))
                logcleanupVO.setUpdateTimeView(TimeViewUtils.format(logcleanupVO.getUpdateTime()));
            if (!StringUtils.isEmpty(logcleanupVO.getCleanupTime()))
                logcleanupVO.setCleanupTimeView(TimeViewUtils.format(logcleanupVO.getCleanupTime()));
            try {
                logcleanupVO.setSizeView(ByteUtils.getSize(logcleanupVO.getUsedDiskSpace()) + "/" + ByteUtils.getSize(logcleanupVO.getTotalDiskSpace()));
            } catch (Exception e) {
            }
            voList.add(logcleanupVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public TableVO<List<AnsibleTaskServerDO>> getLogcleanupTaskLogPage(long id, int page, int length) {
        LogcleanupDO logcleanupDO = logcleanupDao.getLogcleanup(id);
        long size = ansibleTaskDao.getAnsibleTaskServerSize(logcleanupDO.getScriptId(), logcleanupDO.getServerId());
        List<AnsibleTaskServerDO> list = ansibleTaskDao.queryAnsibleTaskServerPage(logcleanupDO.getScriptId(), logcleanupDO.getServerId(), page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public BusinessWrapper<Boolean> save(LogcleanupDO logcleanupDO) {
        if (logcleanupDO.getHistory() == 0)
            logcleanupDO.setHistory(LogcleanupDO.HISTORY_DEFAULT);
        try {
            logcleanupDao.updateLogcleanupConfig(logcleanupDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    /**
     * 判断清理任务是否执行:
     * 磁盘空间超过阈值，则执行清理任务，并记录cleanupTime
     *
     * @param logcleanupDO
     * @return
     */
    private boolean checkTask(LogcleanupDO logcleanupDO) {
        // TODO 禁用
        if (!logcleanupDO.isEnabled())
            return false;
        // TODO 判断磁盘空间阈值
        if (logcleanupDO.getFreeDiskSpace() <= LOGCLEANUP_FREE_RATE) {
            if (logcleanupDO.getHistory() > LOGCLEANUP_HISTORY_MIN_DAY) {
                logcleanupDO.setHistory(logcleanupDO.getHistory() - 1);
                logcleanupDO.setCleanupTime(TimeUtils.nowDate());
                logcleanupDao.updateLogcleanupTask(logcleanupDO);
                return true;
            }
            // TODO 已经低于日志归档最小天数，不执行清理
            return false;
        } else {
            // TODO 磁盘空间未到清理阈值，需要计算是否增加归档天数
            if (StringUtils.isEmpty(logcleanupDO.getCleanupTime())) {
                logcleanupDO.setCleanupTime(TimeUtils.nowDate());
                logcleanupDao.updateLogcleanupTask(logcleanupDO);
                return false;
            }
            // TODO 距离1天则更新归档天数
            if (TimeUtils.calculateDateDiff4Day(logcleanupDO.getCleanupTime()) >= 1) {
                if (logcleanupDO.getHistory() < LOGCLEANUP_HISTORY_MAX_DAY) {
                    logcleanupDO.setHistory(logcleanupDO.getHistory() + 1);
                    logcleanupDO.setCleanupTime(TimeUtils.nowDate());
                    logcleanupDao.updateLogcleanupTask(logcleanupDO);
                }
            }
            return false;
        }
    }

    @Override
    public void task() {
        List<LogcleanupDO> list = logcleanupDao.getLogcleanupByEnabled();
        for (LogcleanupDO logcleanupDO : list) {
            try {
                doTask(logcleanupDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public AnsibleTaskServerDO doTask(long id) {
        LogcleanupDO logcleanupDO = logcleanupDao.getLogcleanup(id);
        return doTask(logcleanupDO);
    }

    private AnsibleTaskServerDO doTask(LogcleanupDO logcleanupDO) {
        AnsibleTaskServerDO ansibleTaskServerDO = new AnsibleTaskServerDO();
        if (checkTask(logcleanupDO)) {
            ansibleTaskServerDO = ansibleTaskService.scriptLogcleanup(logcleanupDO);
            // TODO 更新任务执行结果 cleanupResult
            logcleanupDao.updateLogcleanupTask(logcleanupDO);
        }
        return ansibleTaskServerDO;
    }


    /**
     * 更新服务器日志清理配置
     */
    @Override
    public BusinessWrapper<Boolean> updateLogcleanupServers() {
        List<ServerDO> serverList = serverDao.queryZabbixServer();
        for (ServerDO serverDO : serverList)
            updateLogcleanupServer(serverDO);
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public BusinessWrapper<Boolean> updateLogcleanupConfig(long serverId) {
        ServerDO serverDO = serverService.getServerById(serverId);
        return new BusinessWrapper<>(updateLogcleanupServer(serverDO));
    }

    private boolean updateLogcleanupServer(ServerDO serverDO) {
        try {
            // 查询配置是否存在
            LogcleanupDO logcleanupDO = logcleanupDao.getLogcleanupByServerId(serverDO.getId());
            // 更新服务器信息
            if (logcleanupDO != null) {
                logcleanupDO = updateLogcleanupServerInfo(logcleanupDO, serverDO);
            } else {
                logcleanupDO = new LogcleanupDO(serverDO);
            }
            // 插入采样信息
            zabbixServerService.invokeLogcleanupDiskInfo(logcleanupDO);
            saveLogcleanup(logcleanupDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveLogcleanup(LogcleanupDO logcleanupDO) {
        try {
            if (logcleanupDO.getId() == 0) {
                logcleanupDao.addLogcleanup(logcleanupDO);
            } else {
                logcleanupDao.updateLogcleanup(logcleanupDO);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 更新服务器信息
     *
     * @param logcleanupDO
     * @param serverDO
     * @return
     */
    private LogcleanupDO updateLogcleanupServerInfo(LogcleanupDO logcleanupDO, ServerDO serverDO) {
        if (!logcleanupDO.acqServerName().equals(serverDO.acqServerName())) {
            logcleanupDO.setServerName(serverDO.getServerName());
            logcleanupDO.setEnvType(serverDO.getEnvType());
            logcleanupDO.setServerName(serverDO.getServerName());
            logcleanupDO.setServerGroupId(serverDO.getServerGroupId());
            try {
                logcleanupDao.updateLogcleanupServerInfo(logcleanupDO);
            } catch (Exception e) {
                return null;
            }
        }
        return logcleanupDO;
    }


}
