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
import com.baiyi.opscloud.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.facade.gitlab.GitlabFacade;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.entry.GitlabGroupMemberEntry;
import com.baiyi.opscloud.factory.ticket.entry.ITicketEntry;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/23 3:12 下午
 * @Since 1.0
 */

@Component
public class TicketGitlabGroupMemberHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private GitlabFacade gitlabFacade;

    @Resource
    private OcWorkorderTicketService ocWorkorderTicketService;

    @Override
    public String getKey() {
        return WorkorderKey.GITLAB_GROUP_MEMBER.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.GITLAB_GROUP_MEMBER.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object ticketEntry) {
        return new ObjectMapper().convertValue(ticketEntry, GitlabGroupMemberEntry.class);
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        GitlabGroupParam.AddMember addMember = JSONObject.parseObject(ocWorkorderTicketEntry.getEntryDetail(),  GitlabGroupParam.AddMember.class);
        GitlabGroupMemberEntry entry = new GitlabGroupMemberEntry();
        entry.setAddMember(addMember);
        return (T) entry;
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        GitlabGroupMemberEntry groupEntry = (GitlabGroupMemberEntry) entry;
        BusinessWrapper<Boolean> wrapper = gitlabFacade.addGitlabGroupMember(groupEntry.getAddMember());
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
        GitlabGroupParam.AddMember addMember = JSON.parseObject(JSON.toJSONString(entry.getTicketEntry()), GitlabGroupParam.AddMember.class);
        OcWorkorderTicketEntry newOcWorkorderTicketEntry = BeanCopierUtils.copyProperties(entry, OcWorkorderTicketEntry.class);
        newOcWorkorderTicketEntry.setEntryDetail(JSON.toJSONString(addMember));
        newOcWorkorderTicketEntry.setEntryStatus(0);
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
