package com.baiyi.opscloud.controller.export;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.export.ExportParam;
import com.baiyi.opscloud.domain.vo.export.ExportVO;
import com.baiyi.opscloud.facade.export.ExportTaskFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 5:04 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/export/task")
@Api(tags = "导出任务管理")
public class ExportTaskController {

    @Resource
    private ExportTaskFacade exportTaskFacade;

    @ApiOperation(value = "导出任务分页查询")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ExportVO.Task>> queryOcExportTaskPage(@RequestBody ExportParam.PageQuery pageQuery) {
        return new HttpResult<>(exportTaskFacade.queryOcExportTaskPage(pageQuery));
    }

}
