package com.baiyi.opscloud.vmware.vcsa.impl;

import com.baiyi.opscloud.vmware.vcsa.handler.VcsaHandler;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.mo.ServiceInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/15 9:49 上午
 * @Version 1.0
 */
@Component("Vcsa")
public class VcsaImpl {

    @Resource
    private VcsaHandler vcsaHandler;

    public AboutInfo getVersion() {
        ServiceInstance serviceInstance = vcsaHandler.getAPI();
        return serviceInstance.getAboutInfo();
    }


//
//
//    @Override
//    public List<CloudServerDO> getESXiList() {
//        ServiceInstance serviceInstance = getAPI();
//        List<CloudServerDO> list = new ArrayList<>();
//        try {
//            //获取 HostSystem
//            ManagedEntity[] mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("HostSystem");
//            if (mes == null || mes.length == 0) {
//                log.error("VCSA查询VirtualMachine列表为空");
//                serviceInstance.getRootFolder().getServerConnection().logout();
//                return list;
//            }
//            for (int i = 0; i < mes.length; i++) {
//                HostSystem hostSystem = (HostSystem) mes[i];
//                // System.err.println(JSON.toJSONString(hostSystem));
//                HostHardwareInfo hostHardwareInfo = hostSystem.getHardware();
//                HostListSummary hostSummary = hostSystem.getSummary();
//                List<DatastoreSummary> datastoreSummaryList = getDatastoreSummaryList(hostSystem);
//                HostConfigInfo hostConfigInfo = hostSystem.getConfig();
//                CloudServerDO cloudServerDO = CloudServerBuilder.buildCloudServerDO(hostHardwareInfo, hostSummary, datastoreSummaryList, hostConfigInfo);
//                list.add(cloudServerDO);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//
//    private List<DatastoreSummary> getDatastoreSummaryList(HostSystem hostSystem) {
//        List<DatastoreSummary> datastoreSummaryList = new ArrayList<>();
//        try {
//            Datastore[] datastores = hostSystem.getDatastores();
//            for (Datastore datastore : datastores) {
//                DatastoreSummary datastoreSummary = datastore.getSummary();
//                datastoreSummaryList.add(datastoreSummary);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return datastoreSummaryList;
//    }
//
//    @Override
//    public ESXiHostInfo getESXiInfo(String uuid) {
//        CloudServerDO cloudServerDO = cloudServerDao.getCloudServerByInstanceId(uuid);
//        ESXiHostInfo esxiHostInfo = getESXiHostInfo(cloudServerDO.getInstanceName());
////        String key = Joiner.on(":").join("ESXi", "UUID", uuid);
////        String cache = cacheKeyService.getKeyByString(this.getClass(), key);
////        if (!StringUtils.isEmpty(cache)) {
////            esxiHostInfo = new GsonBuilder().create().fromJson(cache, ESXiHostInfo.class);
////        } else {
////            CloudServerDO cloudServerDO = cloudServerDao.getCloudServerByInstanceId(uuid);
////            esxiHostInfo = getESXiHostInfo(cloudServerDO.getInstanceName());
////            cacheKeyService.set(this.getClass(), key, JSON.toJSONString(esxiHostInfo), 5); // cache 10m
////        }
//        return esxiHostInfo;
//    }
//
//    private ESXiHostInfo getESXiHostInfo(String serverName) {
//        ServiceInstance serviceInstance = getAPI();
//        ESXiHostInfo esxiHostInfo = new ESXiHostInfo();
//        log.info("VCSA查询ESXI主机详情, serverName = {}", serverName);
//        try {
//            //获取 HostSystem
//            ManagedEntity mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity("HostSystem", serverName);
//            //逻辑判断
//            if (mes != null) {
//                HostSystem hostSystem = (HostSystem) mes;
//                esxiHostInfo.setMemeryTotal((int) (hostSystem.getHardware().memorySize / 1024 / 1024));
//                esxiHostInfo.setOverallMemoryUsage(hostSystem.getSummary().quickStats.overallMemoryUsage);
//                esxiHostInfo.calcMemoryUseRate(); // 计算内存使用率
//                invokeESXiHostInfo(esxiHostInfo, hostSystem.getDatastores(), hostSystem.getVms());
//            } else {
//                serviceInstance.getServerConnection().logout();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return esxiHostInfo;
//    }
//
//    private void invokeESXiHostInfo(ESXiHostInfo esxiHostInfo, Datastore[] datastores, VirtualMachine[] vms) {
//        long capacityTotal = 0l;
//        long freeSpace = 0l;
//        List<DatastoreSummary> datastoreList = new ArrayList<>();
//        List<VirtualMachineSummary> vmList = new ArrayList<>();
//        for (Datastore datastore : datastores) {
//            DatastoreSummary datastoreSummary = datastore.getSummary();
//            datastoreList.add(datastoreSummary);
//            capacityTotal += datastoreSummary.getCapacity();
//            freeSpace += datastoreSummary.getFreeSpace();
//        }
//        esxiHostInfo.setDatastoreList(datastoreList);
//        if (capacityTotal != 0) {
//            esxiHostInfo.setDatastoreRate(100 - (int) (freeSpace * 100 / capacityTotal));
//        }
//
//        for (VirtualMachine virtualMachine : vms)
//            vmList.add(virtualMachine.getSummary());
//        esxiHostInfo.setVmList(vmList);
//    }
//
//
//    /**
//     * 修改VM服务器名称
//     *
//     * @param instanceName 原VM名称
//     * @param sererName    新服务器名称
//     * @return
//     */
//    @Override
//    public void modifyInstanceName(String instanceName, String sererName) {
//        if (!StringUtils.isEmpty(sererName) && instanceName.equals(sererName)) return;
//        ServiceInstance serviceInstance = getAPI();
//        try {
//            VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
//                    serviceInstance.getRootFolder()).searchManagedEntity("VirtualMachine", instanceName);
//            if (vm == null) {
//                log.error("VCSA查询错误: instanceName={}", instanceName);
//            } else {
//                VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
//                vmConfigSpec.setName(sererName);
//                Task task = vm.reconfigVM_Task(vmConfigSpec);
//                if (task.waitForMe() == Task.SUCCESS) {
//                    log.info("VCSA修改vm名称成功,instanceName={}", instanceName);
//                } else {
//                    log.error("VCSA修改vm名称失败，instanceName={}", instanceName);
//                }
//            }
//        } catch (Exception e) {
//            log.error("VCSA查询错误");
//        }
//        serviceInstance.getRootFolder().getServerConnection().logout();
//    }
//
//
//    @Override
//    public BusinessWrapper<Boolean> powerOff(CloudServerDO cloudServerDO) {
//        String instanceName = cloudServerDO.getInstanceName();
//        return new BusinessWrapper<Boolean>(powerSwitch(instanceName, CloudServerAbstract.POWER_OFF));
//    }
//
//    @Override
//    public BusinessWrapper<Boolean> powerOn(CloudServerDO cloudServerDO) {
//        String instanceName = cloudServerDO.getInstanceName();
//        return new BusinessWrapper<Boolean>(powerSwitch(instanceName, CloudServerAbstract.POWER_ON));
//    }
//
//    /**
//     * 电源开关
//     *
//     * @param instanceName
//     * @param type
//     * @return
//     */
//    private boolean powerSwitch(String instanceName, boolean type) {
//        if (StringUtils.isEmpty(instanceName))
//            return false;
//        ServiceInstance serviceInstance = getAPI();
//        try {
//            ManagedEntity mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity("VirtualMachine", instanceName);
//            if (mes == null) {
//                log.error("VCSA查询VM错误: instanceName={}", instanceName);
//                serviceInstance.getRootFolder().getServerConnection().logout();
//                return false;
//            }
//            if (type == CloudServerAbstract.POWER_ON) {
//                ((VirtualMachine) mes).powerOnVM_Task(null);
//            } else {
//                ((VirtualMachine) mes).powerOffVM_Task();
//            }
//        } catch (Exception e) {
//            serviceInstance.getRootFolder().getServerConnection().logout();
//            log.error("VCSA查询VM错误,{}", e.getMessage());
//        }
//        serviceInstance.getRootFolder().getServerConnection().logout();
//        return true;
//    }
//


}
