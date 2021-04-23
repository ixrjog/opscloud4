package com.baiyi.opscloud.decorator.export;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcExportTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.export.ExportVO;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 6:04 下午
 * @Since 1.0
 */
@Component
public class ExportTaskDecorator {

    @Resource
    private OcUserService ocUserService;

    public ExportVO.Task decoratorVO(OcExportTask ocExportTask) {
        ExportVO.Task task = BeanCopierUtils.copyProperties(ocExportTask, ExportVO.Task.class);
        OcUser ocUser = ocUserService.queryOcUserByUsername(ocExportTask.getUsername());
        if (ocUser != null)
            task.setOcUser(ocUser);
        return task;
    }

    public List<ExportVO.Task> decoratorVOList(List<OcExportTask> ocExportTaskList) {
        List<ExportVO.Task> taskList = Lists.newArrayListWithCapacity(ocExportTaskList.size());
        ocExportTaskList.forEach(ocItAsset -> taskList.add(decoratorVO(ocItAsset)));
        return taskList;
    }
}
