package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/6/14 17:44
 * @Version 1.0
 */
@Slf4j
@Component
public class SerDeployApplicationEntryQuery extends BaseTicketEntryQuery<Application> {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private TagService tagService;

    private static final String SER_TAG = "Ser";

    @Override
    protected List<Application> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        Tag tag = tagService.getByTagKey(SER_TAG);
        // 标签不存在
        if (tag == null) {
            return Collections.emptyList();
        }
        ApplicationParam.ApplicationPageQuery pageQuery = ApplicationParam.ApplicationPageQuery.builder()
                .queryName(entryQuery.getQueryName())
                .extend(false)
                .tagId(tag.getId())
                .page(1)
                .length(entryQuery.getLength())
                .build();
        DataTable<Application> dataTable = applicationService.queryPageByParam(pageQuery);
        return dataTable.getData();
    }

    @Override
    protected WorkOrderTicketVO.Entry<Application> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, Application entry) {
        return WorkOrderTicketVO.Entry.<Application>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getName())
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .entry(entry)
                .comment(entry.getComment())
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SER_DEPLOY.name();
    }

}
