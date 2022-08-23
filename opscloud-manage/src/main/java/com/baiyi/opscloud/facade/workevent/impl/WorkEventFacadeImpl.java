package com.baiyi.opscloud.facade.workevent.impl;

import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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

    private final static Integer ROOT_PARENT_ID = 0;

    @Override
    public DataTable<WorkEventVO.WorkEvent> queryPageByParam(WorkEventParam.PageQuery pageQuery) {
        DataTable<WorkEvent> table = workEventService.queryPageByParam(pageQuery);
        List<WorkEventVO.WorkEvent> data = BeanCopierUtil.copyListProperties(table.getData(), WorkEventVO.WorkEvent.class).stream().peek(e -> workEventPacker.wrap(e, SimpleExtend.EXTEND)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addWorkEvent(WorkEventParam.AddWorkEvent param) {
        param.getWorkEventList().forEach(workEventVO -> {
            WorkEvent workEvent = BeanCopierUtil.copyProperties(workEventVO, WorkEvent.class);
            workEvent.setUsername(SessionUtil.getUsername());
            workEventService.add(workEvent);
            List<WorkEventProperty> propertyList = workEventVO.getPropertyList();
            if (!CollectionUtils.isEmpty(propertyList)) {
                workEventPropertyService.addList(propertyList.stream().peek(property -> property.setWorkEventId(workEvent.getId())).collect(Collectors.toList()));
            }
        });
//        List<WorkEvent> list = BeanCopierUtil.copyListProperties(param.getWorkEventList(), WorkEvent.class).stream()
//                .peek(workEvent -> workEvent.setUsername(SessionUtil.getUsername()))
//                .collect(Collectors.toList());
//        workEventService.addList(list);
    }

    @Override
    public void updateWorkEvent(WorkEventParam.UpdateWorkEvent param) {
        WorkEvent workEvent = BeanCopierUtil.copyProperties(param.getWorkEvent(), WorkEvent.class);
        workEventService.update(workEvent);
    }

    @Override
    @TagClear
    @Transactional(rollbackFor = {Exception.class})
    public void deleteWorkEvent(Integer id) {
        workEventPropertyService.deleteByWorkEventId(id);
        workEventService.deleteById(id);
    }

    @Override
    public List<WorkRole> listWorkRole() {
        return workRoleService.queryAll();
    }

    @Override
    public List<WorkRole> queryMyWorkRole() {
        String username = SessionUtil.getUsername();
        User user = userService.getByUsername(username);
        if (user == null) throw new AuthRuntimeException(ErrorEnum.AUTHENTICATION_FAILURE);
        List<Tag> tags = businessTagService.queryByBusiness(SimpleBusiness.builder().businessType(BusinessTypeEnum.USER.getType())
                .businessId(user.getId())
                .build()
        ).stream().map(e -> tagService.getById(e.getTagId())).collect(Collectors.toList());

        List<WorkRole> workRoles = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(tags)) {
            for (Tag tag : tags) {
                WorkRole workRole = workRoleService.getByTag(tag.getTagKey());
                if (workRole != null) {
                    workRoles.add(workRole);
                }
            }
        }
        if (!CollectionUtils.isEmpty(workRoles))
            return workRoles;
        return workRoleService.queryAll();
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
    public WorkItem getWorkItemById(Integer workItemId) {
        return workItemService.getById(workItemId);
    }

    @Override
    public List<TreeVO.Tree> queryWorkItemTree(WorkEventParam.WorkItemTreeQuery param) {
        if (!param.getWorkRoleId().equals(-1)) {
            WorkRole workRole = workRoleService.getById(param.getWorkRoleId());
            return Lists.newArrayList(buildTree(workRole));
        }
        List<WorkRole> workRoleList = listWorkRole();
        List<TreeVO.Tree> tree = Lists.newArrayListWithCapacity(workRoleList.size());
        workRoleList.forEach(workRole -> tree.add(buildTree(workRole)));
        return tree;
    }

    private TreeVO.Tree buildTree(WorkRole workRole) {
        return TreeVO.Tree.builder().label(workRole.getWorkRoleName()).children(getChildTree(workRole.getId(), ROOT_PARENT_ID)).build();
    }

    private List<TreeVO.Tree> getChildTree(Integer workRoleId, Integer parentId) {
        List<WorkItem> workItemList = workItemService.listByWorkRoleIdAndParentId(workRoleId, parentId);
        if (CollectionUtils.isEmpty(workItemList)) return Collections.emptyList();
        List<TreeVO.Tree> childDeptTreeList = Lists.newArrayListWithCapacity(workItemList.size());
        workItemList.forEach(child -> {
            List<TreeVO.Tree> list = getChildTree(child.getWorkRoleId(), child.getId());
            TreeVO.Tree childDeptTree = TreeVO.Tree.builder().label(child.getWorkItemName()).value(child.getId()).build();
            if (!CollectionUtils.isEmpty(list)) childDeptTree.setChildren(list);
            childDeptTreeList.add(childDeptTree);
        });
        return childDeptTreeList;
    }
}
