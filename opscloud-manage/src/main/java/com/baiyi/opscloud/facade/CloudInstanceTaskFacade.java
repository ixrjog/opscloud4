package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTask;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTaskVO;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:40 上午
 * @Version 1.0
 */
public interface CloudInstanceTaskFacade {

    void doCreateInstanceTask(OcCloudInstanceTask ocCloudInstanceTask, CreateCloudInstanceBO createCloudInstanceBO);

    CloudInstanceTaskVO.CloudInstanceTask queryCloudInstanceTask(int taskId);

    CloudInstanceTaskVO.CloudInstanceTask queryLastCloudInstanceTask(int templateId);
}
