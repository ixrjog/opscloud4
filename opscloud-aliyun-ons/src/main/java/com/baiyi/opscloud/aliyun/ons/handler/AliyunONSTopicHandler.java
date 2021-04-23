package com.baiyi.opscloud.aliyun.ons.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 10:17 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class AliyunONSTopicHandler extends AliyunONSBaseHandler {

    private List<OnsTopicListResponse.PublishInfoDo> OnsTopicList(String regionId, String instanceId, String topic) {
        IAcsClient client = getClient(regionId);
        OnsTopicListRequest request = new OnsTopicListRequest();
        request.setInstanceId(instanceId);
        if (!Strings.isEmpty(topic))
            request.setTopic(topic);
        try {
            OnsTopicListResponse response = client.getAcsResponse(request);
            return response == null ? Collections.emptyList() : response.getData();
        } catch (ClientException e) {
            log.error("查询ONSTopic列表失败", e);
            return Collections.emptyList();
        }
    }

    public List<OnsTopicListResponse.PublishInfoDo> queryOnsTopicList(AliyunONSParam.QueryTopicList param) {
        return OnsTopicList(param.getRegionId(), param.getInstanceId(), null);
    }

    public OnsTopicListResponse.PublishInfoDo queryOnsTopic(AliyunONSParam.QueryTopic param) {
        List<OnsTopicListResponse.PublishInfoDo> topicList = OnsTopicList(param.getRegionId(), param.getInstanceId(), param.getTopic());
        if (CollectionUtils.isEmpty(topicList))
            return null;
        return topicList.get(0);

    }

    public List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> queryOnsTopicSubDetail(AliyunONSParam.QueryTopicSubDetail param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsTopicSubDetailRequest request = new OnsTopicSubDetailRequest();
        request.setInstanceId(param.getInstanceId());
        request.setTopic(param.getTopic());
        try {
            OnsTopicSubDetailResponse response = client.getAcsResponse(request);
            return response == null ? Collections.emptyList() : response.getData().getSubscriptionDataList();
        } catch (ClientException e) {
            log.error("查询ONSTopic订阅信息失败", e);
            return Collections.emptyList();
        }
    }

    public Boolean onsTopicCreate(AliyunONSParam.TopicCreate param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsTopicCreateRequest request = new OnsTopicCreateRequest();
        request.setInstanceId(param.getInstanceId());
        request.setMessageType(param.getMessageType());
        request.setTopic(param.getTopic());
        request.setRemark(param.getRemark());
        try {
            OnsTopicCreateResponse response = client.getAcsResponse(request);
            return response != null;
        } catch (ClientException e) {
            log.error("创建ONSTopic失败，topic:{}", param.getTopic(), e);
            return false;
        }
    }

    public OnsTopicStatusResponse.Data onsTopicStatus(AliyunONSParam.QueryTopicSubDetail param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsTopicStatusRequest request = new OnsTopicStatusRequest();
        request.setInstanceId(param.getInstanceId());
        request.setTopic(param.getTopic());
        try {
            OnsTopicStatusResponse response = client.getAcsResponse(request);
            return response == null ? null : response.getData();
        } catch (ClientException e) {
            log.error("查询ONSTopicStatus失败，topic:{}", param.getTopic(), e);
            return null;
        }
    }

    public OnsMessagePageQueryByTopicResponse.MsgFoundDo onsMessagePageQueryByTopic(AliyunONSParam.QueryTopicSubDetail param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsMessagePageQueryByTopicRequest request = new OnsMessagePageQueryByTopicRequest();
        request.setInstanceId(param.getInstanceId());
        request.setTopic(param.getTopic());
        long nowTime = new Date().getTime();
        request.setEndTime(nowTime);
        request.setBeginTime(nowTime - 60 * 60 * 1000);
        request.setCurrentPage(1);
        request.setPageSize(50);
        try {
            OnsMessagePageQueryByTopicResponse response = client.getAcsResponse(request);
            return response == null ? null : response.getMsgFoundDo();
        } catch (ClientException e) {
            log.error("分页查询指定时间段内该 Topic 内存在的所有消息失败，topic:{}", param.getTopic(), e);
            return null;
        }
    }

    public OnsMessageGetByMsgIdResponse.Data onsMessageGetByMsgId(AliyunONSParam.QueryTopicMsg param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsMessageGetByMsgIdRequest request = new OnsMessageGetByMsgIdRequest();
        request.setInstanceId(param.getInstanceId());
        request.setTopic(param.getTopic());
        request.setMsgId(param.getMsgId());
        try {
            OnsMessageGetByMsgIdResponse response = client.getAcsResponse(request);
            return response == null ? null : response.getData();
        } catch (ClientException e) {
            log.error("查询Topic消息失败，topic:{}, msgId:{}", param.getTopic(), param.getMsgId(), e);
            return null;
        }
    }

    public String onsTraceQueryByMsgId(AliyunONSParam.QueryTrace param) {
        IAcsClient client = getClient(param.getRegionId());
        OnsTraceQueryByMsgIdRequest request = new OnsTraceQueryByMsgIdRequest();
        request.setMsgId(param.getMsgId());
        request.setBeginTime(param.getBeginTime());
        request.setEndTime(param.getEndTime());
        request.setInstanceId(param.getInstanceId());
        request.setTopic(param.getTopic());
        try {
            OnsTraceQueryByMsgIdResponse response = client.getAcsResponse(request);
            return response == null ? Strings.EMPTY : response.getQueryId();
        } catch (ClientException e) {
            log.error("查询Topic消息轨迹失败，topic:{}, msgId:{}", param.getTopic(), param.getMsgId(), e);
            return null;
        }
    }

    public OnsTraceGetResultResponse.TraceData onsTraceGetResult(String regionId, String queryId) {
        IAcsClient client = getClient(regionId);
        OnsTraceGetResultRequest request = new OnsTraceGetResultRequest();
        request.setQueryId(queryId);
        try {
            OnsTraceGetResultResponse response = client.getAcsResponse(request);
            return response == null ? null : response.getTraceData();
        } catch (ClientException e) {
            log.error("查询Topic消息轨迹结果失", e);
            return null;
        }
    }
}
