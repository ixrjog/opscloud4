package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.service.WorkflowService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/workflow")
public class WorkflowController {


    @Resource
    private WorkflowService workflowService;


    /**
     * 查询工作流列表
     *
     * @param topics
     * @return
     */
    @RequestMapping(value = "/queryGroup", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryWorkflowGroup(@RequestParam String topics) {
        return new HttpResult(
                workflowService.queryWorkflowGroup(topics)
        );
    }

    @RequestMapping(value = "/todo/create", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult createTodo(@RequestParam String wfKey) {
        return new HttpResult(
                workflowService.createTodo(wfKey)
        );
    }


    @RequestMapping(value = "/todo/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveTodo(@RequestBody WorkflowTodoVO workflowTodoVO) {
        return new HttpResult(
                workflowService.saveTodo(workflowTodoVO)
        );
    }


    @RequestMapping(value = "/todo/revoke", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult revokeTodo(@RequestParam long id) {
        return new HttpResult(
                workflowService.revokeTodo(id)
        );
    }

    @RequestMapping(value = "/todo/invoke", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult invokeTodo(@RequestParam long id) {
        return new HttpResult(
                workflowService.invokeTodo(id)
        );
    }

    /**
     * 审批/审核工单
     * @param id
     * @return
     */
    @RequestMapping(value = "/todo/approval", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult approvalTodo(@RequestParam long id) {
        return new HttpResult(
                workflowService.approvalTodo(id)
        );
    }

    /**
     * 审批/审核工单(不批准)
     * @param id
     * @return
     */
    @RequestMapping(value = "/todo/disapprove", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult disapproveTodo(@RequestParam long id) {
        return new HttpResult(
                workflowService.disapproveTodo(id)
        );
    }




    @RequestMapping(value = "/todo/detail/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delTodoDetail(@RequestParam long todoId, @RequestParam long detailId) {
        return new HttpResult(
                workflowService.delTodoDetail(todoId, detailId)
        );
    }

    /**
     * 提交申请
     * @param todoId
     * @return
     */
    @RequestMapping(value = "/todo/apply", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult applyTodo(@RequestParam long todoId) {
        return new HttpResult(
                workflowService.applyTodo(todoId)
        );
    }

    /**
     * 查询我的工作流（待办）
     *
     * @return
     */
    @RequestMapping(value = "/todo/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryMyTodo() {
        return new HttpResult(
                workflowService.queryMyTodo()
        );
    }

    /**
     * 查询我的工作流（完成）
     * @return
     */
    @RequestMapping(value = "/todo/queryComplete", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryMyCompleteTodo() {
        return new HttpResult(
                workflowService.queryMyCompleteTodo()
        );
    }


}
