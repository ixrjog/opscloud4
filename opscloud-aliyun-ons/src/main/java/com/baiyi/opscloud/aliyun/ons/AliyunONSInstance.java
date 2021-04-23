package com.baiyi.opscloud.aliyun.ons;

import com.aliyuncs.ons.model.v20190214.OnsInstanceBaseInfoResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 10:50 上午
 * @Since 1.0
 */
public interface AliyunONSInstance {

    List<OnsInstanceInServiceListResponse.InstanceVO> queryInstanceList(String regionId);

    OnsInstanceBaseInfoResponse.InstanceBaseInfo queryInstanceDetail(AliyunONSParam.QueryInstanceDetail param);
}
