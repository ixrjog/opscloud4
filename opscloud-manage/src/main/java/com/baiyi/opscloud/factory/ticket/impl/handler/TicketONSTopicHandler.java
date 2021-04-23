package com.baiyi.opscloud.factory.ticket.impl.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baiyi.opscloud.common.base.WorkorderKey;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.facade.aliyun.AliyunONSFacade;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.entry.AliyunONSTopicEntry;
import com.baiyi.opscloud.factory.ticket.entry.ITicketEntry;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsInstanceService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/27 1:47 下午
 * @Since 1.0
 */

@Slf4j
@Component("TicketONSTopicExecutor")
public class TicketONSTopicHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private AliyunONSFacade aliyunONSFacade;

    @Resource
    private OcWorkorderTicketService ocWorkorderTicketService;

    @Resource
    private OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Resource
    private OcAliyunOnsInstanceService ocAliyunOnsInstanceService;

    @Override
    public String getKey() {
        return WorkorderKey.ALIYUN_ONS_TOPIC.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.ALIYUN_ONS_TOPIC.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object ticketEntry) {
        return new ObjectMapper().convertValue(ticketEntry, AliyunONSTopicEntry.class);
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        AliyunONSParam.TopicCreate topic = JSONObject.parseObject(ocWorkorderTicketEntry.getEntryDetail(), AliyunONSParam.TopicCreate.class);
        AliyunONSTopicEntry aliyunONSTopicEntry = new AliyunONSTopicEntry();
        aliyunONSTopicEntry.setTopic(topic);
        return (T) aliyunONSTopicEntry;
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        AliyunONSTopicEntry topicEntry = (AliyunONSTopicEntry) entry;
        BusinessWrapper<Boolean> wrapper = aliyunONSFacade.onsTopicCreate(topicEntry.getTopic());
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
        AliyunONSParam.TopicCreate topicCreate = JSON.parseObject(JSON.toJSONString(entry.getTicketEntry()), AliyunONSParam.TopicCreate.class);
        OcWorkorderTicketEntry newOcWorkorderTicketEntry = BeanCopierUtils.copyProperties(entry, OcWorkorderTicketEntry.class);
        newOcWorkorderTicketEntry.setEntryDetail(JSON.toJSONString(topicCreate));
        newOcWorkorderTicketEntry.setEntryStatus(0);
        OcAliyunOnsInstance instance = ocAliyunOnsInstanceService.queryOcAliyunOnsInstance(entry.getBusinessId());
        String comment = Joiner.on("-").join(topicCreate.getRemark(), instance.getEnvName());
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
