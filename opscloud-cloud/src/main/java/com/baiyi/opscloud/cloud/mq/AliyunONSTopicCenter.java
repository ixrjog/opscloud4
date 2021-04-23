package com.baiyi.opscloud.cloud.mq;

import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 3:56 下午
 * @Since 1.0
 */
public interface AliyunONSTopicCenter {

    Boolean syncONSTopic(AliyunONSParam.QueryTopicList param);

    List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> queryOnsTopicSubDetail(AliyunONSParam.QueryTopicSubDetail parma);

    OnsTopicStatusResponse.Data queryOnsTopicStatus(AliyunONSParam.QueryTopicSubDetail param);

    Boolean onsTopicCreate(AliyunONSParam.TopicCreate param);

    Boolean saveTopic(AliyunONSParam.QueryTopic param);

    OnsMessagePageQueryByTopicResponse.MsgFoundDo onsMessagePageQuery(AliyunONSParam.QueryTopicSubDetail param);

    OnsMessageGetByMsgIdResponse.Data queryOnsMessage(AliyunONSParam.QueryTopicMsg param);

    OnsTraceGetResultResponse.TraceData queryOnsTrace(AliyunONSParam.QueryTrace param);
}
