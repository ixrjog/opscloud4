package com.baiyi.opscloud.aliyun.ons.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.OnsInstanceBaseInfoRequest;
import com.aliyuncs.ons.model.v20190214.OnsInstanceBaseInfoResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListRequest;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 10:50 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class AliyunONSInstanceHandler extends AliyunONSBaseHandler {

    public List<OnsInstanceInServiceListResponse.InstanceVO> queryInstanceList(String regionId) {
        IAcsClient client = getClient(regionId);
        OnsInstanceInServiceListRequest request = new OnsInstanceInServiceListRequest();
        try {
            OnsInstanceInServiceListResponse response = client.getAcsResponse(request);
            return response == null ? Collections.emptyList() : response.getData();
        } catch (ClientException e) {
            log.error("查询ONS实例列表失败", e);
            return Collections.emptyList();
        }
    }

    public OnsInstanceBaseInfoResponse.InstanceBaseInfo queryInstanceDetail(AliyunONSParam.QueryInstanceDetail param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsInstanceBaseInfoRequest request = new OnsInstanceBaseInfoRequest();
        request.setInstanceId(param.getInstanceId());
        try {
            OnsInstanceBaseInfoResponse response = client.getAcsResponse(request);
            return response.getInstanceBaseInfo();
        } catch (ClientException e) {
            log.error("查询ONS实例详情失败", e);
            return null;
        }
    }

}
