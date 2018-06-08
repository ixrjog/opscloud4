package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.esxi.EsxiHostDO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.CacheEsxiHostService;
import com.sdg.cmdb.service.IPService;
import com.sdg.cmdb.service.PhysicalServerService;

import com.sdg.cmdb.service.VmService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/2/13.
 */
@Service
public class PhysicalServerServiceImpl implements PhysicalServerService {

    @Resource
    private ServerDao serverDao;

    @Resource
    private IPService ipService;

    @Resource
    private VmService vmService;

    @Resource
    private CacheEsxiHostService cacheEsxiHostService;

    @Override
    public TableVO<List<PhysicalServerVO>> getPhysicalServerPage(String serverName, int useType, int page, int length) {
        long size = serverDao.getPhysicalServerSize(serverName, useType);
        List<PhysicalServerDO> list = serverDao.getPhysicalServerPage(serverName, useType, page * length, length);
        List<PhysicalServerVO> voList = new ArrayList<>();
        for (PhysicalServerDO physicalServerDO : list) {
            // physicalServerVO.   getAllServerIP
            PhysicalServerVO physicalServerVO = new PhysicalServerVO(physicalServerDO);
            physicalServerVO.setIpDetailList(ipService.getAllServerIP(physicalServerDO.getServerId()));
            if (physicalServerDO.getUseType() == PhysicalServerDO.UseTypeEnum.vm.getCode()) {
                EsxiHostDO esxiHostDO = cacheEsxiHostService.queryEsxiHost(physicalServerDO.getServerName());
                if (esxiHostDO == null) esxiHostDO = new EsxiHostDO();
                vmService.hostSystemMemeoryConfig(physicalServerDO, esxiHostDO);
                if (esxiHostDO.getNumcpu() != 0) {
                    esxiHostDO.setMemeryTotal((int) esxiHostDO.getMemeryTotal() / 1024);
                    try{
                        esxiHostDO.setOverallMemoryUsage((int) esxiHostDO.getOverallMemoryUsage() / 1024);
                    }catch (Exception e){
                        //e.printStackTrace();
                        esxiHostDO.setOverallMemoryUsage(0);
                    }

                    //esxiHostDO.setVmServers(vmService.getVirtualMachines(physicalServerDO.getServerName()));
                    physicalServerVO.setEsxiHostDO(esxiHostDO);
                    //vmService.invokeEsxiHostDatastores(esxiHostDO);
                }
            }
            voList.add(physicalServerVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public ServerStatisticsDO statistics() {
        ServerStatisticsDO serverStatisticsDO = serverDao.queryPsStatistics();
        serverStatisticsDO.setServerType(ServerStatisticsDO.ServerTypeEnum.physicalServer.getCode());
        return serverStatisticsDO;
    }


}
