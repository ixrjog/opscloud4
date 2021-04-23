package com.baiyi.opscloud.cloud.mq.builder;

import com.aliyuncs.ons.model.v20190214.OnsTopicListResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 8:38 下午
 * @Since 1.0
 */
public class AliyunONSTopicBuilder {

    public static OcAliyunOnsTopic build(OnsTopicListResponse.PublishInfoDo topic) {
        OcAliyunOnsTopic ocAliyunOnsTopic = new OcAliyunOnsTopic();
        ocAliyunOnsTopic.setInstanceId(topic.getInstanceId());
        ocAliyunOnsTopic.setTopic(topic.getTopic());
        ocAliyunOnsTopic.setIndependentNaming(topic.getIndependentNaming());
        ocAliyunOnsTopic.setMessageType(topic.getMessageType());
        ocAliyunOnsTopic.setRelation(topic.getRelation());
        ocAliyunOnsTopic.setRelationName(topic.getRelationName());
        ocAliyunOnsTopic.setCreateTime(topic.getCreateTime());
        ocAliyunOnsTopic.setRemark(topic.getRemark());
        return ocAliyunOnsTopic;
    }
}
