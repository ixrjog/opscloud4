package com.baiyi.opscloud.factory.ticket.impl.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baiyi.opscloud.common.base.WorkorderKey;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.domain.vo.kafka.KafkaVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.facade.kafka.KafkaTopicFacade;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.entry.ITicketEntry;
import com.baiyi.opscloud.factory.ticket.entry.KafkaTopicEntry;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.gson.JsonSyntaxException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 3:32 下午
 * @Since 1.0
 */

@Component("TicketKafkaTopicHandler")
public class TicketKafkaTopicHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private KafkaTopicFacade kafkaTopicFacade;

    @Resource
    private OcWorkorderTicketService ocWorkorderTicketService;

    @Resource
    private OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Override
    public String getKey() {
        return WorkorderKey.KAFKA_TOPIC.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.KAFKA_TOPIC.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object ticketEntry) {
        return new ObjectMapper().convertValue(ticketEntry, KafkaTopicEntry.class);
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        KafkaParam.TopicCreate topic = JSONObject.parseObject(ocWorkorderTicketEntry.getEntryDetail(), KafkaParam.TopicCreate.class);
        KafkaTopicEntry entry = new KafkaTopicEntry();
        entry.setTopic(topic);
        return (T) entry;
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        KafkaTopicEntry topicEntry = (KafkaTopicEntry) entry;
        BusinessWrapper<Boolean> wrapper = kafkaTopicFacade.kafkaTopicCreate(topicEntry.getTopic());
        saveTicketEntry(ocWorkorderTicketEntry, wrapper);
    }

    @Override
    protected BusinessWrapper<Boolean> updateTicketEntry(WorkorderTicketEntryVO.Entry entry) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addTicketEntry(OcUser ocUser, WorkorderTicketEntryVO.Entry entry) {
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(entry.getWorkorderTicketId());
        if (!ocWorkorderTicket.getUsername().equals(SessionUtils.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        KafkaParam.TopicCreate topicCreate = JSON.parseObject(JSON.toJSONString(entry.getTicketEntry()), KafkaParam.TopicCreate.class);
        KafkaParam.TopicQuery topicQuery = new KafkaParam.TopicQuery();
        topicQuery.setInstanceName(topicCreate.getInstanceName());
        topicQuery.setTopic(topicCreate.getTopic());
        BusinessWrapper<KafkaVO.Topic> wrapper = kafkaTopicFacade.kafkaTopicQuery(topicQuery);
        if (wrapper.isSuccess()) {
            return new BusinessWrapper<>(ErrorEnum.KAFKA_TOPIC_EXIST);
        }
        OcWorkorderTicketEntry newOcWorkorderTicketEntry = BeanCopierUtils.copyProperties(entry, OcWorkorderTicketEntry.class);
        newOcWorkorderTicketEntry.setEntryDetail(JSON.toJSONString(topicCreate));
        newOcWorkorderTicketEntry.setEntryStatus(0);
        String comment = Joiner.on("-").join(topicCreate.getRemark(), topicCreate.getInstanceName());
        newOcWorkorderTicketEntry.setComment(comment);
        OcWorkorderTicketEntry ocWorkorderTicketEntry =
                ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryByTicketIdAndBusinessId(entry.getWorkorderTicketId(), entry.getBusinessId());
        if (ocWorkorderTicketEntry == null) {
            ocWorkorderTicketEntryService.addOcWorkorderTicketEntry(newOcWorkorderTicketEntry);
        } else {
            newOcWorkorderTicketEntry.setId(ocWorkorderTicketEntry.getId());
            ocWorkorderTicketEntryService.updateOcWorkorderTicketEntry(newOcWorkorderTicketEntry);
        }
        return BusinessWrapper.SUCCESS;

    }
}
