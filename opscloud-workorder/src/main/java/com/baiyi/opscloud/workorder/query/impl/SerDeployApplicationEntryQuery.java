package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTask;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.ser.SerDeployParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.ser.SerDeployTaskService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/6/14 17:44
 * @Version 1.0
 */
@Slf4j
@Component
public class SerDeployApplicationEntryQuery extends BaseTicketEntryQuery<SerDeployTask> {

    @Resource
    private SerDeployTaskService serDeployTaskService;


    @Override
    protected List<SerDeployTask> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        SerDeployParam.TaskPageQuery pageQuery = SerDeployParam.TaskPageQuery.builder()
                .applicationId(entryQuery.getApplicationId())
                .isActive(true)
                .extend(SimpleExtend.NOT_EXTEND.getExtend())
                .page(1)
                .length(entryQuery.getLength())
                .build();
        DataTable<SerDeployTask> dataTable = serDeployTaskService.queryPageByParam(pageQuery);
        return dataTable.getData();
    }

    @Override
    protected WorkOrderTicketVO.Entry<SerDeployTask> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, SerDeployTask entry) {
        return WorkOrderTicketVO.Entry.<SerDeployTask>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getTaskName())
                .entryKey(entry.getTaskName())
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .entry(entry)
                .comment(entry.getTaskDesc())
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SER_DEPLOY.name();
    }

}