package com.baiyi.opscloud.aliyun.ons.impl;

import com.aliyuncs.ons.model.v20190214.OnsInstanceBaseInfoResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.aliyun.ons.AliyunONSInstance;
import com.baiyi.opscloud.aliyun.ons.handler.AliyunONSInstanceHandler;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 10:53 上午
 * @Since 1.0
 */

@Component("AliyunONSInstance")
public class AliyunONSInstanceImpl implements AliyunONSInstance {

    @Resource
    private AliyunONSInstanceHandler aliyunONSInstanceHandler;

    @Override
    public List<OnsInstanceInServiceListResponse.InstanceVO> queryInstanceList(String regionId) {
        return aliyunONSInstanceHandler.queryInstanceList(regionId);
    }

    @Override
    public OnsInstanceBaseInfoResponse.InstanceBaseInfo queryInstanceDetail(AliyunONSParam.QueryInstanceDetail param) {
        return aliyunONSInstanceHandler.queryInstanceDetail(param);
    }
}

