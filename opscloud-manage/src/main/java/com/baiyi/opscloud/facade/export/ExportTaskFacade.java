package com.baiyi.opscloud.facade.export;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.export.ExportParam;
import com.baiyi.opscloud.domain.vo.export.ExportVO;


/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 2:00 下午
 * @Since 1.0
 */
public interface ExportTaskFacade {

    void exportAssetAll(String username);

    void exportAssetApplyAll(String username);

    DataTable<ExportVO.Task> queryOcExportTaskPage(ExportParam.PageQuery pageQuery);
}
