package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.ServerCostService;
import com.sdg.cmdb.util.ServerCostUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by liangjian on 2017/2/24.
 */
@Service
public class ServerCostServiceImpl implements ServerCostService {

    @Resource
    private ServerDao serverDao;


    @Resource
    private ServerGroupDao serverGroupDao;

    @Override
    public List<ServerCostVO> getServerPage(int year, int month) {
        List<ServerCostVO> listServerCostVO = queryServerCost(year, month);
        return  listServerCostVO;
    }

    private List<ServerCostVO> queryServerCost(int year, int month) {
        if (year == -1) {
            year = acqYear();
            month = acqMonth();
        }
        String startDate = year + "-" + month + "-1";
        String endDate;
        if (month >= 12) {
            endDate = (year + 1) + "-1-1";
        } else {
            endDate = year + "-" + (month + 1) + "-1";
        }
        List<ServerCostVO> listServerCostVO = new ArrayList<ServerCostVO>();
        List<VmServerDO> listVm = serverDao.getVmServerCost(startDate, endDate);
        for (VmServerDO vm : listVm) {
            ServerCostVO cost = new ServerCostVO(vm);
            cost.setCost(ServerCostUtils.vmCost(cost));
            listServerCostVO.add(cost);
            invokeContent(cost);
        }
        List<EcsServerDO> listEcs = serverDao.getEcsServerCost(startDate, endDate);
        for (EcsServerDO ecs : listEcs) {
            ServerCostVO cost = new ServerCostVO(ecs);
            cost.setCost(ServerCostUtils.ecsCost(cost));
            listServerCostVO.add(cost);
            invokeContent(cost);
        }
        return listServerCostVO;
    }


    @Override
    public ServerStatisticsDO statistics(int year, int month) {
        List<ServerCostVO> listServerCostVO = queryServerCost(year, month);
        ServerStatisticsDO serverStatisticsDO = new ServerStatisticsDO();
        int memory = 0;
        int cpu = 0;
        int cost = 0;
        for (ServerCostVO serverCostVO : listServerCostVO) {
            memory += serverCostVO.getMemory();
            cpu += serverCostVO.getCpu();
            cost += serverCostVO.getCost();
        }
        serverStatisticsDO.setCnt(listServerCostVO.size());
        serverStatisticsDO.setCpuCnt(cpu);
        serverStatisticsDO.setMemoryCnt(memory);
        serverStatisticsDO.setCost(cost);
        return serverStatisticsDO;
    }


    private void invokeContent(ServerCostVO serverCostVO) {
        if (serverCostVO.getContent() != null && !serverCostVO.getContent().isEmpty()) return;
        if (serverCostVO.getServerId() == 0) return;
        ServerDO serverDO = serverDao.getServerInfoById(serverCostVO.getServerId());
        if (serverDO == null) return;
        if (serverDO.getContent() !=null && ! serverDO.getContent().isEmpty()){
            serverCostVO.setContent(serverDO.getContent());
            return ;
        }
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        serverCostVO.setContent(serverGroupDO.getContent());
    }


    private int acqYear() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.YEAR);
    }

    private int acqMonth() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.MONTH) + 1;
    }


}
