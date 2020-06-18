package com.baiyi.opscloud.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.bo.CloudInstanceTaskBO;
import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTask;
import com.baiyi.opscloud.domain.vo.user.UserVO;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:23 上午
 * @Version 1.0
 */
public class CloudInstanceTaskBuilder {

    public static OcCloudInstanceTask build(CreateCloudInstanceBO createCloudInstanceBO) {
        CloudInstanceTaskBO cloudInstanceTaskBO = CloudInstanceTaskBO.builder()
                .cloudType(createCloudInstanceBO.getCloudInstanceTemplate().getCloudType())
                .templateId(createCloudInstanceBO.getCloudInstanceTemplate().getId())
                .regionId(createCloudInstanceBO.getCloudInstanceTemplate().getRegionId())
                .createSize(createCloudInstanceBO.getCreateCloudInstance().getCreateSize())
                .taskPhase("PREPARATION")
                .taskStatus("")
                .createInstance(JSON.toJSONString(createCloudInstanceBO))
                .build();
        return covert(cloudInstanceTaskBO);
    }

    public static OcCloudInstanceTask build(CreateCloudInstanceBO createCloudInstanceBO, UserVO.User user) {
        OcCloudInstanceTask ocCloudInstanceTask = build(createCloudInstanceBO);
        ocCloudInstanceTask.setUserId(user.getId());
        ocCloudInstanceTask.setUserDetail(JSON.toJSONString(user));
        return ocCloudInstanceTask;
    }

    private static OcCloudInstanceTask covert(CloudInstanceTaskBO cloudInstanceTaskBO) {
        return BeanCopierUtils.copyProperties(cloudInstanceTaskBO, OcCloudInstanceTask.class);
    }
}
