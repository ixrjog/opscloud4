package com.baiyi.opscloud.aliyun.ons.impl;

import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.aliyun.ons.AliyunONSTopic;
import com.baiyi.opscloud.aliyun.ons.handler.AliyunONSTopicHandler;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import org.apache.logging.log4j.util.Strings;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 10:17 上午
 * @Since 1.0
 */

@Component("AliyunONSTopic")
public class AliyunONSTopicImpl implements AliyunONSTopic {

    @Resource
    private AliyunONSTopicHandler aliyunONSTopicHandler;

    @Override
    public List<OnsTopicListResponse.PublishInfoDo> queryOnsTopicList(AliyunONSParam.QueryTopicList param) {
        return aliyunONSTopicHandler.queryOnsTopicList(param);
    }

    @Override
    public List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> queryOnsTopicSubDetail(AliyunONSParam.QueryTopicSubDetail param) {
        return aliyunONSTopicHandler.queryOnsTopicSubDetail(param);
    }

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public Boolean onsTopicCreate(AliyunONSParam.TopicCreate param) {
        Boolean result = aliyunONSTopicHandler.onsTopicCreate(param);
        if (!result)
            throw new RuntimeException("创建Topic失败,retry");
        return true;
    }

    @Override
    public OnsTopicStatusResponse.Data queryOnsTopicStatus(AliyunONSParam.QueryTopicSubDetail param) {
        return aliyunONSTopicHandler.onsTopicStatus(param);
    }

    @Override
    public OnsTopicListResponse.PublishInfoDo queryOnsTopic(AliyunONSParam.QueryTopic param) {
        return aliyunONSTopicHandler.queryOnsTopic(param);
    }

    @Override
    public OnsMessagePageQueryByTopicResponse.MsgFoundDo onsMessagePageQuery(AliyunONSParam.QueryTopicSubDetail param) {
        return aliyunONSTopicHandler.onsMessagePageQueryByTopic(param);
    }

    @Override
    public OnsMessageGetByMsgIdResponse.Data queryOnsMessage(AliyunONSParam.QueryTopicMsg param) {
        return aliyunONSTopicHandler.onsMessageGetByMsgId(param);
    }

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 12, backoff = @Backoff(delay = 5000, multiplier = 1))
    public OnsTraceGetResultResponse.TraceData queryOnsTrace(AliyunONSParam.QueryTrace param) throws RuntimeException {
        String queryId = aliyunONSTopicHandler.onsTraceQueryByMsgId(param);
        if (Strings.isEmpty(queryId))
            return null;
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException ignored) {
        }
        OnsTraceGetResultResponse.TraceData traceData = aliyunONSTopicHandler.onsTraceGetResult(param.getRegionId(), queryId);
        if (!traceData.getStatus().equals("finish"))
            throw new RuntimeException("Topic消费轨迹查询中,retry");
        else
            return traceData;
    }
}

