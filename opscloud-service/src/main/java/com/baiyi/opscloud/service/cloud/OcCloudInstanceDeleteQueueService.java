package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceDeleteQueue;

import java.util.List;

/**
 * @Author <a href="mailto:xujian@gegejia.com">修远</a>
 * @Date 2020/8/19 10:17 AM
 * @Since 1.0
 */
public interface OcCloudInstanceDeleteQueueService {

    void addOcCloudInstanceDeleteQueueList(List<OcCloudInstanceDeleteQueue> ocCloudInstanceDeleteQueueList);

    void updateOcCloudInstanceDeleteQueue(OcCloudInstanceDeleteQueue ocCloudInstanceDeleteQueue);
}
