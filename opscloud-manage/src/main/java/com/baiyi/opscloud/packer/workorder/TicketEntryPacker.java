package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/10 7:38 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TicketEntryPacker {

    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    private final DsInstancePacker dsInstancePacker;

    /**
     * 包装工单条目
     *
     * @param iTicketEntries
     */
    public void wrap(WorkOrderTicketVO.ITicketEntries iTicketEntries) {
        List<WorkOrderTicketEntry> ticketEntries = workOrderTicketEntryService.queryByWorkOrderTicketId(iTicketEntries.getTicketId());
        ITicketProcessor iTicketProcessor = WorkOrderTicketProcessorFactory.getByKey(iTicketEntries.getWorkOrderKey());
        // TODO 判空
        List<WorkOrderTicketVO.Entry> entries = ticketEntries.stream().map(e -> {
                    WorkOrderTicketVO.Entry entry = WorkOrderTicketVO.Entry.builder()
                            .entry(iTicketProcessor.toEntry(e.getContent()))
                            .id(e.getId())
                            .name(e.getName())
                            .workOrderTicketId(e.getWorkOrderTicketId())
                            .instanceUuid(e.getInstanceUuid())
                            .businessType(e.getBusinessType())
                            .businessId(e.getBusinessId())
                            .entryStatus(e.getEntryStatus())
                            .entryKey(e.getEntryKey())
                            .role(e.getRole())
                            .comment(e.getComment())
                            .result(e.getResult())
                            .build();
                     dsInstancePacker.wrap(entry);
                    return entry;
        }).collect(Collectors.toList());
        iTicketEntries.setTicketEntries(entries);
    }

}
