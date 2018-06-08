package com.sdg.cmdb.domain.server;

import com.vmware.vim25.VirtualMachineAffinityInfo;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.VirtualMachine;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liangjian on 2016/12/27.
 */
public class VmServerDO implements Serializable {

    private static final long serialVersionUID = 6883024640202363061L;

    private long id;

    // server表id
    private long serverId;

    private String content;

    private String serverName;

    private String vmName;

    private String insideIp;

    private String publicIp;

    /**
     * 当前状态
     * 0 新增（未关联）
     * 1 关联  server表id
     * 2 下线（阿里云不存在）
     * 3 删除  (手动删除)
     */
    private int status;

    // 通电状态(poweredOff|poweredOn|suspended)
    private String powerState;

    private int cpu;

    // 单位MB
    private int memory;

    private String gmtModify;

    private String gmtCreate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getVmName() {
        return vmName;
    }

    public String getInsideIp() {
        return insideIp;
    }

    public void setInsideIp(String insideIp) {
        this.insideIp = insideIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public VmServerDO() {

    }

    public VmServerDO(ServerDO serverDO, int status) {
        this.content = serverDO.getContent();
        this.serverName = serverDO.getServerName();
        this.insideIp = serverDO.getInsideIp();
        this.serverId = serverDO.getId();
        this.status = status;
    }

    public VmServerDO(VirtualMachine vm) {
        VirtualMachineConfigInfo config = vm.getConfig();//虚拟机配置信息
        this.invokeVmName(vm.getName());
        this.content = config.getAnnotation();
        this.memory = config.getHardware().getMemoryMB();
        this.cpu = config.getHardware().getNumCPU();
        VirtualMachinePowerState state = vm.getRuntime().powerState;
        this.powerState = vm.getRuntime().powerState.name();
    }

    private void setVmName(String vmName) {
        this.vmName = vmName;
    }

    /**
     * 按vmName获取ip&serverName
     *
     * @param vmName
     */
    private void invokeVmName(String vmName) {
        //  10.17.1.200:test
        String regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})[:|-](.*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(vmName);
        while (m.find()) {
            this.insideIp = m.group(1);
            this.vmName = m.group(2);
        }
    }


    @Override
    public String toString() {
        return "VmServerDO{" +
                "id=" + id +
                ", serverName='" + serverName + '\'' +
                ", vmName='" + vmName + '\'' +
                ", serverId='" + serverId + '\'' +
                ", content='" + content + '\'' +
                ", insideIp='" + insideIp + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", cpu=" + cpu +
                ", memory=" + memory +
                ", status='" + status + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

}
