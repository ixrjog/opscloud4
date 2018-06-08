package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.esxi.EsxiHostDO;
import com.sdg.cmdb.domain.esxi.HostDatastoreInfoVO;
import com.sdg.cmdb.domain.server.PhysicalServerDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.VmServerDO;
import com.sdg.cmdb.service.impl.VmServiceImpl;
import com.sdg.cmdb.util.TimeUtils;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.HostSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liangjian on 2016/12/28.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class VmServiceTest {

    @Resource
    private VmService vmService;

    @Resource
    private VmServiceImpl vmServiceImpl;

    @Resource
    private ServerDao serverDao;

    @Test
    public void testStatistics() {
        System.err.println(vmService.statistics());
    }

    @Test
    public void testPower() {
        // String vmName = vmServerDO.getInsideIp() + ":" + vmServerDO.getServerName();
        //ServerDO serverDO =serverDao.getServerInfoById(353);
        VmServerDO vm=new VmServerDO();
        vm.setServerName("windows2008R2.vm.template");
        vm.setInsideIp("10.17.1.28");

        // search-mongo-test1
        //vm.setServerName("search-mongo-test1");
        //vm.setInsideIp("10.17.1.181");
        //VmServerDO vm=new VmServerDO(serverDO,0);
        //System.err.println(vmService.powerOff(vm));
       // System.err.println(vmService.powerOff(vm));
        System.err.println(vmService.powerOn(vm));
    }

    @Test
    public void testHostSystemMemeoryConfig() {
        PhysicalServerDO physicalServerDO = new PhysicalServerDO();
        physicalServerDO.setServerName("esxi-01.org");
        EsxiHostDO esxiHostDO = new EsxiHostDO();
        esxiHostDO.setVmName(physicalServerDO.getServerName());
        vmServiceImpl.hostSystemMemeoryConfig(physicalServerDO, esxiHostDO);
        System.err.println(esxiHostDO);
        System.err.println("managementServerIp:" + esxiHostDO.getSummary().getManagementServerIp());

    }

    @Test
    public void testEsxiHostSystem() {
        try {
            //Folder rootFolder = serviceInstance.getRootFolder();
            HostSystem hostSystem = vmServiceImpl.getEsxiHostSystem("esxi-01.org");
            if (hostSystem == null) return;
            //指定服务器：HostSystem 关联的Datastore
            Datastore[] datastores = hostSystem.getDatastores();
            List<HostDatastoreInfoVO> list = new ArrayList<HostDatastoreInfoVO>();
            //HostSystemDataStoreFound hostSystemDataStoreFound = new HostSystemDataStoreFound();
            for (int i = 0; i < datastores.length; i++) {
                Datastore datastore = datastores[i];
                HostDatastoreInfoVO hostDatastoreInfo = vmServiceImpl.setDatastore(datastore, false);
                list.add(hostDatastoreInfo);
            }
            //datastore 数量
            System.out.println("size is:" + list.size());
            for (int j = 0; j < list.size(); j++) {
                HostDatastoreInfoVO datastoreInfo = list.get(j);
                System.out.println("------------start-----------------------");
                System.out.println("name:" + datastoreInfo.getName());
                System.out.println("freeSpace:" + datastoreInfo.getFreeSpace());
                System.out.println("capacity:" + datastoreInfo.getCapacity());
                System.out.println("maxFileSize:" + datastoreInfo.getMaxFileSize());
                System.out.println("------------end-----------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEsxiHostSystem2() {

        List<VmServerDO> vms = vmService.getVirtualMachines("esxi-05.sdg.org");
       // for (VmServerDO vmServerDO : vms)
        //    System.err.println(vmServerDO);
    }


    @Test
    public void testGetEsxiHost() {
        List<EsxiHostDO> esxiHosts = vmService.getEsxiHost();
        for (EsxiHostDO esxi : esxiHosts)
            System.err.println(esxi);
    }

    @Test
    public void testTime() {
        try{
            System.err.println(TimeUtils.timeLapse("2017-08-23 10:54:00", TimeUtils.hourTime));
        }catch (Exception e){

        }
    }

}
