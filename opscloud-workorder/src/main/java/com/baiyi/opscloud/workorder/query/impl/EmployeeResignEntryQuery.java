package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * 员工离职
 *
 * @Author baiyi
 * @Date 2022/3/2 9:37 AM
 * @Version 1.0
 */
@Component
public class EmployeeResignEntryQuery extends BaseTicketEntryQuery<User> {

    @Resource
    private UserService userService;

    @Override
    protected List<User> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        UserParam.EmployeeResignPageQuery pageQuery = UserParam.EmployeeResignPageQuery.builder()
                .queryName(entryQuery.getQueryName())
                .page(1)
                .length(entryQuery.getLength())
                .build();
        DataTable<User> dataTable = userService.queryPageByParam(pageQuery);
        return dataTable.getData();
    }

    @Override
    protected WorkOrderTicketVO.Entry<User> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, User entry) {
        return WorkOrderTicketVO.Entry.<User>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(toName(entry))
                .entryKey(entry.getUsername())
                .businessType(BusinessTypeEnum.USER.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .entry(entry)
                .comment(entry.getComment())
                .build();
    }

    private String toName(User user) {
        String name = StringUtils.isNotBlank(user.getName()) ? user.getName() : null;
        final String desc = Joiner.on(":").skipNulls().join(name, user.getDisplayName(), user.getEmail());
        return Joiner.on("").join(user.getUsername(), "<", desc, ">");
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SYS_EMPLOYEE_RESIGN.name();
    }

}