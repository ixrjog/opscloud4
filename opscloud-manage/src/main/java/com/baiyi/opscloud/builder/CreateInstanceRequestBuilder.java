package com.baiyi.opscloud.builder;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.bo.ServerBO;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/3/30 2:07 下午
 * @Version 1.0
 */
public class CreateInstanceRequestBuilder {

    public static CreateInstanceRequest build(CreateCloudInstanceBO createCloudInstance, ServerBO serverBO) {
        CreateInstanceRequest createInstanceRequest = new CreateInstanceRequest();
        createInstanceRequest.setSysRegionId(createCloudInstance.getCloudInstanceTemplate().getRegionId());
        // 实例所属的可用区编号，空表示由系统选择，默认值：空。
        createInstanceRequest.setZoneId(createCloudInstance.getCreateCloudInstance().getZoneId());
        // 镜像文件ID
        createInstanceRequest.setImageId(createCloudInstance.getCreateCloudInstance().getImageId());
        // 实例规格
        createInstanceRequest.setInstanceType(
                createCloudInstance
                        .getCloudInstanceTemplate()
                        .getInstanceTemplate()
                        .getInstance()
                        .getTypeId());
        // 配置实例VPC安全组
        createInstanceRequest.setSecurityGroupId(createCloudInstance.getCreateCloudInstance().getSecurityGroupId());
        // 配置虚拟交换机
        createInstanceRequest.setVSwitchId(serverBO.getVswitchId());
        // 付费选项设置 PostPaid（默认）：按量付费。 PrePaid：包年包月。选择该类付费方式时，您必须确认自己的账号支持余额支付/信用支付，否则将返回 InvalidPayMethod的错误提示。
        if (createCloudInstance.getCreateCloudInstance().getCharge().getChargeType()) {
            createInstanceRequest.setInstanceChargeType("PrePaid");
            // 包月时长 1,2,3,4,5,6,7,8,9,12,24,36
            createInstanceRequest.setPeriod(createCloudInstance.getCreateCloudInstance().getCharge().getPeriod());
            // 自动续费选项
            createInstanceRequest.setAutoRenew(createCloudInstance.getCreateCloudInstance().getCharge().getAutoRenew());
        }
        // 设置HostName
        createInstanceRequest.setHostName(serverBO.getHostname());
        // 设置IO优化实例配置  none：非I/O优化。optimized：I/O优化。
        if (!StringUtils.isEmpty(createCloudInstance.getCloudInstanceTemplate().getIoOptimized())) {
            createInstanceRequest.setIoOptimized(createCloudInstance.getCloudInstanceTemplate().getIoOptimized());
        } else {
            createInstanceRequest.setIoOptimized("optimized");
        }
        //createInstanceRequest.setPassword(passwd); // 设置系统密码
        // 网络计费类型，按流量计费还是按固定带宽计费。 PayByTraffic（默认）：按使用流量计费。
        // createInstanceRequest.setInternetChargeType("PayByTraffic");
        // 磁盘配置/系统盘
        CloudInstanceTemplateVO.DiskDetail sysDisk = createCloudInstance.getCreateCloudInstance().getDisk().getSysDisk();
        createInstanceRequest.setSystemDiskCategory(sysDisk.getCategory());
        // 系统盘容量必须大于镜像容量
        if (sysDisk.getSize() < createCloudInstance.getOcCloudImage().getImageSize()) {
            createInstanceRequest.setSystemDiskSize(createCloudInstance.getOcCloudImage().getImageSize());
        } else {
            createInstanceRequest.setSystemDiskSize(sysDisk.getSize());
        }
        // 磁盘配置/数据盘
        invokeDataDisk(createInstanceRequest, createCloudInstance.getCreateCloudInstance().getDisk().getDataDisk());
        return createInstanceRequest;
    }

    /**
     * 磁盘配置/数据盘
     *
     * @param createInstanceRequest
     * @param dataDisk
     */
    private static void invokeDataDisk(CreateInstanceRequest createInstanceRequest, CloudInstanceTemplateVO.DiskDetail dataDisk) {
        if (dataDisk.getSize() <= 0) return;
        CreateInstanceRequest.DataDisk disk = new CreateInstanceRequest.DataDisk();
        disk.setCategory(dataDisk.getCategory());
        disk.setSize(dataDisk.getSize());
        /**
         * 数据盘n的磁盘大小（n从1开始编号）。 以GB为单位，取值范围为：
         cloud：5 ~ 2000
         cloud_efficiency：20 ~ 32768
         cloud_ssd：20 ~ 32768
         ephemeral_ssd：5 ~ 800
         */
        createInstanceRequest.setDataDisks(Lists.newArrayList(disk));
    }


}
