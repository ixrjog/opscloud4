package com.sdg.cmdb.domain.server;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/4/21.
 */
@Data
public class CreateEcsVO implements Serializable {

    private static final long serialVersionUID = 5054980679991184075L;

    private EcsTemplateDO ecsTemplateDO;

    private long ecsTemplateId;

    private ServerVO serverVO;

    /**
     * 系统盘容量 GB
     */
    private int systemDiskSize;

    /**
     * 数据盘容量 GB
     */
    private int dataDiskSize;

    /**
     * 需要创建的服务器数量
     */
    private int cnt;

    /**
     * 0 默认
     * 1 自定义
     */
    private int networkConfig;

    /**
     * 是否分配公网ip
     * 1 是
     * 0 否
     */
    private boolean allocatePublicIpAddress;

    /**
     * 付费类型
     * 实例的付费方式。取值范围：
     * PrePaid：预付费，即包年包月。选择该类付费方式的用户必须确认自己的账号支持余额支付/信用支付，否则将返回 InvalidPayMethod 的错误提示。
     * PostPaid：后付费，即按量付费。
     * 默认值：PostPaid
     */
    private String chargeType = "PostPaid";

    /**
     * 购买资源的时长，单位为：月。当 InstanceChargeType 为 PrePaid 时才生效且为必选值。取值范围：
     * 1 - 9
     * 12
     * 24
     * 36
     */
    private int period = 1;
    private long imageId;
    private String networkType;
    private long vpcId;
    private long vswitchId;
    private long securityGroupId;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
