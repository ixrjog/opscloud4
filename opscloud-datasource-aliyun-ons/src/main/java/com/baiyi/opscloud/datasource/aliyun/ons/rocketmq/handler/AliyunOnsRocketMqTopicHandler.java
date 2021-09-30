package com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.handler;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.OnsTopicListRequest;
import com.aliyuncs.ons.model.v20190214.OnsTopicListResponse;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.handler.AliyunHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/30 3:10 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunOnsRocketMqTopicHandler {

    @Resource
    private AliyunHandler aliyunHandler;

    public static final String QUERY_ALL_TOPIC = null;

    public List<OnsTopicListResponse.PublishInfoDo> listTopic(String regionId, DsAliyunConfig.Aliyun aliyun, String instanceId) {
        return listTopic(regionId, aliyun, instanceId, QUERY_ALL_TOPIC);
    }

    /**
     * https://help.aliyun.com/document_detail/29590.html
     * @param regionId
     * @param aliyun
     * @param instanceId 必选参数
     * @param topic
     * @return
     */
    public List<OnsTopicListResponse.PublishInfoDo> listTopic(String regionId, DsAliyunConfig.Aliyun aliyun, String instanceId, String topic) {
        OnsTopicListRequest request = new OnsTopicListRequest();
        request.setInstanceId(instanceId);
        if (StringUtils.isNotBlank(topic)) {
            request.setTopic(topic);
        }
        try {
            OnsTopicListResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, request);
            return response == null ? Collections.emptyList() : response.getData();
        } catch (ClientException e) {
            log.error("查询ONSTopic列表失败", e);
            return Collections.emptyList();
        }
    }

}
