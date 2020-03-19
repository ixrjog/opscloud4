package com.baiyi.opscloud.cloud.image.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunImageHandler;
import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.cloud.image.builder.OcCloudImageBuilder;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcCloudImage;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/17 6:38 下午
 * @Version 1.0
 */
@Slf4j
@Component("AliyunCloudImage")
public class AliyunCloudImage<T> extends BaseCloudImage<T> implements ICloudImage {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunImageHandler aliyunImageHandler;

    @Override
    protected int getCloudType() {
        return CloudType.ALIYUN.getType();
    }

    @Override
    protected List<T> getCloudImageList() {
        List<DescribeImagesResponse.Image> cloudImageList = Lists.newArrayList();
        for (String regionId : aliyunCore.getRegionIds())
            cloudImageList.addAll(aliyunImageHandler.getImageList(regionId, true));
        return (List<T>) cloudImageList;
    }

    @Override
    protected String getImageId(T cloudImage) throws Exception {
        if (!(cloudImage instanceof DescribeImagesResponse.Image)) throw new Exception();
        DescribeImagesResponse.Image image = (DescribeImagesResponse.Image) cloudImage;
        return image.getImageId();
    }

    @Override
    protected OcCloudImage getCloudImage(CloudAccount account, T cloudImage) {
        if (!(cloudImage instanceof DescribeImagesResponse.Image)) return null;
        DescribeImagesResponse.Image image = (DescribeImagesResponse.Image) cloudImage;
        return OcCloudImageBuilder.build(account, image, getCloudImageDetail(cloudImage));
    }

    @Override
    protected CloudAccount getCloudAccount() {
        AliyunAccount account = aliyunCore.getAccount();
        if (account == null) return null;
        return BeanCopierUtils.copyProperties(account, CloudAccount.class);
    }
}
