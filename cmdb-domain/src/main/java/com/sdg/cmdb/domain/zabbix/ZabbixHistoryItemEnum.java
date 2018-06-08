package com.sdg.cmdb.domain.zabbix;

/**
 * Created by zxxiao on 2017/5/25.
 */
public enum ZabbixHistoryItemEnum {
    CPU_USER("system.cpu.util[,user]", "cpu用户使用率"),
    CPU_IO_WAIT("system.cpu.util[,iowait]", "采集cpu iowait"),
    PER_CPU_AVG1("system.cpu.load[percpu,avg1]", "1分钟load"),
    PER_CPU_AVG5("system.cpu.load[percpu,avg5]", "5分钟load"),
    PER_CPU_AVG15("system.cpu.load[percpu,avg15]", "15分钟load"),
    MEM_SIZE_TOTAL("vm.memory.size[total]", "服务器总内存容量"),
    MEM_SIZE_AVAILABLE("vm.memory.size[available]", "服务器可用内存容量"),
    VFS_FS_SIZE_PFREE("vfs.fs.size[/,pfree]", "系统盘剩余空间"),
    VFS_FS_SIZE_DATA_PFREE("vfs.fs.size[/data,pfree]", "数据盘剩余空间"),
    VFS_FS_SIZE_TOTAL("vfs.fs.size[/,total]", "系统盘总容量"),
    VFS_FS_SIZE_USED("vfs.fs.size[/,used]", "系统盘使用容量"),
    VFS_FS_SIZE_DATA_TOTAL("vfs.fs.size[/data,total]", "数据盘总容量"),
    VFS_FS_SIZE_DATA_USED("vfs.fs.size[/data,used]", "数据盘使用容量"),
    NET_IF_IN_ETH0("net.if.in[eth0]", "网卡0（内网网卡）下行流量"),
    NET_IF_OUT_ETH0("net.if.out[eth0]", "网卡0（内网网卡）上行流量"),
    NET_IF_IN_ETH1("net.if.in[eth1]", "网卡1（公网网卡）下行流量"),
    NET_IF_OUT_ETH1("net.if.out[eth1]", "网卡1（公网网卡）上行流量"),
    TOMCAT_VERSION("jmx[\"Catalina:type=Server\",serverInfo]", "tomcat版本信息")
    ;
    private String itemKey;
    private String itemDesc;

    ZabbixHistoryItemEnum(String itemKey, String itemDesc) {
        this.itemKey = itemKey;
        this.itemDesc = itemDesc;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemDescByKey(String itemKey) {
        for(ZabbixHistoryItemEnum itemEnum : ZabbixHistoryItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}
