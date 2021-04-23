package com.baiyi.opscloud.vmware.vcsa.instance;

import com.baiyi.opscloud.common.cloud.BaseCloudServerInstance;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineStorageInfo;
import com.vmware.vim25.VirtualMachineSummary;
import lombok.Data;

import java.io.Serializable;

@Data
public class VMInstance implements BaseCloudServerInstance, Serializable {
    private static final long serialVersionUID = -4582827294503230492L;

    public VMInstance() {
    }

    public VMInstance(VirtualMachineConfigInfo vmConfigInfo, VirtualMachineStorageInfo vmStorageInfo, VirtualMachineSummary vmSummary) {
        this.configInfoName = vmConfigInfo.getName();
        this.configInfoInstanceUuid = vmConfigInfo.getInstanceUuid();
        this.configInfoGuestFullName = vmConfigInfo.getGuestFullName();
        this.configInfoAnnotation = vmConfigInfo.getAnnotation();
        this.configInfoHardwareMemoryMB = vmConfigInfo.getHardware().getMemoryMB();
        this.configInfoHardwareNumCPU = vmConfigInfo.getHardware().getNumCPU();
        this.vmStorageInfo = vmStorageInfo;
        if (vmSummary != null)
            this.vmSummary = vmSummary;
    }

    private String configInfoName;           // instanceName
    private String configInfoInstanceUuid;   // instanceId
    private String configInfoGuestFullName;  // instanceType
    private String configInfoAnnotation;     // content
    private int configInfoHardwareMemoryMB;  // memory
    private int configInfoHardwareNumCPU;    // cpu

    private VirtualMachineStorageInfo vmStorageInfo;
    private VirtualMachineSummary vmSummary;

}
