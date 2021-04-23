package com.baiyi.opscloud.decorator.workorder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsInstanceService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/11 5:57 下午
 * @Since 1.0
 */

@Component("WorkorderTicketEntryDecorator")
public class WorkorderTicketEntryDecorator {

    @Resource
    private OcAliyunOnsInstanceService ocAliyunOnsInstanceService;

    private WorkorderTicketEntryVO.AliyunONSEntry aliyunONSEntryDecorator(OcWorkorderTicketEntry ocWorkorderTicketEntry) {
        WorkorderTicketEntryVO.AliyunONSEntry aliyunONSEntry =
                BeanCopierUtils.copyProperties(ocWorkorderTicketEntry, WorkorderTicketEntryVO.AliyunONSEntry.class);
        OcAliyunOnsInstance ocAliyunOnsInstance = ocAliyunOnsInstanceService.queryOcAliyunOnsInstance(ocWorkorderTicketEntry.getBusinessId());
        aliyunONSEntry.setInstance(ocAliyunOnsInstance);
        aliyunONSEntry.setTicketEntry(JSON.parse(ocWorkorderTicketEntry.getEntryDetail()));
        return aliyunONSEntry;
    }

    public List<WorkorderTicketEntryVO.AliyunONSEntry> aliyunONSEntryListDecorator(List<OcWorkorderTicketEntry> ocWorkorderTicketEntries) {
        List<WorkorderTicketEntryVO.AliyunONSEntry> entryList = Lists.newArrayListWithCapacity(ocWorkorderTicketEntries.size());
        ocWorkorderTicketEntries.forEach(ocWorkorderTicketEntry -> entryList.add(aliyunONSEntryDecorator(ocWorkorderTicketEntry)));
        return entryList;
    }

    private WorkorderTicketEntryVO.Entry entryDecorator(OcWorkorderTicketEntry ocWorkorderTicketEntry) {
        WorkorderTicketEntryVO.Entry entry =
                BeanCopierUtils.copyProperties(ocWorkorderTicketEntry, WorkorderTicketEntryVO.Entry.class);
        entry.setTicketEntry(JSON.parse(ocWorkorderTicketEntry.getEntryDetail()));
        return entry;
    }

    public List<WorkorderTicketEntryVO.Entry> entryListDecorator(List<OcWorkorderTicketEntry> ocWorkorderTicketEntries) {
        List<WorkorderTicketEntryVO.Entry> entryList = Lists.newArrayListWithCapacity(ocWorkorderTicketEntries.size());
        ocWorkorderTicketEntries.forEach(ocWorkorderTicketEntry -> entryList.add(entryDecorator(ocWorkorderTicketEntry)));
        return entryList;
    }

}
