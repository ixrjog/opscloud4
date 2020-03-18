package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.generator.OcCloudImage;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 9:14 上午
 * @Version 1.0
 */
public interface OcCloudImageService {

    List<OcCloudImage> queryOcCloudImageByType(int cloudType);
}
