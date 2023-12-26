package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/8/28 14:39
 * @Version 1.0
 */
@Component
public class ApplicationFinOpsEntryQuery extends BaseTicketEntryQuery<Application> {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private TagService tagService;

    @Override
    protected List<Application> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        ApplicationParam.ApplicationPageQuery pageQuery = ApplicationParam.ApplicationPageQuery.builder()
                .queryName(entryQuery.getQueryName())
                .extend(false)
                .page(1)
                .length(entryQuery.getLength())
                .build();
        DataTable<Application> dataTable = applicationService.queryPageByParam(pageQuery);
        return dataTable.getData();
    }

    @Override
    protected WorkOrderTicketVO.Entry<Application> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, Application entry) {
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(entry.getId())
                .build();

        // 清除所有FinOps tags
        String finOpsTag = "";
        for (BusinessTag e : businessTagService.queryByBusiness(query)) {
            Tag tag = tagService.getById(e.getTagId());
            if (tag.getTagKey().startsWith("$")) {
                finOpsTag = tag.getTagKey();
                break;
            }
        }
        return WorkOrderTicketVO.Entry.<Application>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getName())
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .entry(entry)
                .comment(entry.getComment())
                .role(finOpsTag)
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_FINOPS_TAG.name();
    }

}