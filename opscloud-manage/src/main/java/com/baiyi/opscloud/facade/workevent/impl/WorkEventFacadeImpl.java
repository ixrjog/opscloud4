package com.baiyi.opscloud.facade.workevent.impl;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventReportVO;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.facade.workevent.WorkEventFacade;
import com.baiyi.opscloud.packer.workevent.WorkEventPacker;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workevent.WorkEventPropertyService;
import com.baiyi.opscloud.service.workevent.WorkEventService;
import com.baiyi.opscloud.service.workevent.WorkItemService;
import com.baiyi.opscloud.service.workevent.WorkRoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2022/8/12 10:23 AM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
@BusinessType(BusinessTypeEnum.WORK_EVENT)
public class WorkEventFacadeImpl implements WorkEventFacade {

    private final WorkEventService workEventService;

    private final WorkItemService workItemService;

    private final WorkRoleService workRoleService;

    private final WorkEventPacker workEventPacker;

    private final BusinessTagService businessTagService;

    private final TagService tagService;

    private final WorkEventPropertyService workEventPropertyService;

    private final UserService userService;

    private final UserPermissionFacade userPermissionFacade;

    private final static Integer ROOT_PARENT_ID = 0;

    @Override
    public DataTable<WorkEventVO.WorkEvent> queryWorkEventPage(WorkEventParam.WorkEventPageQuery pageQuery) {
        DataTable<WorkEvent> table = workEventService.queryPageByParam(pageQuery);
        List<WorkEventVO.WorkEvent> data = BeanCopierUtil.copyListProperties(table.getData(), WorkEventVO.WorkEvent.class)
                .stream()
                .peek(e -> workEventPacker.wrap(e, SimpleExtend.EXTEND))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addWorkEvent(WorkEventParam.AddWorkEvent param) {
        param.getWorkEventList().forEach(workEventVO -> {
            WorkEvent workEvent = BeanCopierUtil.copyProperties(workEventVO, WorkEvent.class);
            workEvent.setUsername(SessionHolder.getUsername());
            workEventService.add(workEvent);
            List<WorkEventProperty> propertyList = workEventVO.getPropertyList();
            if (!CollectionUtils.isEmpty(propertyList)) {
                workEventPropertyService.addList(propertyList
                        .stream()
                        .peek(property -> property.setWorkEventId(workEvent.getId()))
                        .collect(Collectors.toList()));
            }
        });
    }

    private void validAccess(WorkEvent workEvent) {
        String username = SessionHolder.getUsername();
        int accessLevel = userPermissionFacade.getUserAccessLevel(username);
        // ADMIN角色可以操作所有
        if (accessLevel >= AccessLevel.ADMIN.getLevel()) {
            return;
        }
        if (SessionHolder.equalsUsername(workEvent.getUsername())) {
            return;
        }
        throw new OCException("只能变更自己创建的工作事件");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateWorkEvent(WorkEventParam.UpdateWorkEvent param) {
        WorkEvent workEvent = BeanCopierUtil.copyProperties(param.getWorkEvent(), WorkEvent.class);
        validAccess(workEvent);
        workEventService.update(workEvent);
        workEventPropertyService.deleteByWorkEventId(workEvent.getId());
        Map<String, String> map = param.getWorkEvent().getProperty();
        if (!CollectionUtils.isEmpty(map)) {
            List<WorkEventProperty> propertyList = Lists.newArrayList();
            map.forEach((k, v) ->
                    propertyList.add(WorkEventProperty.builder()
                            .workEventId(workEvent.getId())
                            .name(k)
                            .value(v)
                            .build())
            );
            workEventPropertyService.addList(propertyList);
        }
    }

    @Override
    @TagClear
    @Transactional(rollbackFor = {Exception.class})
    public void deleteWorkEvent(Integer id) {
        WorkEvent workEvent = workEventService.getById(id);
        if (ObjectUtils.isEmpty(workEvent)) {
            return;
        }
        validAccess(workEvent);
        workEventPropertyService.deleteByWorkEventId(id);
        workEventService.deleteById(id);
    }

    @Override
    public List<WorkRole> queryWorkRole() {
        return workRoleService.queryAll();
    }

    @Override
    public List<WorkRole> queryMyWorkRole() {
        User user = userService.getByUsername(SessionHolder.getUsername());
        if (ObjectUtils.isEmpty(user)) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_FAILURE);
        }
        List<Tag> tags = businessTagService.queryByBusiness(
                        SimpleBusiness.builder()
                                .businessType(BusinessTypeEnum.USER.getType())
                                .businessId(user.getId())
                                .build())
                .stream()
                .map(e -> tagService.getById(e.getTagId()))
                .toList();
        List<WorkRole> workRoles = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(tags)) {
            tags.forEach(tag -> {
                WorkRole workRole = workRoleService.getByTag(tag.getTagKey());
                if (!ObjectUtils.isEmpty(workRole)) {
                    workRoles.add(workRole);
                }
            });
        }
        return CollectionUtils.isEmpty(workRoles) ? workRoleService.queryAll() : workRoles;
    }

    @Override
    public WorkRole getWorkRoleById(Integer workRoleId) {
        return workRoleService.getById(workRoleId);
    }

    @Override
    public List<WorkItem> listWorkItem(WorkEventParam.WorkItemQuery query) {
        return workItemService.listByWorkRoleIdAndParentId(query.getWorkRoleId(), query.getParentId());
    }

    @Override
    public WorkEventVO.Item getWorkItemById(Integer workItemId) {
        WorkItem workItem = workItemService.getById(workItemId);
        return BeanCopierUtil.copyProperties(workItem, WorkEventVO.Item.class);
    }

    @Override
    public List<TreeVO.Tree> queryWorkItemTree(WorkEventParam.WorkItemTreeQuery param) {
        if (!param.getWorkRoleId().equals(-1)) {
            WorkRole workRole = workRoleService.getById(param.getWorkRoleId());
            return Lists.newArrayList(buildTree(workRole));
        }
        List<WorkRole> workRoleList = queryWorkRole();
        List<TreeVO.Tree> tree = Lists.newArrayListWithCapacity(workRoleList.size());
        workRoleList.forEach(workRole -> tree.add(buildTree(workRole)));
        return tree;
    }

    private TreeVO.Tree buildTree(WorkRole workRole) {
        return TreeVO.Tree.builder().label(workRole.getWorkRoleName()).children(getChildTree(workRole.getId(), ROOT_PARENT_ID)).build();
    }

    private List<TreeVO.Tree> getChildTree(Integer workRoleId, Integer parentId) {
        List<WorkItem> workItemList = workItemService.listByWorkRoleIdAndParentId(workRoleId, parentId);
        if (CollectionUtils.isEmpty(workItemList)) {
            return Collections.emptyList();
        }
        List<TreeVO.Tree> childDeptTreeList = Lists.newArrayListWithCapacity(workItemList.size());
        workItemList.forEach(child -> {
            List<TreeVO.Tree> list = getChildTree(child.getWorkRoleId(), child.getId());
            TreeVO.Tree childDeptTree = TreeVO.Tree.builder().label(child.getWorkItemName()).value(child.getId()).build();
            if (!CollectionUtils.isEmpty(list)) {
                childDeptTree.setChildren(list);
            }
            childDeptTreeList.add(childDeptTree);
        });
        return childDeptTreeList;
    }

    @Override
    public WorkEventReportVO.WeeklyReport getWorkEventWeeklyReport(Integer workRoleId) {
        List<String> weeks = workEventService.queryWeek(workRoleId).stream().map(ReportVO.Report::getCName).collect(Collectors.toList());
        List<WorkItem> workItemList = queryChildItem(workRoleId);
        Map<String, WorkEventReportVO.WeeklyStatistics> map = Maps.newHashMapWithExpectedSize(workItemList.size());
        workItemList.forEach(workItem -> {
            List<Integer> value = workEventService.queryWeekByItem(workRoleId, workItem.getId()).stream()
                    .map(ReportVO.Report::getValue).collect(Collectors.toList());
            WorkEventReportVO.WeeklyStatistics weeklyStatistics = WorkEventReportVO.WeeklyStatistics.builder()
                    .color(workItem.getColor())
                    .values(value)
                    .build();
            map.put(workItem.getWorkItemName(), weeklyStatistics);
        });
        return WorkEventReportVO.WeeklyReport.builder()
                .weeks(weeks)
                .valueMap(map)
                .build();
    }

    private List<WorkItem> queryChildItem(Integer workRoleId) {
        List<WorkItem> workItemList = workItemService.listByWorkRoleId(workRoleId);
        return workItemList.stream()
                .filter(e -> CollectionUtils.isEmpty(workItemService.listByWorkRoleIdAndParentId(workRoleId, e.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkEventReportVO.ItemReport> getWorkEventItemReport(Integer workRoleId) {
        List<ReportVO.Report> report = workEventService.getWorkEventItemReport(workRoleId);
        return BeanCopierUtil.copyListProperties(report, WorkEventReportVO.ItemReport.class);
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventTimeReport() {
        return workEventService.getWorkEventTimeReport();
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventInterceptReport() {
        return workEventService.getWorkEventInterceptReport();
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventSolveReport() {
        return workEventService.getWorkEventSolveReport();
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventFaultReport() {
        return workEventService.getWorkEventFaultReport();
    }
}
