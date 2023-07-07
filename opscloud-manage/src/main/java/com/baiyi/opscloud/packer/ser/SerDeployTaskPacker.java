package com.baiyi.opscloud.packer.ser;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.ser.SerDeploySubtaskService;
import com.baiyi.opscloud.service.ser.SerDeployTaskItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/12 4:09 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class SerDeployTaskPacker implements IWrapper<SerDeployVO.Task> {

    private final SerDeployTaskItemService serDeployTaskItemService;

    private final SerDeploySubtaskService serDeploySubtaskService;

    private final SerDeployTaskItemPacker serDeployTaskItemPacker;

    private final SerDeploySubtaskPacker serDeploySubtaskPacker;

    private final ApplicationService applicationService;

    @Override
    public void wrap(SerDeployVO.Task vo, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        Application application = applicationService.getById(vo.getApplicationId());
        vo.setApplication(application);
        List<SerDeployVO.TaskItem> taskItemList = serDeployTaskItemService.listBySerDeployTaskId(vo.getId()).stream()
                .map(taskItem -> {
                    SerDeployVO.TaskItem itemVO = BeanCopierUtil.copyProperties(taskItem, SerDeployVO.TaskItem.class);
                    serDeployTaskItemPacker.wrap(itemVO, iExtend);
                    return itemVO;
                }).toList();
        vo.setTaskItemList(taskItemList);
        vo.setTaskItemSize(taskItemList.size());
        List<SerDeployVO.SubTask> subTaskList = serDeploySubtaskService.listBySerDeployTaskId(vo.getId()).stream()
                .map(subtask -> {
                    SerDeployVO.SubTask subTaskVO = BeanCopierUtil.copyProperties(subtask, SerDeployVO.SubTask.class);
                    serDeploySubtaskPacker.wrap(subTaskVO, iExtend);
                    return subTaskVO;
                }).toList();
        vo.setSubTaskList(subTaskList);
        vo.setSubTaskSize(subTaskList.size());
    }
}
