package com.baiyi.opscloud.vmware.vcsa.vm.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.vmware.vcsa.handler.VcsaHandler;
import com.baiyi.opscloud.vmware.vcsa.instance.VMInstance;
import com.baiyi.opscloud.vmware.vcsa.vm.VcsaVM;
import com.google.common.collect.Lists;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineStorageInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/15 12:30 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class VcsaVMImpl implements VcsaVM {

    @Resource
    private VcsaHandler vcsaHandler;

    public static final String SEARCH_VIRTUALMACHINE_TYPE = "VirtualMachine";

    @Override
    public List<VMInstance> getInstanceList() {
        try {
            ManagedEntity[] mes = vcsaHandler.searchManagedEntities(SEARCH_VIRTUALMACHINE_TYPE);
            if (mes != null) return convert(mes);
        } catch (Exception e) {
            log.error("VCSA SearchManagedEntities VirtualMachine Error");
        }
        return Collections.emptyList();
    }

    @Override
    public VMInstance getInstance(String serverName) {
        try {
            ManagedEntity me = vcsaHandler.searchManagedEntity(SEARCH_VIRTUALMACHINE_TYPE, serverName);
            if (me != null) return convert(me);
        } catch (Exception e) {
            log.error("VCSA SearchManagedEntities HostSystem Error");
        }
        return null;
    }


    @Override
    public String getZone() {
        return vcsaHandler.getZone();
    }

    @Override
    public BusinessWrapper<Boolean> power(String instanceName, Boolean action) {
        if (StringUtils.isEmpty(instanceName))
            return new BusinessWrapper<>(false);
        //ServiceInstance serviceInstance = getAPI();
        try {
            ManagedEntity me = vcsaHandler.searchManagedEntity(SEARCH_VIRTUALMACHINE_TYPE, instanceName);
            if (action) {
                ((VirtualMachine) me).powerOnVM_Task(null);
            } else {
                ((VirtualMachine) me).powerOffVM_Task();
            }
        } catch (Exception e) {
            log.error("VM {} {}",instanceName,ErrorEnum.CLOUD_SERVER_POWER_MGMT_FAILED.getMessage());
            return new BusinessWrapper<Boolean>(ErrorEnum.CLOUD_SERVER_POWER_MGMT_FAILED);
        }
        return new BusinessWrapper<>(true);
    }

    private List<VMInstance> convert(ManagedEntity[] mes) {
        List<VMInstance> vmList = Lists.newArrayList();
        for (ManagedEntity me : mes)
            vmList.add(convert(me));
        return vmList;
    }

    private VMInstance convert(ManagedEntity me) {
        VirtualMachine vm = (VirtualMachine) me;
        VirtualMachineConfigInfo configInfo = vm.getConfig();
        VirtualMachineStorageInfo storageInfo = vm.getStorage();
        VirtualMachineSummary summary = vm.getSummary();
        return new VMInstance(configInfo, storageInfo, summary);
    }

}
