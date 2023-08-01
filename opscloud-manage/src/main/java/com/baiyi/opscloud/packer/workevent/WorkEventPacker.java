package com.baiyi.opscloud.packer.workevent;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.WorkEventProperty;
import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.user.UserPacker;
import com.baiyi.opscloud.service.workevent.WorkEventPropertyService;
import com.baiyi.opscloud.service.workevent.WorkItemService;
import com.baiyi.opscloud.service.workevent.WorkRoleService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2022/8/15 11:11 AM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class WorkEventPacker implements IWrapper<WorkEventVO.WorkEvent> {

    private final WorkItemService workItemService;

    private final WorkRoleService workRoleService;

    private final UserPacker userPacker;

    private final WorkEventPropertyService workEventPropertyService;

    private final static Integer ROOT_PARENT_ID = 0;

    @Override
    @AgoWrapper(extend = true)
    @TagsWrapper
    public void wrap(WorkEventVO.WorkEvent vo, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        vo.setWorkItem(workItemService.getById(vo.getWorkItemId()));
        List<WorkItem> workItemList = Lists.newArrayList();
        WorkItem workItem = workItemService.getById(vo.getWorkItemId());
        while (!workItem.getParentId().equals(ROOT_PARENT_ID)) {
            workItemList.add(workItem);
            workItem = workItemService.getById(workItem.getParentId());
        }
        workItemList.add(workItem);
        List<String> list = workItemList.stream().map(WorkItem::getWorkItemName).collect(Collectors.toList());
        Collections.reverse(list);
        vo.setWorkItemTree(Joiner.on("/").join(list));
        vo.setWorkRole(workRoleService.getById(vo.getWorkRoleId()));
        userPacker.wrap(vo, SimpleExtend.NOT_EXTEND);
        List<WorkEventProperty> propertyList = workEventPropertyService.listByWorkEventId(vo.getId());
        if (!CollectionUtils.isEmpty(propertyList)) {
            vo.setPropertyList(propertyList);
            vo.setProperty(propertyList.stream()
                    .collect(Collectors.toMap(WorkEventProperty::getName, WorkEventProperty::getValue)));
            vo.setProperties(propertyList.stream().map(this::toProperty).collect(Collectors.toList()));
        }
    }

    private WorkEventVO.EventProperty toProperty(WorkEventProperty workEventProperty) {
        // 故障属性
        if (workEventProperty.getName().equals("fault")) {
            if (Boolean.parseBoolean(workEventProperty.getValue())) {
                return WorkEventVO.EventProperty.FAULT;
            }
        }
        // 拦截属性
        if (workEventProperty.getName().equals("intercept")) {
            if (Boolean.parseBoolean(workEventProperty.getValue())) {
                return WorkEventVO.EventProperty.INTERCEPT;
            } else {
                return WorkEventVO.EventProperty.NOT_INTERCEPT;
            }
        }
        // 处理中属性
        if (workEventProperty.getName().equals("solve")) {
            if (!Boolean.parseBoolean(workEventProperty.getValue())) {
                return WorkEventVO.EventProperty.SOLVE;
            }
        }
        // 不显示
        return WorkEventVO.EventProperty.NO_SHOW;
    }

}
