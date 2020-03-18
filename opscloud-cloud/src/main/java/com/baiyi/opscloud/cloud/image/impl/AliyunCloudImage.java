package com.baiyi.opscloud.cloud.image.impl;

import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunImageHandler;
import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.common.base.CloudType;
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
        aliyunCore.getRegionIds();
        for (String regionId : aliyunCore.getRegionIds())
            aliyunImageHandler.getImageList(regionId, true);

        return null;
    }
}
