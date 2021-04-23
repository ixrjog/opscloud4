package com.baiyi.opscloud.service.export;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcExportTask;
import com.baiyi.opscloud.domain.param.export.ExportParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 11:44 上午
 * @Since 1.0
 */
public interface OcExportTaskService {

    void addOcExportTask(OcExportTask ocExportTask);

    void updateOcExportTask(OcExportTask ocExportTask);

    DataTable<OcExportTask> queryOcExportTaskPage(ExportParam.PageQuery pageQuery);

    List<OcExportTask> queryOcExportTaskByType(String username, Integer taskType);
}
