package com.sdg.cmdb.domain.esxi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by liangjian on 2017/8/2.
 */
public class HostDatastoreInfoVO implements Serializable {
    private static final long serialVersionUID = 9094111522145383623L;

    private String name; //名称
    private String url;  //定位数据存储
    private long freeSpace;  //剩余空间容量 单位:byte
    private long maxFileSize; //最大文件容量 单位:byte
    private long capacity;//容量
    //内存使用率
    private int rate;
    private String accessible; //连接状态
    private String uuid;//特定id
    private Calendar timestamp; //空间剩余空间更新时间
    private String progId;
    private ArrayList<HostDatastoreMountInfoVO> hostMounts = new ArrayList<HostDatastoreMountInfoVO>();//挂载主机相关信息
    private ArrayList<VirtualVmFileVO> vmFiles = new ArrayList<VirtualVmFileVO>();//虚拟机文件
    private String dsType;//存储类型

    public long getFreeSpace() {
        return freeSpace / 1024 / 1024 / 1024;
    }

    public void setFreeSpace(long freeSpace) {
        this.freeSpace = freeSpace;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<HostDatastoreMountInfoVO> getHostMounts() {
        return hostMounts;
    }

    public void setHostMount(HostDatastoreMountInfoVO hostMount) {
        this.hostMounts.add(hostMount);
    }

    public void setHostMounts(ArrayList<HostDatastoreMountInfoVO> hostMounts) {
        this.hostMounts = hostMounts;
    }

    public ArrayList<VirtualVmFileVO> getVmFiles() {
        return vmFiles;
    }

    public void setVmFile(VirtualVmFileVO vmFile) {
        this.vmFiles.add(vmFile);
    }

    public void setVmFiles(ArrayList<VirtualVmFileVO> vmFiles) {
        this.vmFiles = vmFiles;
    }

    public long getCapacity() {
        return capacity / 1024 / 1024 / 1024;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessible() {
        return accessible;
    }

    public void setAccessible(String accessible) {
        this.accessible = accessible;
    }

    public String getDsType() {
        return dsType;
    }

    public void setDsType(String dsType) {
        this.dsType = dsType;
    }

    public String getProgId() {
        return progId;
    }

    public void setProgId(String progId) {
        this.progId = progId;
    }

    public int getRate() {
        Long r = freeSpace * 100 / capacity;
        return 100 - r.intValue();
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
