package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
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

    @ApiOperation(value = "查询我的工单")
    @PostMapping(value = "/ticket/my/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcWorkorderTicketVO.Ticket>> queryMyWorkorderTicketPage(@RequestBody @Valid WorkorderTicketParam.QueryMyTicket queryMyTicket) {
        return new HttpResult<>(workorderFacade.queryMyTicketPage(queryMyTicket));
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
    @PutMapping(value = "/ticket/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> submitWorkorderTicket(@RequestBody @Valid OcWorkorderTicketVO.Ticket ticket) {
        return new HttpResult<>(workorderFacade.submitWorkorderTicket(ticket));
    }

    @ApiOperation(value = "审批同意工单票据")
    @PutMapping(value = "/ticket/agree",  produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> agreeWorkorderTicket( @Valid int ticketId) {
        return new HttpResult<>(workorderFacade.agreeWorkorderTicket(ticketId));
    }

    @ApiOperation(value = "审批拒绝工单票据")
    @PutMapping(value = "/ticket/disagree",  produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> disagreeWorkorderTicket( @Valid int ticketId) {
        return new HttpResult<>(workorderFacade.disagreeWorkorderTicket(ticketId));
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

    /**
     * 工单配置-用户组查询
     * @param queryParam
     * @return
     */
    @ApiOperation(value = "工单配置-用户组查询")
    @PostMapping(value = "/ticket/server/group/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcWorkorderTicketEntryVO.Entry>> queryUserTicketServerGroupPage(@RequestBody @Valid ServerGroupParam.UserTicketOcServerGroupQuery queryParam) {
        return new HttpResult<>(workorderFacade.queryUserTicketOcServerGroupByParam(queryParam));
    }

    /**
     * 工单配置-用户组查询
     * @param queryParam
     * @return
     */
    @ApiOperation(value = "工单配置-用户组查询")
    @PostMapping(value = "/ticket/user/group/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcWorkorderTicketEntryVO.Entry>> queryUserTicketUserGroupPage(@RequestBody @Valid UserBusinessGroupParam.UserTicketOcUserGroupQuery queryParam) {
        return new HttpResult<>(workorderFacade.queryUserTicketOcUserGroupByParam(queryParam));
    }

    /**
     * 工单配置-平台角色查询
     * @param queryParam
     * @return
     */
    @ApiOperation(value = "工单配置-平台角色查询")
    @PostMapping(value = "/ticket/role/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcWorkorderTicketEntryVO.Entry>> queryUserTicketAuthRolePage(@RequestBody @Valid RoleParam.UserTicketOcAuthRoleQuery queryParam) {
        return new HttpResult<>(workorderFacade.queryUserTicketOcAuthRoleByParam(queryParam));
    }

}
