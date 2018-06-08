package com.sdg.cmdb.util;

/**
 * Created by liangjian on 2017/2/23.
 */

import com.sdg.cmdb.domain.server.EcsServerDO;
import com.sdg.cmdb.domain.server.ServerCostVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.VmServerDO;

/**
 * 服务器成本计算
 */
public class ServerCostUtils {


    public  static int vmCost(ServerCostVO serverCostVO) {
        // 服务器按3年折旧，1GB虚拟机使用成本约为11
        // 每核心cpu按0.5元计算
        int cost = 0;
        cost = serverCostVO.getCpu() / 2 + serverCostVO.getMemory() / 1024 * 11;
        return cost;
    }

    public static int ecsCost(ServerCostVO serverCostVO) {
        String ecsType = serverCostVO.getCpu() + "/" + serverCostVO.getMemory() / 1024;
        // cpu / memory(GB) 此费用在阿里云查询得到
        switch (ecsType) {
            case "1/1":
                return 105;
            case "1/2":
                return 129;
            case "2/4":
                return 237;
            case "4/8":
                return 437;
            case "8/16":
                return 825;
            case "16/32":
                return 1601;
            case "32/64":
                return 3153;
            default:
                return 0;
        }
    }


}
