package com.baiyi.opscloud.aliyun.ecs.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/1/13 7:09 下午
 * @Version 1.0
 */
@Data
public class ECSDisk implements Serializable {
    private static final long serialVersionUID = 4965201666099892715L;

    private String diskId;
    private String regionId;
    private String zoneId;
    private String diskName;
    private String description;
    private String type;
    private String category;
    private Integer size;
    private String imageId;
    private String sourceSnapshotId;
    private String autoSnapshotPolicyId;
    private String productCode;
    private Boolean portable;
    private String status;
    private String instanceId;
    private String device;
    private Boolean deleteWithInstance;
    private Boolean deleteAutoSnapshot;
    private Boolean enableAutoSnapshot;
    private Boolean enableAutomatedSnapshotPolicy;
    private String creationTime;
    private String attachedTime;
    private String detachedTime;
    private String diskChargeType;
    private String expiredTime;
    private String resourceGroupId;
    private Boolean encrypted;
    private String storageSetId;
    private Integer storageSetPartitionNumber;
    private Integer mountInstanceNum;
    private Integer iOPS;
    private Integer iOPSRead;
    private Integer iOPSWrite;
    private String kMSKeyId;
    private String performanceLevel;
    private String bdfId;
    //private List<DescribeDisksResponse.Disk.OperationLock> operationLocks;
    //private List<DescribeDisksResponse.Disk.MountInstance> mountInstances;
    //private List<DescribeDisksResponse.Disk.Tag> tags;
}
