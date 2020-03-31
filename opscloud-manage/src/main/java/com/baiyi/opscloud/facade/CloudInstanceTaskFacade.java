package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceTask;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTaskVO;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:40 上午
 * @Version 1.0
 */
public interface CloudInstanceTaskFacade {
    void doCreateInstanceTask(OcCloudInstanceTask ocCloudInstanceTask, CreateCloudInstanceBO createCloudInstanceBO);

    OcCloudInstanceTaskVO.CloudInstanceTask queryCloudInstanceTask(int taskId);

    OcCloudInstanceTaskVO.CloudInstanceTask queryLastCloudInstanceTask(int templateId);
}
