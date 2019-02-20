package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.esxi.EsxiHostDO;
import com.sdg.cmdb.domain.esxi.HostDatastoreInfoVO;
import com.sdg.cmdb.domain.server.*;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.mo.ServiceInstance;

import java.util.List;

/**
 * Created by liangjian on 2016/12/27.
 */

public interface VmService {

    BusinessWrapper<Boolean> vmModifyName(VmServerDO vmServer) ;

    BusinessWrapper<Boolean> vmRefresh() ;

    BusinessWrapper<Boolean> rename() ;

    BusinessWrapper<Boolean> vmCheck() ;

    BusinessWrapper<Boolean> delVm(String insideIp) ;

    /**
     * 获取Vm服务器分页数据
     * @param serverName
     * @param queryIp
     * @param status
     * @param page
     * @param length
     * @return
     */
    TableVO<List<VmServerDO>> getVmServerPage(String serverName,String queryIp, int status, int page, int length);

    /**
     * 统计
     * @return
     */
    ServerStatisticsDO statistics();

    /**
     * 关机
     * @return
     */
    BusinessWrapper<Boolean> powerOff(VmServerDO vmServerDO);

    BusinessWrapper<Boolean> powerOn(VmServerDO vmServerDO);

    /**
     * 获取EsxiHost详细信息
     * @param physicalServerDO
     * @return
     */
    void hostSystemMemeoryConfig(PhysicalServerDO physicalServerDO,EsxiHostDO esxiHostDO);
    /**
     * 更新vmServer
     * @param vmServerDO
     * @return
     */
    boolean updateVmServerForServer(VmServerDO vmServerDO);

    /**
     * 更新vmServer
     * @param serverDO
     * @return
     */
    boolean updateVmServerForServer(ServerDO serverDO);


    /**
     * 获取VM模版机分页数据
     * @param page
     * @param length
     * @return
     */
    TableVO<List<VmTemplateDO>> getVmTemplatePage(int page, int length);

    /**
     * 获取esxi数据存储信息
     * @param serverName
     * @return
     */
    List<HostDatastoreInfoVO> getEsxiHostDatastores(String serverName);

    List<VmServerDO> getVirtualMachines(String hostName);

    TableVO<List<VmServerDO>> getEsxiVmsPage(String serverName);

    /**
     * esxi服务器数据存储详情
     * @param serverName
     * @return
     */
    TableVO<List<HostDatastoreInfoVO>> getEsxiDatastoresPage(String serverName);

    /**
     * 用于cache
     * @param serverName
     * @return
     */
    EsxiHostDO getEsxiHost(String serverName);

    /**
     * 用于cache
     * @return
     */
    List<EsxiHostDO> getEsxiHost();

    AboutInfo getVersion();

}
