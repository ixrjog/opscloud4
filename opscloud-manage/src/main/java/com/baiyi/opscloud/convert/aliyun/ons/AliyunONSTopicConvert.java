package com.baiyi.opscloud.convert.aliyun.ons;

import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/10 10:45 上午
 * @Since 1.0
 */
public class AliyunONSTopicConvert {

    private static AliyunONSVO.Topic toVO(OcAliyunOnsTopic ocAliyunOnsTopic) {
        AliyunONSVO.Topic topic = BeanCopierUtils.copyProperties(ocAliyunOnsTopic, AliyunONSVO.Topic.class);
        topic.setCreateTime(new Date(ocAliyunOnsTopic.getCreateTime()));
        return topic;
    }

    public static List<AliyunONSVO.Topic> toVOList(List<OcAliyunOnsTopic> ocAliyunOnsTopicList) {
        List<AliyunONSVO.Topic> topicList = Lists.newArrayListWithCapacity(ocAliyunOnsTopicList.size());
        ocAliyunOnsTopicList.forEach(ocAliyunOnsTopic -> topicList.add(toVO(ocAliyunOnsTopic)));
        return topicList;
    }

    private static AliyunONSVO.TopicSub toSubVO(OnsTopicSubDetailResponse.Data.SubscriptionDataListItem subscriptionDataListItem) {
        return BeanCopierUtils.copyProperties(subscriptionDataListItem, AliyunONSVO.TopicSub.class);
    }

    public static AliyunONSVO.TopicSubDetail toSubDetailVO
            (List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> subscriptionDataListItemList, OnsTopicStatusResponse.Data data) {
        AliyunONSVO.TopicSubDetail topicSubDetail = new AliyunONSVO.TopicSubDetail();
        List<AliyunONSVO.TopicSub> subList = Lists.newArrayListWithCapacity(subscriptionDataListItemList.size());
        subscriptionDataListItemList.forEach(topicSub -> subList.add(toSubVO(topicSub)));
        topicSubDetail.setSubList(subList);
        if (data != null) {
            topicSubDetail.setLastTimeStamp(new Date(data.getLastTimeStamp()));
            topicSubDetail.setPerm(data.getPerm());
            topicSubDetail.setTotalCount(data.getTotalCount());
        }
        return topicSubDetail;
    }

    public static AliyunONSVO.TopicMessage toMessageVO(OnsMessageGetByMsgIdResponse.Data data) {
        AliyunONSVO.TopicMessage message = BeanCopierUtils.copyProperties(data, AliyunONSVO.TopicMessage.class);
        if (data.getBornTimestamp() != null)
            message.setBornTime(TimeUtils.dateToStr(new Date(data.getBornTimestamp())));
        if (data.getStoreTimestamp() != null)
            message.setStoreTime(TimeUtils.dateToStr(new Date(data.getStoreTimestamp())));
        return message;
    }

    public static List<AliyunONSVO.TopicMessage> toMessageVOList(OnsMessagePageQueryByTopicResponse.MsgFoundDo data) {
        List<AliyunONSVO.TopicMessage> messageList = Lists.newArrayListWithCapacity(data.getMsgFoundList().size());
        data.getMsgFoundList().forEach(x -> messageList.add(toMessageVO(x)));
        return BeanCopierUtils.copyListProperties(messageList, AliyunONSVO.TopicMessage.class);
    }

    private static AliyunONSVO.TopicMessage toMessageVO(OnsMessagePageQueryByTopicResponse.MsgFoundDo.OnsRestMessageDo data) {
        AliyunONSVO.TopicMessage message = BeanCopierUtils.copyProperties(data, AliyunONSVO.TopicMessage.class);
        if (data.getBornTimestamp() != null)
            message.setBornTime(TimeUtils.dateToStr(new Date(data.getBornTimestamp())));
        if (data.getStoreTimestamp() != null)
            message.setStoreTime(TimeUtils.dateToStr(new Date(data.getStoreTimestamp())));
        return message;
    }
}