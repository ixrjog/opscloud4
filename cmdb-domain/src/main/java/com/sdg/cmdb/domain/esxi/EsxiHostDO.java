package com.sdg.cmdb.domain.esxi;

import com.sdg.cmdb.domain.server.VmServerDO;
import com.vmware.vim25.HostListSummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/7/28.
 */
public class EsxiHostDO implements Serializable {
    private static final long serialVersionUID = -1719872350746627715L;

    private String vmName;

    //物理机内存总数
    private double memeryTotal;

    //虚拟机内存使用
    private Integer overallMemoryUsage =0;

    private HostListSummary summary;

    //cpu个数
    private int numcpu;

    //内存使用率
    private Integer memoryUseRate=0;

    //数据存储信息
    private List<HostDatastoreInfoVO> datastores;

    //Esxi下的所有虚拟机
    private List<VmServerDO> vmServers;

    //数据存储使用率
    private int dsRate;

    //虚拟机数量
    private int vmSize = 0;

    @Override
    public String toString() {
        return "EsxiHostDO{" +
                "id=" + 0 +
                ", vmName='" + vmName + '\'' +
                ", memeryTotal=" + memeryTotal +
                ", overallMemoryUsage=" + overallMemoryUsage +
                ", summary='" + summary + '\'' +
                ", numcpu=" + numcpu +
                ", vmSize=" + vmSize +
                ", dsRate=" + dsRate +
                '}';
    }

    public Integer getMemoryUseRate() {
        if (overallMemoryUsage == 0) return 0;
        Double r = overallMemoryUsage * 100 / memeryTotal;
        return r.intValue();
    }

    public void setMemoryUseRate(int memoryUseRate) {
        this.memoryUseRate = memoryUseRate;
    }

    public double getMemeryTotal() {
        return memeryTotal;
    }

    public void setMemeryTotal(double memeryTotal) {
        this.memeryTotal = memeryTotal;
    }

    public Integer getOverallMemoryUsage() {
        return overallMemoryUsage;
    }

    public void setOverallMemoryUsage(Integer overallMemoryUsage) {
        this.overallMemoryUsage = overallMemoryUsage;
    }

    public HostListSummary getSummary() {
        return summary;
    }

    public void setSummary(HostListSummary summary) {
        this.summary = summary;
    }

    public int getNumcpu() {
        return numcpu;
    }

    public void setNumcpu(int numcpu) {
        this.numcpu = numcpu;
    }

    public List<HostDatastoreInfoVO> getDatastores() {
        return datastores;
    }

    public void setDatastores(List<HostDatastoreInfoVO> datastores) {
        this.datastores = datastores;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<VmServerDO> getVmServers() {
        return vmServers;
    }

    public void setVmServers(List<VmServerDO> vmServers) {
        this.vmServers = vmServers;
    }

    public int getDsRate() {
        return dsRate;
    }

    public void setDsRate(int dsRate) {
        this.dsRate = dsRate;
    }

    public int getVmSize() {
        if (vmServers != null)
            return vmServers.size();
        return vmSize;
    }

    public void setVmSize(int vmSize) {
        this.vmSize = vmSize;
    }
}
