package com.sdg.cmdb.zabbix;

import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.ZabbixResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2017/2/15.
 */
public interface ZabbixHistoryService {

    /**
     * 查询服务器组的所有服务器当前监控基本信息
     *
     * @param serverGroupDO
     * @return
     */
    List<Map<String, Object>> queryHistory(ServerGroupDO serverGroupDO);


    JSONObject queryCpuUser(ServerDO serverDO, int limit);

    JSONObject queryCpuIowait(ServerDO serverDO, int limit);

    List<ZabbixResult> acqResults(JSONObject response);


    String acqResultValue(JSONObject response);



    JSONObject queryPerCpuAvg1(ServerDO serverDO, int limit);

    JSONObject queryPerCpuAvg5(ServerDO serverDO, int limit);

    JSONObject queryPerCpuAvg15(ServerDO serverDO, int limit);

    JSONObject queryMemoryTotal(ServerDO serverDO, int limit);

    JSONObject queryMemoryAvailable(ServerDO serverDO, int limit);

    /**
     * 获取tomcat版本
     * @param serverDO
     * @param limit
     * @return
     */
    JSONObject queryTomcatVersion(ServerDO serverDO, int limit);
    /**
     * 百分比
     *
     * @param serverDO
     * @param limit
     * @return
     */
    JSONObject queryFreeDiskSpaceOnSys(ServerDO serverDO, int limit);

    /**
     * 百分比
     *
     * @param serverDO
     * @param limit
     * @return
     */
    JSONObject queryFreeDiskSpaceOnData(ServerDO serverDO, int limit);


    //JSONObject queryTotalDiskSpaceOnSys(ServerDO serverDO, int limit);

    //JSONObject queryUsedDiskSpaceOnSys(ServerDO serverDO, int limit);

    //JSONObject queryTotalDiskSpaceOnData(ServerDO serverDO, int limit);

    //JSONObject queryUsedDiskSpaceOnData(ServerDO serverDO, int limit);

    /**
     * 查询磁盘读Bps
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    JSONObject queryDiskReadBps(ServerDO serverDO, String volume, int limit);

    /**
     * 查询磁盘写Bps
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    JSONObject queryDiskWriteBps(ServerDO serverDO, String volume, int limit);

    /**
     * 查询磁盘读iops
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    JSONObject queryDiskReadIops(ServerDO serverDO, String volume, int limit);

    /**
     * 查询磁盘写iops
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    JSONObject queryDiskWriteIops(ServerDO serverDO, String volume, int limit);


    /**
     * 查询磁盘读延迟ms
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    JSONObject queryDiskReadMs(ServerDO serverDO, String volume, int limit);

    /**
     * 查询磁盘写延迟ms
     * @param serverDO
     * @param volume
     * @param limit
     * @return
     */
    JSONObject queryDiskWriteMs(ServerDO serverDO, String volume, int limit);

    /**
     * 获取服务器组的监控数据(图标展示)
     * @param serverGroupDO
     * @return
     */
    HashMap<Long, HashMap<Integer, String>> queryZabbixMoniter(ServerGroupDO serverGroupDO);

}
