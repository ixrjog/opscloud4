package com.sdg.cmdb.domain.server;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/4/20.
 */
public class EcsTemplateDO implements Serializable {

    private static final long serialVersionUID = 2017938801152209206L;

    private long id;

    private String name;

    private String zoneId;

    private String instanceType;

    private int networkSupport;

    private int cpu;

    private int memory;

    private int dataDiskSize;

    private String ioOptimized;

    private String systemDiskCategory;

    private String dataDisk1Category;

    public enum NetworkSupportTypeEnum {
        //0 保留
        classic(0, "classic"),
        all(1, "all"),
        vpc(2, "vpc");

        private int code;
        private String desc;

        NetworkSupportTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getNetworkSupportTypeName(int code) {
            for (NetworkSupportTypeEnum networkSupportTypeEnum : NetworkSupportTypeEnum.values()) {
                if (networkSupportTypeEnum.getCode() == code) {
                    return networkSupportTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getNetworkSupport() {
        return networkSupport;
    }

    public void setNetworkSupport(int networkSupport) {
        this.networkSupport = networkSupport;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
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

    public int getDataDiskSize() {
        return dataDiskSize;
    }

    public void setDataDiskSize(int dataDiskSize) {
        this.dataDiskSize = dataDiskSize;
    }

    public String getIoOptimized() {
        return ioOptimized;
    }

    public void setIoOptimized(String ioOptimized) {
        this.ioOptimized = ioOptimized;
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }

    public String getDataDisk1Category() {
        return dataDisk1Category;
    }

    public void setDataDisk1Category(String dataDisk1Category) {
        this.dataDisk1Category = dataDisk1Category;
    }
}
