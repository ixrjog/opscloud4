package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/22 17:14
 * @Version 1.0
 */
public interface LeoBuildImageService {

    void add(LeoBuildImage leoBuildImage);

    void updateByPrimaryKeySelective(LeoBuildImage leoBuildImage);

    LeoBuildImage getByUniqueKey(int buildId, String image);

    LeoBuildImage getByImage(String image);

    LeoBuildImage findBuildImage(int jobId, String image);

    List<LeoBuildImage> queryImageWithJobIdAndImage(int jobId, String image);

}