package com.sdg.cmdb.domain.server;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by liangjian on 2016/11/14.
 */
public class EcsServerDO implements Serializable {

    private static final long serialVersionUID = -7988865367153517454L;

    private long id;

    private long serverId;

    private String content;

    //ecs创建时间
    private String creationTime;

    //是否可用
    private boolean deviceAvailable;

    private String serverName;

    private String insideIp;

    private String publicIp;
    //带宽(Mbps)
    private int internetMaxBandwidthOut;

    //Instance Id 重要pk,修改删除实例都需用到
    private String instanceId;

    //可用区 Id
    private String area;

    private String regionId;

    //阿里云false 金融云true
    private boolean finance = false;

    /**
     * 是否是 IO 优化型实例
     * True | False
     */
    private boolean ioOptimized;

    /**
     * 当前状态
     * 0 新增（未关联）
     * 1 关联  server表id
     * 2 下线（阿里云不存在）
     * 3 删除  (手动删除)
     */
    private int status;

    public static final int statusNew = 0;
    public static final int statusAssociate = 1;
    public static final int statusOffline = 2;
    public static final int statusDel = 3;

    private int cpu;

    private int memory;


    private String systemDiskCategory;

    private int systemDiskSize;

    private String dataDiskCategory;

    private int dataDiskSize;

    private String gmtModify;

    private String gmtCreate;

    /**
     * 网络计费类型:
     * PayByTraffic：按流量计费
     * PayByBandwidth：按带宽计费
     */
    private String internetChargeType;


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

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isDeviceAvailable() {
        return deviceAvailable;
    }

    public void setDeviceAvailable(boolean deviceAvailable) {
        this.deviceAvailable = deviceAvailable;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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

    public int getInternetMaxBandwidthOut() {
        return internetMaxBandwidthOut;
    }

    public void setInternetMaxBandwidthOut(int internetMaxBandwidthOut) {
        this.internetMaxBandwidthOut = internetMaxBandwidthOut;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public boolean isFinance() {
        return finance;
    }

    public void setFinance(boolean finance) {
        this.finance = finance;
    }

    public boolean isIoOptimized() {
        return ioOptimized;
    }

    public void setIoOptimized(boolean ioOptimized) {
        this.ioOptimized = ioOptimized;
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

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }

    public int getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(int systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getDataDiskCategory() {
        return dataDiskCategory;
    }

    public void setDataDiskCategory(String dataDiskCategory) {
        this.dataDiskCategory = dataDiskCategory;
    }

    public int getDataDiskSize() {
        return dataDiskSize;
    }

    public void setDataDiskSize(int dataDiskSize) {
        this.dataDiskSize = dataDiskSize;
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

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }

    public EcsServerDO(DescribeInstancesResponse.Instance ecs) {

        if (ecs.getCreationTime() != null)
            this.creationTime = acqCreationTime(ecs.getCreationTime());
        this.deviceAvailable = ecs.getDeviceAvailable();
        this.serverName = ecs.getInstanceName();
        if (ecs.getInstanceNetworkType().equals("vpc")) {
            this.insideIp = ecs.getVpcAttributes().getPrivateIpAddress().get(0);
        } else {
            this.insideIp = ecs.getInnerIpAddress().get(0);
        }
        if (ecs.getPublicIpAddress().size() != 0) {
            this.publicIp = ecs.getPublicIpAddress().get(0);
        }
        this.internetMaxBandwidthOut = ecs.getInternetMaxBandwidthOut();
        this.cpu = ecs.getCpu();
        this.memory = ecs.getMemory();
        this.ioOptimized = ecs.getIoOptimized();
        this.instanceId = ecs.getInstanceId();
        this.area = ecs.getZoneId();
        this.regionId = ecs.getRegionId();
        this.internetChargeType = ecs.getInstanceChargeType();
    }

    //转换时间
    private String acqCreationTime(String createTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            Date date = format.parse(createTime);
            SimpleDateFormat fmt;
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fmt.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public EcsServerDO(String instanceId) {
        this.instanceId = instanceId;
    }

    public EcsServerDO(ServerDO serverDO, int status) {
        this.publicIp = serverDO.getPublicIp();
        this.insideIp = serverDO.getInsideIp();
        this.serverName = serverDO.getServerName();
        this.area = serverDO.getArea();
        this.content = serverDO.getContent();
        this.serverId = serverDO.getId();
        this.status = status;
    }


    public EcsServerDO() {
    }


    @Override
    public String toString() {
        return "EcsServerDO{" +
                "id=" + id +
                ", creationTime='" + creationTime + '\'' +
                ", deviceAvailable=" + deviceAvailable +
                ", serverName='" + serverName + '\'' +
                ", serverId='" + serverId + '\'' +
                ", insideIp='" + insideIp + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", internetMaxBandwidthOut=" + internetMaxBandwidthOut +
                ", cpu=" + cpu +
                ", memory=" + memory +
                ", systemDiskCategory='" + systemDiskCategory + '\'' +
                ", systemDiskSize=" + systemDiskSize +
                ", dataDiskCategory='" + dataDiskCategory + '\'' +
                ", dataDiskSize=" + dataDiskSize +
                ", ioOptimized=" + ioOptimized +
                ", instanceId='" + instanceId + '\'' +
                ", area='" + area + '\'' +
                ", regionId='" + regionId + '\'' +
                ", internetChargeType='" + internetChargeType + '\'' +
                ", status='" + status + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


}
