package com.baiyi.opscloud.cloud.mq.impl;

import com.aliyuncs.ons.model.v20190214.*;
import com.baiyi.opscloud.aliyun.ons.AliyunONSTopic;
import com.baiyi.opscloud.cloud.mq.AliyunONSTopicCenter;
import com.baiyi.opscloud.cloud.mq.builder.AliyunONSTopicBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsTopicService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 3:56 下午
 * @Since 1.0
 */

@Slf4j
@Component("AliyunONSTopicCenter")
public class AliyunONSTopicCenterImpl implements AliyunONSTopicCenter {

    @Resource
    private AliyunONSTopic aliyunONSTopic;

    @Resource
    private OcAliyunOnsTopicService ocAliyunOnsTopicService;

    @Override
    public Boolean syncONSTopic(AliyunONSParam.QueryTopicList param) {
        HashMap<String, OcAliyunOnsTopic> map = getTopicMap(param);
        List<OnsTopicListResponse.PublishInfoDo> instanceList = aliyunONSTopic.queryOnsTopicList(param);
        instanceList.forEach(topic -> {
            saveTopic(topic);
            map.remove(topic.getTopic());
        });
        delTopicByMap(map);
        return true;
    }

    private HashMap<String, OcAliyunOnsTopic> getTopicMap(AliyunONSParam.QueryTopicList param) {
        List<OcAliyunOnsTopic> topicList = ocAliyunOnsTopicService.queryOcAliyunOnsTopicByInstanceId(param.getInstanceId());
        HashMap<String, OcAliyunOnsTopic> map = Maps.newHashMap();
        topicList.forEach(topic -> map.put(topic.getTopic(), topic));
        return map;
    }

    private Boolean saveTopic(OnsTopicListResponse.PublishInfoDo topic) {
        OcAliyunOnsTopic ocAliyunOnsTopic = ocAliyunOnsTopicService.queryOcAliyunOnsTopicByInstanceIdAndTopic(topic.getInstanceId(), topic.getTopic());
        OcAliyunOnsTopic newOcAliyunOnsTopic = AliyunONSTopicBuilder.build(topic);
        if (ocAliyunOnsTopic == null) {
            try {
                ocAliyunOnsTopicService.addOcAliyunOnsTopic(newOcAliyunOnsTopic);
                return true;
            } catch (Exception e) {
                newOcAliyunOnsTopic.setId(ocAliyunOnsTopic.getId());
                ocAliyunOnsTopicService.updateOcAliyunOnsTopic(newOcAliyunOnsTopic);
                log.error("新增阿里云onsTopic信息失败，Topic:{}", topic.getTopic(), e);
                return false;
            }
        }
        return true;
    }

    private void delTopicByMap(HashMap<String, OcAliyunOnsTopic> map) {
        map.forEach((key, value) -> ocAliyunOnsTopicService.deleteOcAliyunOnsTopicById(value.getId()));
    }

    @Override
    public List<OnsTopicSubDetailResponse.Data.SubscriptionDataListItem> queryOnsTopicSubDetail(AliyunONSParam.QueryTopicSubDetail parma) {
        return aliyunONSTopic.queryOnsTopicSubDetail(parma);
    }

    @Override
    public OnsTopicStatusResponse.Data queryOnsTopicStatus(AliyunONSParam.QueryTopicSubDetail param) {
        return aliyunONSTopic.queryOnsTopicStatus(param);
    }

    @Override
    public Boolean onsTopicCreate(AliyunONSParam.TopicCreate param) {
        return aliyunONSTopic.onsTopicCreate(param);
    }

    @Override
    public Boolean saveTopic(AliyunONSParam.QueryTopic param) {
        OnsTopicListResponse.PublishInfoDo topic = aliyunONSTopic.queryOnsTopic(param);
        if (topic != null) {
            return saveTopic(topic);
        }
        return false;
    }

    @Override
    public OnsMessagePageQueryByTopicResponse.MsgFoundDo onsMessagePageQuery(AliyunONSParam.QueryTopicSubDetail param) {
        return aliyunONSTopic.onsMessagePageQuery(param);
    }

    @Override
    public OnsMessageGetByMsgIdResponse.Data queryOnsMessage(AliyunONSParam.QueryTopicMsg param) {
        return aliyunONSTopic.queryOnsMessage(param);
    }

    @Override
    public OnsTraceGetResultResponse.TraceData queryOnsTrace(AliyunONSParam.QueryTrace param) {
        try {
            return aliyunONSTopic.queryOnsTrace(param);
        } catch (Exception e) {
            return null;
        }
    }
}
