package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;
import com.baiyi.opscloud.facade.WorkorderFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:09 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/workorder")
@Api(tags = "工单")
public class WorkorderController {

    @Resource
    private WorkorderFacade workorderFacade;

    @ApiOperation(value = "分页工单组列表")
    @PostMapping(value = "/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcWorkorderGroupVO.WorkorderGroup>> queryWorkorderGroupPage(@RequestBody @Valid WorkorderGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(workorderFacade.queryWorkorderGroupPage(pageQuery));
    }

    @ApiOperation(value = "工作台查询工单组详情")
    @GetMapping(value = "/group/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcWorkorderGroupVO.WorkorderGroup>> queryWorkbenchWorkorderGroup() {
        return new HttpResult<>(workorderFacade.queryWorkbenchWorkorderGroup());
    }

    @ApiOperation(value = "创建工单票据")
    @PostMapping(value = "/ticket/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcWorkorderTicketVO.Ticket> createWorkorderTicket(@RequestBody @Valid WorkorderTicketParam.CreateTicket createTicket) {
        return new HttpResult<>(workorderFacade.createWorkorderTicket(createTicket));
    }

    @ApiOperation(value = "查询工单票据详情")
    @GetMapping(value = "/ticket/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcWorkorderTicketVO.Ticket> queryWorkorderTicket(@Valid WorkorderTicketParam.QueryTicket queryTicket) {
        return new HttpResult<>(workorderFacade.queryWorkorderTicket(queryTicket));
    }

    @ApiOperation(value = "提交工单票据")
    @PutMapping(value = "/ticket/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean>  submitWorkorderTicket(@Valid int id) {
        return new HttpResult<>(workorderFacade.submitWorkorderTicket(id));
    }

    @ApiOperation(value = "工单票据添加条目")
    @PostMapping(value = "/ticket/entry/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addWorkorderTicketEntry(@RequestBody @Valid OcWorkorderTicketEntryVO.Entry entry) {
        return new HttpResult<>(workorderFacade.addTicketEntry(entry));
    }

    @ApiOperation(value = "工单票据更新条目")
    @PostMapping(value = "/ticket/entry/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateWorkorderTicketEntry(@RequestBody @Valid OcWorkorderTicketEntryVO.Entry entry) {
        return new HttpResult<>(workorderFacade.updateTicketEntry(entry));
    }

    @ApiOperation(value = "删除工单票据添加条目")
    @DeleteMapping(value = "/ticket/entry/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delWorkorderTicketEntry(@RequestParam int id) {
        return new HttpResult<>(workorderFacade.delWorkorderTicketEntryById(id));
    }

    // 用户查询服务器组信息
    @ApiOperation(value = "工单票据查询服务器组列表")
    @PostMapping(value = "/ticket/server/group/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcWorkorderTicketEntryVO.Entry>> queryUserTicketServerGroupPage(@RequestBody @Valid ServerGroupParam.UserTicketOcServerGroupQuery queryParam) {
        return new HttpResult<>(workorderFacade.queryUserTicketOcServerGroupByParam(queryParam));
    }

}
