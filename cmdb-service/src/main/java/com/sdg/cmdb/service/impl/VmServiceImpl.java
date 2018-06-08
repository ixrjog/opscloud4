package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.VcsaItemEnum;
import com.sdg.cmdb.domain.esxi.EsxiHostDO;
import com.sdg.cmdb.domain.esxi.HostDatastoreInfoVO;
import com.sdg.cmdb.domain.esxi.HostDatastoreMountInfoVO;
import com.sdg.cmdb.domain.esxi.VirtualVmFileVO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.CacheEsxiHostService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.VmService;
import com.sdg.cmdb.util.vmware.util.ClientSesion;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/1/13.
 */
@Service
public class VmServiceImpl implements VmService {


    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    //private static Folder rootFolder;

    private ServiceInstance serviceInstance;

    private String[] excludeEsxiDatastores = {"esxi", "nfs0"};

    @Resource
    private ServerDao serverDao;

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private CacheEsxiHostService cacheEsxiHostService;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.VCSA.getItemKey());
    }

    private void login() {

        HashMap<String, String> configMap = acqConifMap();
        String vcsaHost = configMap.get(VcsaItemEnum.VCSA_HOST.getItemKey());
        String vcsaUser = configMap.get(VcsaItemEnum.VCSA_USER.getItemKey());
        String vcsaPasswd = configMap.get(VcsaItemEnum.VCSA_PASSWD.getItemKey());

        try {
            ClientSesion session = new ClientSesion(vcsaHost, vcsaUser, vcsaPasswd);
            URL url = new URL("https", session.getHost(), "/sdk");
            ServiceInstance si = new ServiceInstance(url, session.getUsername(), session.getPassword(), true);
            //rootFolder = si.getRootFolder();
            serviceInstance = si;
            //VCenter vc = new VCenter();
            AboutInfo ai = si.getAboutInfo();
            //System.out.println("名称" + ai.getFullName());
            //System.out.println("版本：" + ai.getVersion());
            //System.out.println(ai.apiType);
            //si.currentTime();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vcsa 登陆失败");
            serviceInstance = null;
        }
    }


    /**
     * 查询vm总数
     *
     * @return
     */
    private int vmTotalCount() {
        this.login();
        try {
            ManagedEntity[] mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("VirtualMachine");
            if (mes == null || mes.length == 0) {
                serviceInstance.getRootFolder().getServerConnection().logout();
                return 0;
            } else {
                return mes.length;
            }
        } catch (Exception e) {
            logger.error("vcsa 查询错误");
        }
        return 0;
    }

    /**
     * 查询所有的虚拟机
     *
     * @return
     */
    private List<VmServerDO> vmGetAll() {
        this.login();
        try {
            ManagedEntity[] mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("VirtualMachine");
            if (mes == null || mes.length == 0) {
                serviceInstance.getRootFolder().getServerConnection().logout();
                return null;
            }
            List<VmServerDO> listVmServerDO = new ArrayList<VmServerDO>();
            for (int i = 0; i < mes.length; i++) {
                VirtualMachine vm = (VirtualMachine) mes[i];
                VmServerDO vmServerDO = new VmServerDO(vm);
                listVmServerDO.add(vmServerDO);
            }
            return listVmServerDO;
        } catch (Exception e) {
            logger.error("vcsa 查询错误");
        }
        return null;
    }

    private boolean saveVmServer(VmServerDO vmServerDO) {
        VmServerDO vm = serverDao.queryVmServerByInsideIp(vmServerDO.getInsideIp());
        if (vm == null) {
            return addVmServer(vmServerDO);
        } else {
            vmServerDO.setId(vm.getId());
            return updateVmServer(vmServerDO);
        }
    }

    /**
     * 新增vm
     *
     * @param vmServerDO
     * @return
     */
    private boolean addVmServer(VmServerDO vmServerDO) {
        try {
            serverDao.addVmServer(vmServerDO);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 更新vm
     *
     * @param vmServerDO
     * @return
     */
    private boolean updateVmServer(VmServerDO vmServerDO) {
        try {
            serverDao.updateVmServer(vmServerDO);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从server表更新ecsServer数据（content,serverId 字段数据）
     *
     * @param vmServerDO
     * @return
     */
    public boolean updateVmServerForServer(VmServerDO vmServerDO) {
        if (vmServerDO == null) return false;
        ServerDO serverDO = serverDao.queryServerByInsideIp(vmServerDO.getInsideIp());
        if (serverDO == null) return false;
        vmServerDO.setServerName(serverDO.getServerName());
        vmServerDO.setContent(serverDO.getContent());
        vmServerDO.setServerId(serverDO.getId());
        vmServerDO.setStatus(EcsServerDO.statusAssociate);
        return saveVmServer(vmServerDO);
    }


    public boolean updateVmServerForServer(ServerDO serverDO) {
        if (serverDO == null) return false;
        VmServerDO vmServerDO = serverDao.queryVmServerByInsideIp(serverDO.getInsideIp());
        if (vmServerDO == null) return false;
        vmServerDO.setServerName(serverDO.getServerName());
        vmServerDO.setContent(serverDO.getContent());
        vmServerDO.setServerId(serverDO.getId());
        vmServerDO.setStatus(EcsServerDO.statusAssociate);
        return saveVmServer(vmServerDO);
    }

    /**
     * 查询vmServer
     *
     * @param serverDO
     * @return
     */
    private VmServerDO vmGet(ServerDO serverDO) {
        if (serverDO == null) return null;
        VmServerDO vmServerDO = serverDao.queryVmServerByInsideIp(serverDO.getInsideIp());
        if (vmServerDO != null) return vmServerDO;
        return new VmServerDO();
    }

    /**
     * 查询server中对比vmServer多余的服务器
     */
    private List<VmServerDO> acqServerExclude() {
        List<ServerDO> servers = serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.vm.getCode());
        List<VmServerDO> vms = vmGetAll();
        for (VmServerDO vm : vms) {
            for (ServerDO server : servers) {
                // 匹配内网ip;匹配则删除
                if (server.getInsideIp().equals(vm.getInsideIp())) {
                    servers.remove(server);
                    break;
                }
            }
        }
        List<VmServerDO> result = new ArrayList<VmServerDO>();
        for (ServerDO server : servers) {
            result.add(new VmServerDO(server, EcsServerDO.statusOffline));
        }
        return result;
    }

    /**
     * 新增 查询server中没有登记的vmServer
     *
     * @return
     */
    private List<VmServerDO> acqVmExclude() {
        List<ServerDO> servers = serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.vm.getCode());
        List<VmServerDO> vms = vmGetAll();
        for (ServerDO server : servers) {
            for (VmServerDO vm : vms) {
                // 匹配内网ip;匹配则删除
                if (server.getInsideIp().equals(vm.getInsideIp())) {
                    vms.remove(vm);
                    break;
                }
            }
        }
        if (vms.size() == 0) return vms;
        for (VmServerDO vm : vms) {
            vm.setStatus(EcsServerDO.statusNew);
        }
        return vms;
    }

    @Override
    public BusinessWrapper<Boolean> vmModifyName(VmServerDO vmServer) {
        String vmName = vmServer.getInsideIp() + ":" + vmServer.getServerName();
        if (vmServer.getVmName().equals(vmName)) return new BusinessWrapper<>(true);
        this.login();
        try {
            VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
                    serviceInstance.getRootFolder()).searchManagedEntity("VirtualMachine", vmServer.getVmName());
            if (vm == null) {
                logger.error("vcsa 查询错误:" + " No VM " + vmServer.getVmName() + " found");
                serviceInstance.getRootFolder().getServerConnection().logout();
                return new BusinessWrapper<>(false);
            }
            VmServerDO vmServerDO = new VmServerDO(vm);
            VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
            vmConfigSpec.setName(vmServerDO.getInsideIp() + ":" + vmServerDO.getServerName());
            Task task = vm.reconfigVM_Task(vmConfigSpec);
            if (task.waitForMe() == Task.SUCCESS) {
                serviceInstance.getRootFolder().getServerConnection().logout();
                return new BusinessWrapper<>(true);
            } else {
                serviceInstance.getRootFolder().getServerConnection().logout();
                return new BusinessWrapper<>(false);
            }
        } catch (Exception e) {
            serviceInstance.getRootFolder().getServerConnection().logout();
            logger.error("vcsa 查询错误");
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> powerOff(VmServerDO vmServerDO) {
        String vmName = vmServerDO.getInsideIp() + ":" + vmServerDO.getServerName();
        this.login();
        try {
            //VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
            //        rootFolder).searchManagedEntity("VirtualMachine", name);
            vmName = "10.17.1.28:windows2008R2.vm.template";
            ManagedEntity mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity("VirtualMachine", vmName);
            if (mes == null) {
                logger.error("vcsa 查询错误:" + " No VM " + vmName + " found");
                serviceInstance.getRootFolder().getServerConnection().logout();
                return new BusinessWrapper<>(false);
            }
            ((VirtualMachine) mes).powerOffVM_Task();
        } catch (Exception e) {
            serviceInstance.getRootFolder().getServerConnection().logout();
            logger.error("vcsa 查询错误");
        }
        serviceInstance.getRootFolder().getServerConnection().logout();
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> powerOn(VmServerDO vmServerDO) {
        String vmName = vmServerDO.getInsideIp() + ":" + vmServerDO.getServerName();
        this.login();
        try {
            vmName = "10.17.1.28:windows2008R2.vm.template";
            ManagedEntity mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity("VirtualMachine", vmName);
            if (mes == null) {
                logger.error("vcsa 查询错误:" + " No VM " + vmName + " found");
                serviceInstance.getRootFolder().getServerConnection().logout();
                return new BusinessWrapper<>(false);
            }
            ((VirtualMachine) mes).powerOnVM_Task(null);
        } catch (Exception e) {
            serviceInstance.getRootFolder().getServerConnection().logout();
            logger.error("vcsa 查询错误");
        }
        serviceInstance.getRootFolder().getServerConnection().logout();
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> vmRefresh() {
        List<VmServerDO> vms = vmGetAll();
        for (VmServerDO vm : vms) {
            if (!saveVmServer(vm)) {
                return new BusinessWrapper<>(false);
            }
            updateVmServerForServer(vm);
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> rename() {
        List<VmServerDO> vms = vmGetAll();
        for (VmServerDO vm : vms) {
            vmModifyName(vm);
        }
        return new BusinessWrapper<>(true);
    }


    @Override
    public BusinessWrapper<Boolean> vmCheck() {
        //查询server中没有登记的ecsServer
        List<VmServerDO> server = acqVmExclude();
        for (VmServerDO vm : server) {
            saveVmServer(vm);
        }
        //查询server中对比ecsServer多余的服务器
        server = acqServerExclude();
        for (VmServerDO vm : server) {
            saveVmServer(vm);
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> delVm(String insideIp) {
        VmServerDO vmServerDO = serverDao.queryVmServerByInsideIp(insideIp);
        if (vmServerDO == null) return new BusinessWrapper<>(false);
        serverDao.delVmServerById(vmServerDO.getId());
        return new BusinessWrapper<>(true);
    }


    @Override
    public TableVO<List<VmServerDO>> getVmServerPage(String serverName, String queryIp, int status, int page, int length) {
        long size = serverDao.getVmServerSize(serverName, queryIp, status);
        List<VmServerDO> list = serverDao.getVmServerPage(serverName, queryIp, status, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public ServerStatisticsDO statistics() {
        ServerStatisticsDO serverStatisticsDO = serverDao.queryVmStatistics();
        if (serverStatisticsDO.getCnt() == 0) return serverStatisticsDO;
        ServerStatisticsDO psServerStatisticsDO = serverDao.queryPsVmStatistics();
        if (psServerStatisticsDO.getCnt() == 0) return serverStatisticsDO;
        int rate = serverStatisticsDO.getMemoryCnt() * 100 / psServerStatisticsDO.getMemoryCnt();
        serverStatisticsDO.setRate(rate);
        serverStatisticsDO.setServerType(ServerStatisticsDO.ServerTypeEnum.vmServer.getCode());
        return serverStatisticsDO;
    }


    @Override
    public TableVO<List<VmTemplateDO>> getVmTemplatePage(int page, int length) {
        long size = serverDao.getVmTemplateSize();
        List<VmTemplateDO> list = serverDao.getVmTemplatePage(page * length, length);
        return new TableVO<>(size, list);

    }

    /**
     * 获取EsxiHost详细信息
     *
     * @param physicalServerDO
     * @return
     */
    @Override
    public void hostSystemMemeoryConfig(PhysicalServerDO physicalServerDO,EsxiHostDO esxiHostDO) {
        this.login();
        try {
            //获取 HostSystem
            ManagedEntity mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity("HostSystem", physicalServerDO.getServerName());
            //逻辑判断
            if (mes != null) {
                //   for(int i=0;i<mes.length;i++){
                HostSystem hostSystem = (HostSystem) mes;
                //System.err.println("HostSystem name:" + hostSystem.getName());
                double memorySize = (double) hostSystem.getHardware().memorySize / 1024 / 1024; //内存总容量
                Integer overallMemoryUsage = hostSystem.getSummary().quickStats.overallMemoryUsage; //内存使用容量(MB)
                HostListSummary summary = hostSystem.getSummary();
                int numcpu = summary.getHardware().numCpuCores;////cpu个数
                //EsxiHostDO esxiHostDO = new EsxiHostDO();
                esxiHostDO.setMemeryTotal(memorySize);
                esxiHostDO.setOverallMemoryUsage(overallMemoryUsage);
                esxiHostDO.setSummary(summary);
                esxiHostDO.setNumcpu(numcpu);
                esxiHostDO.setVmName(physicalServerDO.getServerName());
                //System.out.println("number cpu:" + numcpu);
                //System.err.println("memery total:" + memorySize);
                //System.err.println("memery used:" + overallMemoryUsage);
            } else {
                serviceInstance.getServerConnection().logout();
            }
            //   }
            //serviceInstance.getServerConnection().logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取HostSystem存储信息
     *
     * @param datastore
     * @return
     */
    public HostDatastoreInfoVO setDatastore(Datastore datastore, boolean isDiscoveryVmFile) {
        HostDatastoreInfoVO hostDatastore = new HostDatastoreInfoVO();
        hostDatastore.setName(datastore.getSummary().getName());
        hostDatastore.setUrl(datastore.getSummary().getUrl());
        hostDatastore.setMaxFileSize(datastore.getInfo().getMaxFileSize());
        hostDatastore.setFreeSpace(datastore.getSummary().getFreeSpace());
        hostDatastore.setTimestamp(datastore.getInfo().getTimestamp());
        hostDatastore.setCapacity(datastore.getSummary().getCapacity()); //容量
        hostDatastore.setProgId(datastore.getMOR().val);
        hostDatastore.setAccessible("0"); //连接状态
        hostDatastore.setDsType(datastore.getSummary().getType());
        if (datastore.getSummary().isAccessible())
            hostDatastore.setAccessible("1");
        if (datastore.getHost() != null)
            for (int j = 0; j < datastore.getHost().length; j++) {
                HostMountInfo hostMountInfo = datastore.getHost()[j].getMountInfo();
                HostDatastoreMountInfoVO hostDatastoreMount = new HostDatastoreMountInfoVO();
                hostDatastoreMount.setPath(hostMountInfo.getPath());
                hostDatastoreMount.setAccessible(hostMountInfo.getAccessible());
                hostDatastoreMount.setAccessMode(hostMountInfo.getAccessMode());
                hostDatastoreMount.setMounted(hostMountInfo.getMounted());
                hostDatastore.setHostMount(hostDatastoreMount);
            }
        if (isDiscoveryVmFile) //是否发现数据源虚拟文件
        {
            ArrayList<VirtualVmFileVO> vmFiles = getDatastoreVmFile(datastore);
            hostDatastore.setVmFiles(vmFiles);
        }
        return hostDatastore;
    }

    /**
     * 得取数据存储下面虚拟机文件,也就是虚拟磁盘
     *
     * @param datastore
     * @return
     */

    public ArrayList<VirtualVmFileVO> getDatastoreVmFile(Datastore datastore) {
        ArrayList<VirtualVmFileVO> result = new ArrayList<VirtualVmFileVO>();
        try {

            HostDatastoreBrowser dsBrowser = datastore.getBrowser();
            DatastoreSummary ds = datastore.getSummary();
            String dsName = ds.getName();
            VmDiskFileQueryFilter vdiskFilter = new VmDiskFileQueryFilter();
            vdiskFilter.setControllerType(new String[]{});
            VmDiskFileQuery fQuery = new VmDiskFileQuery();
            fQuery.setFilter(vdiskFilter);
            HostDatastoreBrowserSearchSpec searchSpec = new HostDatastoreBrowserSearchSpec();
            searchSpec.setQuery(new FileQuery[]{fQuery});
            Task task = dsBrowser.searchDatastoreSubFolders_Task("[" + dsName + "]", searchSpec);
            task.waitForMe();
            TaskInfo tInfo = task.getTaskInfo();
            ArrayOfHostDatastoreBrowserSearchResults searchResult = (ArrayOfHostDatastoreBrowserSearchResults) tInfo.getResult();
            int len = searchResult.getHostDatastoreBrowserSearchResults().length;
            for (int j = 0; j < len; j++) {
                HostDatastoreBrowserSearchResults sres = searchResult.HostDatastoreBrowserSearchResults[j];
                FileInfo[] fileArray = sres.getFile();
                if (fileArray == null) continue;
                for (int k = 0; k < fileArray.length; k++) {
                    VirtualVmFileVO vmFile = new VirtualVmFileVO();
                    vmFile.setFileSize(fileArray[k].getFileSize());
                    vmFile.setModification(fileArray[k].getModification());
                    vmFile.setOwner(fileArray[k].getOwner());
                    vmFile.setPath(fileArray[k].getPath());
                    result.add(vmFile);
                }
            }
        } catch (Exception e) {

        }
        return result;
    }

    public HostSystem getEsxiHostSystem(String hostName) {
        this.login();
        try {
            Folder rootFolder = serviceInstance.getRootFolder();
            HostSystem hostSystem = (HostSystem) new InventoryNavigator(
                    rootFolder).searchManagedEntity("HostSystem", hostName);
            if (hostSystem == null) {
                logger.error("No Esxi {} found", hostName);
                //System.out.println("No VM " + vmName + " found");
                serviceInstance.getServerConnection().logout();
                return null;
            }
            return hostSystem;
        } catch (Exception e) {
            logger.error("Search Esxi {} not found", hostName);
            return null;
        }
    }

    /**
     * 获取esxi数据存储信息
     *
     * @param serverName
     * @return
     */
    @Override
    public List<HostDatastoreInfoVO> getEsxiHostDatastores(String serverName) {
        List<HostDatastoreInfoVO> list = new ArrayList<HostDatastoreInfoVO>();
        try {
            HostSystem hostSystem = getEsxiHostSystem(serverName);
            if (hostSystem == null) return list;
            //指定服务器：HostSystem 关联的Datastore
            Datastore[] datastores = hostSystem.getDatastores();

            //HostSystemDataStoreFound hostSystemDataStoreFound = new HostSystemDataStoreFound();
            for (int i = 0; i < datastores.length; i++) {
                Datastore datastore = datastores[i];
                HostDatastoreInfoVO hostDatastoreInfo = setDatastore(datastore, false);
                list.add(hostDatastoreInfo);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    @Override
    public List<VmServerDO> getVirtualMachines(String hostName) {
        this.login();
        //List<String> list=new ArrayList<String>();
        List<VmServerDO> vmServers = new ArrayList<VmServerDO>();

        try {
            ManagedEntity mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity("HostSystem", hostName);
            if (mes != null) {
                HostSystem systems = (HostSystem) mes;
                VirtualMachine[] virtualMachines = systems.getVms();
                if (virtualMachines != null && virtualMachines.length > 0) {
                    for (VirtualMachine virtualMachine : virtualMachines) {
                        VirtualMachinePowerState state = virtualMachine.getRuntime().powerState;
                        //String virtualMachineName=virtualMachine.getName();
                        //String vmname[]=virtualMachineName.split(":");
                        //System.err.println(virtualMachine.getName());
                        VmServerDO vmServerDO = new VmServerDO(virtualMachine);
                        vmServers.add(vmServerDO);
                    }
                }
            }
        } catch (Exception e) {
            serviceInstance.getServerConnection().logout();
            return vmServers;
        } finally {
            serviceInstance.getServerConnection().logout();
        }
        return vmServers;
    }

    @Override
    public TableVO<List<VmServerDO>> getEsxiVmsPage(String serverName) {
        List<VmServerDO> vmServers = getVirtualMachines(serverName);
        return new TableVO<>(vmServers.size(), vmServers);
    }

    @Override
    public TableVO<List<HostDatastoreInfoVO>> getEsxiDatastoresPage(String serverName) {
        EsxiHostDO esxiHostDO = getEsxiHost(serverName);
        List<HostDatastoreInfoVO> datastores = esxiHostDO.getDatastores();
        cacheEsxiHostService.insert(esxiHostDO);
        return new TableVO<>(datastores.size(), datastores);
    }

    @Override
    public EsxiHostDO getEsxiHost(String serverName) {
        EsxiHostDO esxiHostDO = new EsxiHostDO();
        esxiHostDO.setVmName(serverName);
        esxiHostDO.setDatastores(getEsxiHostDatastores(serverName));
        invokeDatastoresRate(esxiHostDO);
        return esxiHostDO;
    }

    /**
     * 计算Esxi的数据存储使用率
     *
     * @param esxiHostDO
     */
    private void invokeDatastoresRate(EsxiHostDO esxiHostDO) {
        List<HostDatastoreInfoVO> datastores = esxiHostDO.getDatastores();
        Long capacity = 0l;
        Long free = 0l;
        for (HostDatastoreInfoVO ds : datastores) {
            String dsName[] = ds.getName().split("\\.");
            // 排除部分存储（系统盘+nfs）
            boolean checkName = false;
            for (String excludeName : excludeEsxiDatastores) {
                if (excludeName.equalsIgnoreCase(dsName[dsName.length - 1])) {
                    checkName = true;
                    continue;
                }
            }
            if (checkName) continue;
            capacity += ds.getCapacity();
            free += ds.getFreeSpace();
        }
        Long rate = 100 - (free * 100 / capacity);
        esxiHostDO.setDsRate(rate.intValue());
    }

    @Override
    public List<EsxiHostDO> getEsxiHost() {
        List<EsxiHostDO> esxiHosts = new ArrayList<EsxiHostDO>();
        List<PhysicalServerDO> servers = serverDao.getPhysicalServerEsxi();
        for (PhysicalServerDO server : servers) {
            EsxiHostDO esxiHostDO = getEsxiHost(server.getServerName());
            esxiHosts.add(esxiHostDO);
        }
        return esxiHosts;
    }

}
