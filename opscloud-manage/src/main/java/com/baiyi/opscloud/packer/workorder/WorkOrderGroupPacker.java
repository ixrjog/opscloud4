package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/8 9:22 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderGroupPacker implements IWrapper<WorkOrderVO.Group> {

    private final WorkOrderService workOrderService;

    @Override
    public void wrap(WorkOrderVO.Group group, IExtend iExtend) {
        if (iExtend.getExtend()) {
            int workOrderSize = workOrderService.countByWorkOrderGroupId(group.getId());
            group.setWorkOrderSize(workOrderSize);
        }
    }

}
