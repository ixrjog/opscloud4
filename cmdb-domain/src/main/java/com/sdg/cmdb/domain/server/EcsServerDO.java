package com.sdg.cmdb.domain.server;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by liangjian on 2016/11/14.
 */
@Data
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

    /**
     * 过期时间。按照ISO8601标准表示，并需要使用UTC时间，格式为yyyy-MM-ddTHH:mm:ssZ。
     */
    private String expiredTime;

    private String gmtModify;

    private String gmtCreate;

    /**
     * 网络计费类型:
     * PayByTraffic：按流量计费
     * PayByBandwidth：按带宽计费
     */
    private String internetChargeType;

    /**
     * 实例的计费方式。可能值：
     * PrePaid：预付费（包年包月 ）
     * PostPaid：按量付费
     */
    private String instanceChargeType;

    private String instanceType;
    private String instanceTypeFamily;

    public EcsServerDO(DescribeInstancesResponse.Instance instance) {
        if (!StringUtils.isEmpty(instance.getCreationTime()))
            this.creationTime = acqGmtTime(instance.getCreationTime());
        this.instanceChargeType = instance.getInstanceChargeType();
        if (instance.getInstanceChargeType().equalsIgnoreCase("PrePaid") && !StringUtils.isEmpty(instance.getExpiredTime()))
            this.expiredTime = acqGmtTime(instance.getExpiredTime());
        this.deviceAvailable = instance.getDeviceAvailable();
        this.serverName = instance.getInstanceName();
        if (instance.getInstanceNetworkType().equals("vpc")) {
            this.insideIp = instance.getVpcAttributes().getPrivateIpAddress().get(0);
        } else {
            this.insideIp = instance.getInnerIpAddress().get(0);
        }
        if (instance.getPublicIpAddress().size() != 0) {
            this.publicIp = instance.getPublicIpAddress().get(0);
        }
        // 弹性IP
        if (!StringUtils.isEmpty(instance.getEipAddress().getIpAddress())) {
            this.publicIp = instance.getEipAddress().getIpAddress();
        }
        this.internetMaxBandwidthOut = instance.getInternetMaxBandwidthOut();
        this.cpu = instance.getCpu();
        this.memory = instance.getMemory();
        this.ioOptimized = instance.getIoOptimized();
        this.instanceId = instance.getInstanceId();
        this.area = instance.getZoneId();
        this.regionId = instance.getRegionId();
        this.internetChargeType = instance.getInternetChargeType();
        this.instanceType = instance.getInstanceType();
        this.instanceTypeFamily = instance.getInstanceTypeFamily();
    }

    //转换时间
    private String acqGmtTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            Date date = format.parse(time);
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
        return JSON.toJSONString(this);
    }


}
