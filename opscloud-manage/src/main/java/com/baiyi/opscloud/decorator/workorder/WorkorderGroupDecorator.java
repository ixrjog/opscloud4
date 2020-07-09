package com.baiyi.opscloud.decorator.workorder;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderVO;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/28 3:01 下午
 * @Version 1.0
 */
@Component
public class WorkorderGroupDecorator {

    @Resource
    private OcWorkorderService ocWorkorderService;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private UserFacade userFacade;

    public WorkorderGroupVO.WorkorderGroup decorator(WorkorderGroupVO.WorkorderGroup workorderGroup) {
        int userAccessLevel = userPermissionFacade.getUserAccessLevel(userFacade.getOcUserBySession());
        List<OcWorkorder> workorderList = ocWorkorderService.queryOcWorkorderByGroupId(workorderGroup.getId(), userAccessLevel >= AccessLevel.ADMIN.getLevel());
        workorderGroup.setWorkorders(decorator(workorderList));
        return workorderGroup;
    }

    private List<WorkorderVO.Workorder> decorator(List<OcWorkorder> workorderList) {
       if(workorderList == null) return Lists.newArrayList();
       return workorderList.stream().map(e -> {
           WorkorderVO.Workorder workorder = BeanCopierUtils.copyProperties(e, WorkorderVO.Workorder.class);
           workorder.setReadme("// TODO");
           return workorder;
       } ).collect(Collectors.toList());
    }

}
