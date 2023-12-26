package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/6/13 17:37
 * @Version 1.0
 */
@Component
public class ApolloReleaseApplicationEntryQuery extends BaseTicketEntryQuery<Application> {

    @Resource
    private ApplicationService applicationService;

    @Override
    protected List<Application> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        // 查询用户授权的
        UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery = UserBusinessPermissionParam.UserBusinessPermissionPageQuery
                .builder()
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .queryName(entryQuery.getQueryName())
                .userId(getUser().getId())
                .extend(false)
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
        return WorkOrderKeyConstants.APOLLO_RELEASE.name();
    }

}