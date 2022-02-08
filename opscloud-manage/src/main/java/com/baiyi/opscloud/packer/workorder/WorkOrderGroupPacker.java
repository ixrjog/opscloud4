package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
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
public class WorkOrderGroupPacker {

    private final WorkOrderService workOrderService;

    public WorkOrderVO.Group wrap(WorkOrderGroup workOrderGroup, IExtend iExtend) {
        WorkOrderVO.Group vo = BeanCopierUtil.copyProperties(workOrderGroup, WorkOrderVO.Group.class);
        if (iExtend.getExtend()) {
            int workOrderSize = workOrderService.countByWorkOrderGroupId(workOrderGroup.getId());
            vo.setWorkOrderSize(workOrderSize);
        }
        return vo;
    }
}
