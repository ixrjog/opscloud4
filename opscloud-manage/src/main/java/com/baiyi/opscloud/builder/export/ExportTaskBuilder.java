package com.baiyi.opscloud.builder.export;

import com.baiyi.opscloud.domain.generator.opscloud.OcExportTask;

import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 2:40 下午
 * @Since 1.0
 */
public class ExportTaskBuilder {

    public static OcExportTask build(String username, String fileName, Integer taskType) {
        OcExportTask ocExportTask = new OcExportTask();
        ocExportTask.setFileName(fileName);
        ocExportTask.setTaskType(taskType);
        ocExportTask.setTaskStatus(1);
        ocExportTask.setUsername(username);
        ocExportTask.setStartTime(new Date());
        return ocExportTask;
    }
}
