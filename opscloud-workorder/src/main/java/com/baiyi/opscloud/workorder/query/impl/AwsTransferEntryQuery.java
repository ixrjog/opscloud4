package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.AwsTransferCreateUserEntry;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/10/9 17:55
 * @Version 1.0
 */
@Component
public class AwsTransferEntryQuery extends BaseTicketEntryQuery<AwsTransferCreateUserEntry.TransferUser> {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.AWS_TRANSFER_CREATE_USER.name();
    }

    @Override
    protected List<AwsTransferCreateUserEntry.TransferUser> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return Lists.newArrayList(AwsTransferCreateUserEntry.TransferUser.NEW);
    }

    @Override
    protected WorkOrderTicketVO.Entry<AwsTransferCreateUserEntry.TransferUser> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, AwsTransferCreateUserEntry.TransferUser entry) {
        final String comment = Joiner.on("@").skipNulls().join(entry.getUserName(), "s-6d384561f0da4b148.server.transfer.eu-west-1.amazonaws.com");
        return WorkOrderTicketVO.Entry.<AwsTransferCreateUserEntry.TransferUser>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getUserName())
                .entryKey(entry.getUserName())
                .instanceUuid("")
                .businessType(0)
                .businessId(0)
                .content(JSONUtil.writeValueAsString(entry))
                .entry(entry)
                .comment(comment)
                .build();
    }

}