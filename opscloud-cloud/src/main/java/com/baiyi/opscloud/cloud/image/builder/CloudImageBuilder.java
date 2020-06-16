package com.baiyi.opscloud.cloud.image.builder;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudImage;

/**
 * @Author baiyi
 * @Date 2020/3/18 10:00 上午
 * @Version 1.0
 */
public class CloudImageBuilder {

    /**
     * aliyun image
     *
     * @param image
     * @param imageDetail
     * @return
     */
    public static OcCloudImage build(CloudAccount account, DescribeImagesResponse.Image image, String imageDetail) {
        CloudImageBO bo = CloudImageBO.builder()
                .uid(account.getUid())
                .accountName(account.getName())
                .regionId(account.getRegionId())
                .imageId(image.getImageId())
                .imageName(image.getImageName())
                .imageSize(image.getSize())
                .cloudType(CloudType.ALIYUN.getType())
                .imageDetail(imageDetail)
                .imageOwnerAlias(image.getImageOwnerAlias())
                .isSupportCloudinit(image.getIsSupportCloudinit() ? 1 : 0)
                .isSupportioOptimized(image.getIsSupportIoOptimized() ? 1 : 0)
                .osName(image.getOSName())
                .osNameEn(image.getOSNameEn())
                .architecture(image.getArchitecture())
                .imageStatus(image.getStatus())
                .creationTime(TimeUtils.acqGmtDate(image.getCreationTime()))
                .osType(image.getOSType())
                .platform(image.getPlatform())
                .build();
        return covert(bo);
    }

    private static OcCloudImage covert(CloudImageBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcCloudImage.class);
    }

}
