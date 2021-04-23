package com.baiyi.opscloud.controller.helpdesk;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.helpdesk.HelpdeskReportParam;
import com.baiyi.opscloud.domain.vo.helpdesk.HelpdeskReportVO;
import com.baiyi.opscloud.facade.helpdesk.HelpdeskFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/4 3:24 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/helpdesk")
@Api(tags = "Helpdesk")
public class HelpdeskController {

    @Resource
    private HelpdeskFacade helpdeskFacade;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<HelpdeskReportVO.HelpdeskReport>> helpdeskReportPage(@RequestBody @Valid HelpdeskReportParam.PageQuery pageQuery) {
        return new HttpResult<>(helpdeskFacade.helpdeskReportPage(pageQuery));
    }

    @ApiOperation(value = "保存")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveHelpdeskReport(@RequestBody @Valid HelpdeskReportParam.SaveHelpdeskReport param) {
        return new HttpResult<>(helpdeskFacade.saveHelpdeskReport(param));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delHelpdeskReport(@RequestParam int id) {
        return new HttpResult<>(helpdeskFacade.delHelpdeskReport(id));
    }
}
