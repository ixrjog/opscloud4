package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceDeleteQueue;
import com.baiyi.opscloud.mapper.opscloud.OcCloudInstanceDeleteQueueMapper;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceDeleteQueueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xujian@gegejia.com">修远</a>
 * @Date 2020/8/19 10:18 AM
 * @Since 1.0
 */

@Service
public class OcCloudInstanceDeleteQueueServiceImpl implements OcCloudInstanceDeleteQueueService {

    @Resource
    private OcCloudInstanceDeleteQueueMapper ocCloudInstanceDeleteQueueMapper;

    @Override
    public void addOcCloudInstanceDeleteQueueList(List<OcCloudInstanceDeleteQueue> ocCloudInstanceDeleteQueueList) {
        ocCloudInstanceDeleteQueueMapper.insertList(ocCloudInstanceDeleteQueueList);
    }

    @Override
    public void updateOcCloudInstanceDeleteQueue(OcCloudInstanceDeleteQueue ocCloudInstanceDeleteQueue) {
        ocCloudInstanceDeleteQueueMapper.updateByPrimaryKey(ocCloudInstanceDeleteQueue);
    }
}
