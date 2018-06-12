package com.sdg.cmdb.service.impl;

/**
 * Created by liangjian on 2016/12/21.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.ZabbixHistoryItemEnum;
import com.sdg.cmdb.domain.zabbix.ZabbixResult;
import com.sdg.cmdb.service.ConfigCenterService;

import com.sdg.cmdb.service.ZabbixHistoryService;
import com.sdg.cmdb.service.ZabbixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询监控项
 */
@Service
public class ZabbixHistoryServiceImpl implements ZabbixHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(ZabbixHistoryServiceImpl.class);


    @Resource
    private ZabbixService zabbixService;

    public final int zabbix_result_array = 0;

    public final int zabbix_result_object = 1;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ConfigCenterService configCenterService;

    static Gson gson = new Gson();

    //浮点数
    public static final int historyTypeNumericFloat = 0;
    //字符
    public static final int historyTypeCharacter = 1;
    //日志
    public static final int historyTypeLog = 2;
    //数字无符号
    public static final int historyTypeNumericUnsigned = 3;
    //文本
    public static final int historyTypeText = 4;

    @Override
    public List<ZabbixResult> acqResults(JSONObject response) {
        if (response == null) return null;
        try {
            JSONArray result = response.getJSONArray("result");
            if (result == null) return null;
            //System.err.println(result);
            //ZabbixResult zabbixRsult = gson.fromJson(result.toString(), ZabbixResult.class);
            List<ZabbixResult> results = gson.fromJson(result.toString(), new TypeToken<List<ZabbixResult>>() {
            }.getType());
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("zabbix result错误:" + response.toJSONString());
        }
        return null;
    }


    @Override
    public String acqResultValue(JSONObject response) {
        if (response == null) return null;
        try {
            JSONArray result = response.getJSONArray("result");
            if (result == null) return null;
            return result.getJSONObject(0).getString("value");
        } catch (Exception e) {
            logger.error("zabbix 返回值错误!");
        }
        return null;
    }

    /**
     * 查询最新的数据
     *
     * @param itemName
     * @param itemKey
     * @param historyType
     * @param limit       数量
     * @return
     */
    private JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit) {
        return zabbixService.historyGet(serverDO, itemName, itemKey, historyType, limit);
    }

    /**
     * 查询时间范围内
     *
     * @param itemName
     * @param itemKey
     * @param historyType
     * @param timestampFrom 开始时间
     * @param timestampTill 结束时间
     * @return
     */
    private JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill) {
        return zabbixService.historyGet(serverDO, itemName, itemKey, historyType, timestampFrom, timestampTill);
    }

    /**
     * cpu使用率
     *
     * @param serverDO
     * @param limit
     * @return
     */
    @Override
    public JSONObject queryCpuUser(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.CPU_USER.getItemKey(), historyTypeNumericFloat, limit);
    }

    @Override
    public JSONObject queryCpuIowait(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.CPU_IO_WAIT.getItemKey(), historyTypeNumericFloat, limit);
    }


    /**
     * cpu使用率
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryCpuUser(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.CPU_USER.getItemKey(), historyTypeNumericFloat, timestampFrom, timestampTill);
    }

    /**
     * 平均每分钟load
     *
     * @param serverDO
     * @param limit
     * @return
     */
    @Override
    public JSONObject queryPerCpuAvg1(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.PER_CPU_AVG1.getItemKey(), historyTypeNumericFloat, limit);
    }

    @Override
    public JSONObject queryPerCpuAvg5(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.PER_CPU_AVG5.getItemKey(), historyTypeNumericFloat, limit);
    }

    @Override
    public JSONObject queryPerCpuAvg15(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.PER_CPU_AVG15.getItemKey(), historyTypeNumericFloat, limit);
    }

    /**
     * 平均每分钟load
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryPerCpuAvg(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.PER_CPU_AVG1.getItemKey(), historyTypeNumericFloat, timestampFrom, timestampTill);
    }

    /**
     * 服务器总内存容量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    @Override
    public JSONObject queryMemoryTotal(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.MEM_SIZE_TOTAL.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 服务器总内存容量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryMemoryTotal(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.MEM_SIZE_TOTAL.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 服务器可用内存容量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    @Override
    public JSONObject queryMemoryAvailable(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.MEM_SIZE_AVAILABLE.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    @Override
    public JSONObject queryTomcatVersion(ServerDO serverDO, int limit){
      // return historyGet(serverDO, null, ZabbixHistoryItemEnum.TOMCAT_VERSION.getItemKey(), historyTypeText, limit);
       // Tomcat version
        return historyGet(serverDO, "Tomcat version", ZabbixHistoryItemEnum.TOMCAT_VERSION.getItemKey(), historyTypeCharacter, limit);
    }

    /**
     * 服务器可用内存容量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryMemoryAvailable(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.MEM_SIZE_AVAILABLE.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * @param serverDO
     * @param limit
     * @return
     */
    @Override
    public JSONObject queryFreeDiskSpaceOnSys(ServerDO serverDO, int limit) {
        return historyGet(serverDO, "Free disk space on / (percentage)", ZabbixHistoryItemEnum.VFS_FS_SIZE_PFREE.getItemKey(), historyTypeNumericFloat, limit);
    }


    private JSONObject queryFreeDiskSpaceOnSys(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_PFREE.getItemKey(), historyTypeNumericFloat, timestampFrom, timestampTill);
    }

    @Override
    public JSONObject queryFreeDiskSpaceOnData(ServerDO serverDO, int limit) {
        //"vfs.fs.size[/data,pfree]"
        return historyGet(serverDO, "Free disk space on $1 (percentage)", ZabbixHistoryItemEnum.VFS_FS_SIZE_DATA_PFREE.getItemKey(), historyTypeNumericFloat, limit);
    }


    /**
     * 系统盘总容量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    private JSONObject queryTotalDiskSpaceOnSys(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_TOTAL.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 系统盘总容量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryTotalDiskSpaceOnSys(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_TOTAL.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 系统盘使用容量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    private JSONObject queryUsedDiskSpaceOnSys(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_USED.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 系统盘使用容量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryUsedDiskSpaceOnSys(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_USED.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 数据盘总容量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    public JSONObject queryTotalDiskSpaceOnData(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_DATA_TOTAL.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 数据盘总容量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryTotalDiskSpaceOnData(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_DATA_TOTAL.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 数据盘使用容量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    public JSONObject queryUsedDiskSpaceOnData(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_DATA_USED.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 数据盘使用容量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryUsedDiskSpaceOnData(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.VFS_FS_SIZE_DATA_USED.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 磁盘read bps
     *
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    @Override
    public JSONObject queryDiskReadBps(ServerDO serverDO, String volume, int limit) {
        return historyGet(serverDO, null, "custom.vfs.dev.read.sectors[" + volume + "]", historyTypeNumericUnsigned, limit);
    }

    /**
     * 磁盘read bps
     *
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    @Override
    public JSONObject queryDiskWriteBps(ServerDO serverDO, String volume, int limit) {
        return historyGet(serverDO, null, "custom.vfs.dev.write.sectors[" + volume + "]", historyTypeNumericUnsigned, limit);
    }


    @Override
    public JSONObject queryDiskReadIops(ServerDO serverDO, String volume, int limit) {
        return historyGet(serverDO, null, "custom.vfs.dev.read.ops[" + volume + "]", historyTypeNumericUnsigned, limit);
    }

    @Override
    public JSONObject queryDiskWriteIops(ServerDO serverDO, String volume, int limit) {
        return historyGet(serverDO, null, "custom.vfs.dev.write.ops[" + volume + "]", historyTypeNumericUnsigned, limit);
    }


    @Override
    public JSONObject queryDiskReadMs(ServerDO serverDO, String volume, int limit) {
        //custom.vfs.dev.read.ms[xvda]
        return historyGet(serverDO, null, "custom.vfs.dev.read.ms[" + volume + "]", historyTypeNumericUnsigned, limit);
    }

    @Override
    public JSONObject queryDiskWriteMs(ServerDO serverDO, String volume, int limit) {
        //custom.vfs.dev.write.ms[xvda]
        return historyGet(serverDO, null, "custom.vfs.dev.write.ms[" + volume + "]", historyTypeNumericUnsigned, limit);
    }


    /**
     * 网卡0（内网网卡） 下行流量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    private JSONObject queryIncomingNetworkTrafficOnEth0(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_IN_ETH0.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 网卡0（内网网卡） 下行流量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryIncomingNetworkTrafficOnEth0(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_IN_ETH0.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 网卡0（内网网卡） 上行流量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    private JSONObject queryOutgoingNetworkTrafficOnEth0(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_OUT_ETH0.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 网卡0（内网网卡） 上行流量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryOutgoingNetworkTrafficOnEth0(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_OUT_ETH0.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 网卡1（公网网卡） 下行流量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    private JSONObject queryIncomingNetworkTrafficOnEth1(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_IN_ETH1.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 网卡1（公网网卡） 下行流量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryIncomingNetworkTrafficOnEth1(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_IN_ETH1.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    /**
     * 网卡1（公网网卡） 上行流量
     *
     * @param serverDO
     * @param limit
     * @return
     */
    private JSONObject queryOutgoingNetworkTrafficOnEth1(ServerDO serverDO, int limit) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_OUT_ETH1.getItemKey(), historyTypeNumericUnsigned, limit);
    }

    /**
     * 网卡1（公网网卡） 上行流量
     *
     * @param serverDO
     * @param timestampFrom
     * @param timestampTill
     * @return
     */
    private JSONObject queryOutgoingNetworkTrafficOnEth1(ServerDO serverDO, String timestampFrom, String timestampTill) {
        return historyGet(serverDO, null, ZabbixHistoryItemEnum.NET_IF_OUT_ETH1.getItemKey(), historyTypeNumericUnsigned, timestampFrom, timestampTill);
    }

    @Override
    public List<Map<String, Object>> queryHistory(ServerGroupDO serverGroupDO) {
        List result = new ArrayList<>();
        List<ServerDO> listServerDO = serverDao.acqServersByGroupId(serverGroupDO.getId());
        if (listServerDO == null || listServerDO.size() == 0) {
            return result;
        }
        for (ServerDO serverDO : listServerDO) {
            // 主机未监控
            if (!zabbixService.hostExists(serverDO)) continue;
            // 主机监控被禁用
            if (zabbixService.hostGetStatus(serverDO) == ZabbixServiceImpl.hostStatusDisable) continue;

            Map<String, Object> server = new HashMap<>();
            server.put("serverId", serverDO.getId());
            server.put("serverName", serverDO.getServerName());
            server.put("evnType", serverDO.getEnvType());
            server.put("serialNumber", serverDO.getSerialNumber());

            Map<String, Object> history = new HashMap<>();
            Float cpuUser = Float.valueOf(acqResultValue(queryCpuUser(serverDO, 1)));
            BigDecimal bigDecimalCpuUser = new BigDecimal(cpuUser);
            cpuUser = bigDecimalCpuUser.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
            history.put("cpuUser", cpuUser);

            Float cpuLoad = Float.valueOf(acqResultValue(queryPerCpuAvg1(serverDO, 1)));
            BigDecimal bigDecimalCpuLoad = new BigDecimal(cpuLoad);
            cpuLoad = bigDecimalCpuLoad.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
            history.put("cpuLoad", cpuLoad);

            Long longMemoryAvailable = Long.valueOf(acqResultValue(queryMemoryAvailable(serverDO, 1)));
            //转换成 GB
            Float memoryAvailable = (float) longMemoryAvailable / 1073741824;
            BigDecimal bigDecimalMemoryAvailable = new BigDecimal(memoryAvailable);
            memoryAvailable = bigDecimalMemoryAvailable.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            history.put("memoryAvailable", memoryAvailable);

            Long longMemoryTotal = Long.valueOf(acqResultValue(queryMemoryTotal(serverDO, 1)));
            //转换成 GB
            Float memoryTotal = (float) longMemoryTotal / 1073741824;
            BigDecimal bigDecimalMemoryTotal = new BigDecimal(memoryTotal);
            memoryTotal = bigDecimalMemoryTotal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            history.put("memoryTotal", memoryTotal);

            server.put("history", history);
            result.add(server);
        }
        return result;
    }

    @Override
    public HashMap<Long, HashMap<Integer, String>> queryZabbixMoniter(ServerGroupDO serverGroupDO) {
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        HashMap<Long, HashMap<Integer, String>> serverGroupMap = new HashMap<>();
        for (ServerDO serverDO : list) {
            HashMap<Integer, String> serverMap = new HashMap<>();
            /**
             *    key 0 : cpuUser
             *    key 2 : load
             */
            try {
                String cpu = acqResultValue(queryCpuUser(serverDO, 1));
                BigDecimal bigDecimal = new BigDecimal(Float.valueOf(cpu));
                serverMap.put(0, bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
            } catch (Exception e) {
                logger.error("计算cpu使用率错误", e);
                serverMap.put(0, "-1");
            }

            // memoryRate
            try {
                String memoryAvailable = acqResultValue(queryMemoryAvailable(serverDO, 1));
                String memoryTotal = acqResultValue(queryMemoryTotal(serverDO, 1));
                long memoryRate = Long.parseLong(memoryAvailable) * 100 / Long.parseLong(memoryTotal);
                serverMap.put(1, String.valueOf(memoryRate));
            } catch (Exception e) {
                logger.error("计算内存使用率错误", e);
                serverMap.put(1, "-1");
            }

            // load
            try {
                String load = acqResultValue(queryPerCpuAvg1(serverDO, 1));
                if (load.isEmpty()) load = "-1";
                serverMap.put(2, load);
            } catch (Exception e) {
                logger.error("计算load错误", e);
                serverMap.put(2, "-1");
            }
            serverGroupMap.put(serverDO.getId(), serverMap);
        }
        return serverGroupMap;
    }

}
