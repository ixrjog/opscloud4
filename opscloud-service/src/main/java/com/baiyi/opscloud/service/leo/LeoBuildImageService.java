package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;

/**
 * @Author baiyi
 * @Date 2022/11/22 17:14
 * @Version 1.0
 */
public interface LeoBuildImageService {

    void add(LeoBuildImage leoBuildImage);

    void updateByPrimaryKeySelective(LeoBuildImage leoBuildImage);

    LeoBuildImage getByUniqueKey(int buildId, String image);

}
