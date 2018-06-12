package com.sdg.cmdb.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.dao.cmdb.LogCleanupDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.logCleanup.LogCleanupPropertyDO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.domain.zabbix.ZabbixResult;
import com.sdg.cmdb.plugin.cache.CacheZabbixService;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.ServerGroupService;
import com.sdg.cmdb.service.ServerPerfService;
import com.sdg.cmdb.service.ZabbixHistoryService;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liangjian on 2017/2/25.
 */
@Service
public class ServerPerfServiceImpl implements ServerPerfService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ServerPerfServiceImpl.class);

    public static final int ZABBIX_HISTORY_LIMIT = 2000;

    @Resource
    protected ZabbixHistoryService zabbixHistoryService;

    @Resource
    private CacheZabbixService cacheZabbixService;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private LogCleanupDao logCleanupDao;

    @Resource
    private AuthService authService;

    @Resource
    private ServerDao serverDao;

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    protected ConfigDao configDao;

    private static String[] diskSysVolume = {"sda", "vda"};

    private static String[] diskDataVolume = {"sdb", "vdb", "vdc"};

    public static final String zabbix_disk_sys_volume = "ZABBIX_DISK_SYS_VOLUME_NAME";

    public static final String zabbix_disk_data_volume = "ZABBIX_DISK_DATA_VOLUME_NAME";

    public static final String zabbix_cacha_status = "serverPerfServiceCache:status";

    public static final String zabbix_cacha_gmtModify = "serverPerfServiceCache:gmtModify";

    @Override
    public TableVO<List<ServerPerfVO>> getServerPerfPage(long serverGroupId, String serverName, int useType, int envType, String queryIp, int page, int length) {
        List<Long> groupFilter = authService.getUserGroupIds(SessionUtils.getUsername());
        long size = serverDao.getServerPerfSize(groupFilter, serverGroupId, serverName, useType, envType, queryIp);
        List<ServerDO> list = serverDao.getServerPerfPage(groupFilter, serverGroupId, serverName, useType, envType, queryIp, page * length, length);
        List<ServerPerfVO> voList = new ArrayList<>();
        for (ServerDO serverDO : list) {
            voList.add(getCache(serverDO));
        }
        statistics(voList);
        return new TableVO<>(size, voList);
    }


    private void statistics(List<ServerPerfVO> voList) {
        int memoryRate = 0;
        float load = 0;
        float cpuUser = 0;
        int cnt = 0;
        for (ServerPerfVO serverPerfVO : voList) {
            if (serverPerfVO.getEnvType() == ServerDO.EnvTypeEnum.prod.getCode()) {
                memoryRate += Integer.parseInt(serverPerfVO.getMemoryRate());
                load += Float.parseFloat(serverPerfVO.getLoad1());
                cpuUser += Float.parseFloat(serverPerfVO.getCpuUser());
                cnt++;
            }
        }
        ServerStatisticsDO serverStatisticsDO = new ServerStatisticsDO();
        if (cnt == 0) {
            cacheZabbixService.insert(serverStatisticsDO);
            return;
        }
        serverStatisticsDO.setMemoryRate(memoryRate / cnt);
        serverStatisticsDO.setLoad(load / cnt);
        serverStatisticsDO.setCpuUser((int) (cpuUser / cnt));
        serverStatisticsDO.setCnt(cnt);
        cacheZabbixService.insert(serverStatisticsDO);
    }


    @Override
    public ServerStatisticsDO statistics() {
        return cacheZabbixService.queryServerStatistics();
    }

    @Override
    public TaskVO taskGet() {
        String gmtModify = cacheZabbixService.getKeyByString(zabbix_cacha_gmtModify);
        String isRunning = cacheZabbixService.getKeyByString(zabbix_cacha_status);
        if (isRunning.equalsIgnoreCase("true")) {
            return new TaskVO(gmtModify, true);
        } else {
            return new TaskVO(gmtModify, false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> taskReset() {
        cacheZabbixService.set(zabbix_cacha_status, "false");
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> taskRun() {
        cacheZabbixService.set(zabbix_cacha_status, "false");
        schedulerManager.registerJob(() -> {
            this.cache();
        });


        return new BusinessWrapper<>(true);
    }

    /**
     * 计算最大浮点数
     *
     * @param results
     * @return
     */
    private String calMaxValueByFloat(List<ZabbixResult> results) {
        if (results == null || results.size() == 0) {
            return null;
        }
        List<Float> values = new ArrayList<>();
        for (ZabbixResult zabbixResult : results) {
            values.add(Float.valueOf(zabbixResult.getValue()));
            //System.err.println(Float.valueOf(zabbixResult.getValue()));
        }
        return Collections.max(values).toString();
    }

    private String calMaxValueByLong(List<ZabbixResult> results) {
        List<Long> values = new ArrayList<>();
        for (ZabbixResult zabbixResult : results) {
            values.add(Long.valueOf(zabbixResult.getValue()));
            //System.err.println(Float.valueOf(zabbixResult.getValue()));
        }
        return Collections.max(values).toString();
    }

    private String calMinValueByLong(List<ZabbixResult> results) {
        List<Long> values = new ArrayList<>();
        for (ZabbixResult zabbixResult : results) {
            values.add(Long.valueOf(zabbixResult.getValue()));
            //System.err.println(Float.valueOf(zabbixResult.getValue()));
        }
        return Collections.min(values).toString();
    }

    public void invokePerf(ServerPerfVO serverPerfVO, ServerDO serverDO) {
        //SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        //serverPerfVO.setGmtModify(df.format(new Date()));
        serverPerfVO.setGmtModify(TimeUtils.nowDate());
        // cpu user
        try {
            List<ZabbixResult> results = zabbixHistoryService.acqResults(zabbixHistoryService.queryCpuUser(serverDO, ZABBIX_HISTORY_LIMIT));
            String cpuUserMax = calMaxValueByFloat(results);

            //String cpuUser = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryCpuUser(serverDO, 1));
            //BigDecimal bigDecimal = new BigDecimal(Float.valueOf(cpuUser));
            System.err.println("cpuUserMax:" + cpuUserMax);
            BigDecimal bigDecimal = new BigDecimal(Float.valueOf(cpuUserMax));
            serverPerfVO.setCpuUser(bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        } catch (Exception e) {
            logger.error("计算cpu使用率错误", e);
            return;
        }

        // cpu iowait
        try {
            //String cpuIowait = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryCpuIowait(serverDO, 1));
            List<ZabbixResult> results = zabbixHistoryService.acqResults(zabbixHistoryService.queryCpuIowait(serverDO, ZABBIX_HISTORY_LIMIT));
            String cpuIowaitMax = calMaxValueByFloat(results);

            BigDecimal bigDecimal = new BigDecimal(Float.valueOf(cpuIowaitMax));
            serverPerfVO.setCpuIowait(bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        } catch (Exception e) {
            logger.error("计算cpu-iowait错误", e);
            return;
        }

        // load
        try {
            // load avg 1
            //String load1 = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryPerCpuAvg1(serverDO, 1));
            List<ZabbixResult> results1 = zabbixHistoryService.acqResults(zabbixHistoryService.queryPerCpuAvg1(serverDO, ZABBIX_HISTORY_LIMIT));
            String load1Max = calMaxValueByFloat(results1);
            if (StringUtils.isEmpty(load1Max)) {
                serverPerfVO.setLoad1("-1");
            } else {
                BigDecimal bigDecimal1 = new BigDecimal(Float.valueOf(load1Max));
                serverPerfVO.setLoad1(bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
            // load avg 5
            //String load5 = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryPerCpuAvg5(serverDO, 1));
            List<ZabbixResult> results5 = zabbixHistoryService.acqResults(zabbixHistoryService.queryPerCpuAvg5(serverDO, ZABBIX_HISTORY_LIMIT));
            String load5Max = calMaxValueByFloat(results5);
            if (StringUtils.isEmpty(load5Max)) {
                serverPerfVO.setLoad5("-1");
            } else {
                BigDecimal bigDecimal5 = new BigDecimal(Float.valueOf(load5Max));
                serverPerfVO.setLoad5(bigDecimal5.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
            // load avg 15
            //String load15 = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryPerCpuAvg15(serverDO, 1));
            List<ZabbixResult> results15 = zabbixHistoryService.acqResults(zabbixHistoryService.queryPerCpuAvg15(serverDO, ZABBIX_HISTORY_LIMIT));
            String load15Max = calMaxValueByFloat(results15);

            if (StringUtils.isEmpty(load15Max)) {
                serverPerfVO.setLoad15("-1");
            } else {
                BigDecimal bigDecimal15 = new BigDecimal(Float.valueOf(load15Max));
                serverPerfVO.setLoad15(bigDecimal15.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

        } catch (Exception e) {
            logger.error("计算load错误", e);
        }

        // memoryRate
        try {
            //String memoryAvailable = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryMemoryAvailable(serverDO, 1));
            List<ZabbixResult> results = zabbixHistoryService.acqResults(zabbixHistoryService.queryMemoryAvailable(serverDO, ZABBIX_HISTORY_LIMIT));
            String memoryAvailableMin = calMinValueByLong(results);
            String memoryTotal = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryMemoryTotal(serverDO, 1));
            long memoryRate = 100 - Long.parseLong(memoryAvailableMin) * 100 / Long.parseLong(memoryTotal);
            serverPerfVO.setMemoryRate(String.valueOf(memoryRate));
        } catch (Exception e) {
            logger.error("计算内存使用率错误", e);
        }

        // diskRate
        invokeDiskRate(serverDO, serverPerfVO);

        String diskSysVolume = getDiskSysVolume(serverDO);
        if (diskSysVolume != null) {
            try {
                long vaReadBps = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskReadBps(serverDO, diskSysVolume, 1)));
                long vaWriteBps = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskWriteBps(serverDO, diskSysVolume, 1)));
                serverPerfVO.setDiskSysBps(String.valueOf(vaReadBps + vaWriteBps));

            } catch (Exception e) {
                logger.error("计算系统盘Bps错误", e);
            }

            try {
                long vaReadIops = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskReadIops(serverDO, diskSysVolume, 1)));
                long vaWriteIops = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskWriteIops(serverDO, diskSysVolume, 1)));
                serverPerfVO.setDiskSysIops(String.valueOf(vaReadIops + vaWriteIops));
            } catch (Exception e) {
                logger.error("计算系统盘IOPS错误", e);
            }

            try {
                long vaReadMs = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskReadMs(serverDO, diskSysVolume, 1)));
                long vaWriteMs = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskWriteMs(serverDO, diskSysVolume, 1)));
                serverPerfVO.setDiskSysReadMs(String.valueOf(vaReadMs));
                serverPerfVO.setDiskSysWriteMs(String.valueOf(vaWriteMs));
            } catch (Exception e) {
                logger.error("计算系统盘读写延迟错误", e);
            }
        }

        String diskDataVolume = getDiskDataVolume(serverDO);
        if (diskDataVolume != null) {
            try {
                long vbReadBps = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskReadBps(serverDO, diskDataVolume, 1)));
                long vbWriteBps = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskWriteBps(serverDO, diskDataVolume, 1)));
                serverPerfVO.setDiskDataBps(String.valueOf(vbReadBps + vbWriteBps));

            } catch (Exception e) {
                logger.error("计算数据盘Bps错误", e);
            }

            try {
                long vbReadIops = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskReadIops(serverDO, diskDataVolume, 1)));
                long vbWriteIops = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskWriteIops(serverDO, diskDataVolume, 1)));
                serverPerfVO.setDiskDataIops(String.valueOf(vbReadIops + vbWriteIops));

            } catch (Exception e) {
                logger.error("计算数据盘IOPS错误", e);
            }

            try {
                long vbReadMs = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskReadMs(serverDO, diskDataVolume, 1)));
                long vbWriteMs = Long.valueOf(zabbixHistoryService.acqResultValue(zabbixHistoryService.queryDiskWriteMs(serverDO, diskDataVolume, 1)));
                serverPerfVO.setDiskDataReadMs(String.valueOf(vbReadMs));
                serverPerfVO.setDiskDataWriteMs(String.valueOf(vbWriteMs));
            } catch (Exception e) {
                logger.error("计算数据盘读写延迟错误", e);
            }

        }
    }

    private void invokeDiskRate(ServerDO serverDO, ServerPerfVO serverPerfVO) {
        // diskRate
        try {
            float usedDiskOnSys = acqDiskSysRate(serverDO);
            float usedDiskOnData = acqDiskDataRate(serverDO);
            serverPerfVO.setDiskSysRate(String.valueOf(usedDiskOnSys));
            serverPerfVO.setDiskDataRate(String.valueOf(usedDiskOnData));
            if (usedDiskOnSys > usedDiskOnData) {
                serverPerfVO.setDiskRate(String.valueOf(usedDiskOnSys));
            } else {
                serverPerfVO.setDiskRate(String.valueOf(usedDiskOnData));
            }
        } catch (Exception e) {
            logger.error("计算磁盘使用率错误", e);
        }
    }

    // 计算磁盘使用率
    @Override
    public float acqDiskRate(ServerDO serverDO) {
        // diskRate
        try {
            float usedDiskOnSys = acqDiskSysRate(serverDO);
            float usedDiskOnData = acqDiskDataRate(serverDO);
            if (usedDiskOnSys > usedDiskOnData) {
                return usedDiskOnSys;
            } else {
                return usedDiskOnData;
            }
        } catch (Exception e) {
            logger.error("计算磁盘使用率错误", e);
            return -1;
        }
    }


    private float acqDiskSysRate(ServerDO serverDO) {
        // diskRate
        try {
            String freeDiskOnSys = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryFreeDiskSpaceOnSys(serverDO, 1));
            float usedDiskOnSys = -1;
            if (freeDiskOnSys != null) {
                usedDiskOnSys = 100 - Float.parseFloat(freeDiskOnSys);
                BigDecimal b = new BigDecimal(usedDiskOnSys);
                usedDiskOnSys = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            }
            return usedDiskOnSys;
        } catch (Exception e) {
            logger.error("计算磁盘使用率错误", e);
            return -1;
        }
    }

    private float acqDiskDataRate(ServerDO serverDO) {
        // diskRate
        try {
            String freeDiskOnData = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryFreeDiskSpaceOnData(serverDO, 1));
            float usedDiskOnData = -1;
            if (freeDiskOnData != null) {
                usedDiskOnData = 100 - Float.parseFloat(freeDiskOnData);
                BigDecimal b2 = new BigDecimal(usedDiskOnData);
                usedDiskOnData = b2.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            }
            return usedDiskOnData;
        } catch (Exception e) {
            logger.error("计算磁盘使用率错误", e);
            return -1;
        }
    }


    /**
     * 获取系统盘vol
     *
     * @param serverDO
     * @return
     */
    private String getDiskSysVolume(ServerDO serverDO) {
        ConfigPropertyDO confPropertyDo = configDao.getConfigPropertyByName(zabbix_disk_sys_volume);
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerPropertyData(serverDO.getId(), confPropertyDo.getId());
        if (serverGroupPropertiesDO != null && !serverGroupPropertiesDO.getPropertyValue().isEmpty()) {
            return serverGroupPropertiesDO.getPropertyValue();
        }
        for (String vol : diskSysVolume) {
            try {
                JSONObject response = zabbixHistoryService.queryDiskReadBps(serverDO, vol, 1);
                JSONArray result = response.getJSONArray("result");
                if (result == null) return null;
                result.getJSONObject(0).getString("value");
                addServerGroupProperties(serverDO, confPropertyDo, vol);
                return vol;
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    /**
     * 获取数据盘vol
     *
     * @param serverDO
     * @return
     */
    private String getDiskDataVolume(ServerDO serverDO) {
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName(zabbix_disk_data_volume);
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerPropertyData(serverDO.getId(), confPropertyDO.getId());
        if (serverGroupPropertiesDO != null && !serverGroupPropertiesDO.getPropertyValue().isEmpty()) {
            return serverGroupPropertiesDO.getPropertyValue();
        }
        for (String vol : diskDataVolume) {
            try {
                JSONObject response = zabbixHistoryService.queryDiskReadBps(serverDO, vol, 1);
                JSONArray result = response.getJSONArray("result");
                if (result == null) return null;
                result.getJSONObject(0).getString("value");
                addServerGroupProperties(serverDO, confPropertyDO, vol);
                return vol;
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    /**
     * 增加服务器磁盘卷标属性
     *
     * @param serverDO
     * @param confPropertyDo
     * @param vol
     */
    private void addServerGroupProperties(ServerDO serverDO, ConfigPropertyDO confPropertyDo, String vol) {
        ServerGroupPropertiesDO serverGroupPropertiesDO = new ServerGroupPropertiesDO();
        serverGroupPropertiesDO.setServerId(serverDO.getId());
        serverGroupPropertiesDO.setPropertyId(confPropertyDo.getId());
        serverGroupPropertiesDO.setPropertyValue(vol);
        serverGroupPropertiesDO.setPropertyGroupId(configDao.getConfigPropertyGroupByName("Zabbix").getId());
        configDao.addServerPropertyData(serverGroupPropertiesDO);
    }


    @Override
    public void cache() {
        List<ServerDO> servers = serverDao.getAllServer();
        for (ServerDO serverDO : servers) {
            ServerPerfVO serverPerfVO = cache(serverDO);
            LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(serverDO.getId());
            if (logCleanupPropertyDO != null) {
                logCleanupPropertyDO.setDiskRate(Float.valueOf(serverPerfVO.getDiskRate()).intValue());
                logCleanupDao.updateLogCleanupProperty(logCleanupPropertyDO);
            }
        }
    }

    @Override
    public ServerPerfVO cache(ServerDO serverDO) {
        ServerPerfVO serverPerfVO = cacheZabbixService.queryServerPerfVO(serverDO.getId());
        if (serverPerfVO == null) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());
            serverPerfVO = new ServerPerfVO(serverDO, serverGroupDO);
        }
        invokePerf(serverPerfVO, serverDO);
        cache(serverPerfVO);
        return serverPerfVO;
    }

    @Override
    public ServerPerfVO getCache(ServerDO serverDO) {
        ServerPerfVO serverPerfVO = cacheZabbixService.queryServerPerfVO(serverDO.getId());
        if (serverPerfVO != null) return serverPerfVO;
        return cache(serverDO);
    }


    private void cache(ServerPerfVO serverPerfVO) {
        cacheZabbixService.insert(serverPerfVO);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_status, "false");
    }
}
