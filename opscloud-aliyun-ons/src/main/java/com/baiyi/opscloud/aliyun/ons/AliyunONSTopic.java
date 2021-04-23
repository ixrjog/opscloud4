package com.baiyi.opscloud.aliyun.ons;

import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 10:17 上午
 * @Since 1.0
 */
public interface AliyunONSTopic {

    List<OnsTopicListResponse.PublishInfoDo> queryOnsTopicList(AliyunONSParam.QueryTopicList param);

    List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> queryOnsTopicSubDetail(AliyunONSParam.QueryTopicSubDetail parma);

    Boolean onsTopicCreate(AliyunONSParam.TopicCreate param);

    OnsTopicStatusResponse.Data queryOnsTopicStatus(AliyunONSParam.QueryTopicSubDetail param);

    OnsTopicListResponse.PublishInfoDo queryOnsTopic(AliyunONSParam.QueryTopic param);

    OnsMessagePageQueryByTopicResponse.MsgFoundDo onsMessagePageQuery(AliyunONSParam.QueryTopicSubDetail param);

    OnsMessageGetByMsgIdResponse.Data queryOnsMessage(AliyunONSParam.QueryTopicMsg param);

    OnsTraceGetResultResponse.TraceData queryOnsTrace(AliyunONSParam.QueryTrace param) throws RuntimeException;
}
