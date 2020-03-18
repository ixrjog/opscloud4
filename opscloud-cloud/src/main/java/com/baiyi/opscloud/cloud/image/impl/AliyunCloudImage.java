package com.baiyi.opscloud.cloud.image.impl;

import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.common.base.CloudType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/3/17 6:38 下午
 * @Version 1.0
 */
@Slf4j
@Component("AliyunCloudImage")
public class AliyunCloudImage<T> extends BaseCloudImage<T> implements ICloudImage {

    @Override
    protected int getCloudImageType(){
        return CloudType.ALIYUN.getType();
    }
}
