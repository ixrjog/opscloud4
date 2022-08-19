package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;
import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;
import com.baiyi.opscloud.facade.workevent.WorkEventFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/15 6:23 PM
 * @Since 1.0
 */


@RestController
@RequestMapping("/api/workevent")
@Api(tags = "工作事件")
@RequiredArgsConstructor
public class WorkEventController {

    private final WorkEventFacade workEventFacade;

    @ApiOperation(value = "分页查询工作事件")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkEventVO.WorkEvent>> queryPageByParam(@RequestBody @Valid WorkEventParam.PageQuery pageQuery) {
        return new HttpResult<>(workEventFacade.queryPageByParam(pageQuery));
    }

    @ApiOperation(value = "新增工作事件")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addWorkEvent(@RequestBody @Valid WorkEventParam.AddWorkEvent param) {
        workEventFacade.addWorkEvent(param);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新工作事件")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateWorkEvent(@RequestBody @Valid WorkEventParam.UpdateWorkEvent param) {
        workEventFacade.updateWorkEvent(param);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除工作事件")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteWorkEvent(@RequestParam int id) {
        workEventFacade.deleteWorkEvent(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "工作事项树查询")
    @PostMapping(value = "/item/tree/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TreeVO.Tree>> queryWorkItemTree(@RequestBody @Valid WorkEventParam.WorkItemTreeQuery param) {
        return new HttpResult<>(workEventFacade.queryWorkItemTree(param));
    }

    @ApiOperation(value = "工作角色查询")
    @GetMapping(value = "/role/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<WorkRole>> listWorkRole() {
        return new HttpResult<>(workEventFacade.listWorkRole());
    }

    @ApiOperation(value = "工作角色id查询")
    @GetMapping(value = "/role/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkRole> getWorkRoleById(@RequestParam int id) {
        return new HttpResult<>(workEventFacade.getWorkRoleById(id));
    }

    @ApiOperation(value = "工作事项查询")
    @PostMapping(value = "/item/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<WorkItem>> listWorkItem(@RequestBody @Valid WorkEventParam.WorkItemQuery param) {
        return new HttpResult<>(workEventFacade.listWorkItem(param));
    }

    @ApiOperation(value = "工作事项id查询")
    @GetMapping(value = "/item/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkItem> getWorkItemById(@RequestParam int id) {
        return new HttpResult<>(workEventFacade.getWorkItemById(id));
    }

}
