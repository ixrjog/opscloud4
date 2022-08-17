package com.baiyi.opscloud.packer.workevent;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workevent.WorkItemService;
import com.baiyi.opscloud.service.workevent.WorkRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    private final UserService userService;

    @Override
    public void wrap(WorkEventVO.WorkEvent vo, IExtend iExtend) {
        if (!iExtend.getExtend()) return;
        vo.setWorkItem(workItemService.getById(vo.getWorkItemId()));
        vo.setWorkRole(workRoleService.getById(vo.getWorkRoleId()));
        vo.setUser(userService.getByUsername(vo.getUsername()));
    }
}
