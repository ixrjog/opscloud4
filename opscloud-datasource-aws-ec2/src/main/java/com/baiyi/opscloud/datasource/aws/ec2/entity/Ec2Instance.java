package com.baiyi.opscloud.datasource.aws.ec2.entity;

import com.amazonaws.internal.SdkInternalList;
import com.amazonaws.services.ec2.model.*;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
import com.baiyi.opscloud.datasource.aws.ec2.util.AmazonEc2Util;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/1/24 6:27 PM
 * @Version 1.0
 */
public class Ec2Instance {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Instance implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -5709404012386189803L;

        private String regionId;

        private InstanceModel.EC2InstanceType ec2InstanceType;

        private Integer amiLaunchIndex;
        private String imageId;
        private String instanceId;
        private String instanceType;
        private String kernelId;
        private String keyName;
        private Date launchTime;
        private Monitoring monitoring;
        private Placement placement;
        private String platform;
        private String privateDnsName;
        private String privateIpAddress;
        // private SdkInternalList<ProductCode> productCodes;
        private String publicDnsName;
        private String publicIpAddress;
        private String ramdiskId;
        private InstanceState state;
        private String stateTransitionReason;
        private String subnetId;
        private String vpcId;
        private String architecture;
        // private SdkInternalList<InstanceBlockDeviceMapping> blockDeviceMappings;
        private String clientToken;
        private Boolean ebsOptimized;
        private Boolean enaSupport;
        private String hypervisor;
        private IamInstanceProfile iamInstanceProfile;
        private String instanceLifecycle;
        // private SdkInternalList<ElasticGpuAssociation> elasticGpuAssociations;
        // private SdkInternalList<ElasticInferenceAcceleratorAssociation> elasticInferenceAcceleratorAssociations;
        // private SdkInternalList<InstanceNetworkInterface> networkInterfaces;
        private String outpostArn;
        private String rootDeviceName;
        private String rootDeviceType;
        private SdkInternalList<GroupIdentifier> securityGroups;
        private Boolean sourceDestCheck;
        private String spotInstanceRequestId;
        private String sriovNetSupport;
        private StateReason stateReason;
        private SdkInternalList<Tag> tags;
        private String virtualizationType;
        private CpuOptions cpuOptions;
        private String capacityReservationId;
        // private CapacityReservationSpecificationResponse capacityReservationSpecification;
        //  private HibernationOptions hibernationOptions;
        // private SdkInternalList<LicenseConfiguration> licenses;
        //  private InstanceMetadataOptionsResponse metadataOptions;
        // private EnclaveOptions enclaveOptions;
        private String bootMode;
        private String platformDetails;
        private String usageOperation;
        private Date usageOperationUpdateTime;
        // private PrivateDnsNameOptionsResponse privateDnsNameOptions;
        private String ipv6Address;


        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(AmazonEc2Util.getInstanceName(this.getTags()))
                    .assetKey(this.privateIpAddress)
                    .assetKey2(this.publicIpAddress)
                    .kind(this.instanceType)
                    .assetType(DsAssetTypeConstants.EC2.name())
                    .regionId(this.regionId)
                    .createdTime(this.getLaunchTime())
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("platformDetails", this.platformDetails)
                    .paramProperty("imageId", this.imageId)
                    .paramProperty("cpu", this.getCpuOptions().getCoreCount() * this.getCpuOptions().getThreadsPerCore())
                    .paramProperty("memory", getEc2InstanceType() != null ? getEc2InstanceType().acqMemory() : 0)
                    .build();
        }
    }

}
