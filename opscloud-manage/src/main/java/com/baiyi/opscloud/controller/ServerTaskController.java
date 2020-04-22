package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.param.ansible.AnsibleScriptParam;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsibleScriptVO;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;
import com.baiyi.opscloud.facade.ServerTaskFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:19 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/server/task")
@Api(tags = "服务器任务")
public class ServerTaskController {

    @Resource
    private ServerTaskFacade serverTaskFacade;

    @ApiOperation(value = "分页模糊查询playbook列表")
    @PostMapping(value = "/playbook/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcAnsiblePlaybookVO.AnsiblePlaybook>> queryPlaybookPage(@RequestBody @Valid AnsiblePlaybookParam.PageQuery pageQuery) {
        return new HttpResult<>(serverTaskFacade.queryPlaybookPage(pageQuery));
    }

    @ApiOperation(value = "新增playbook")
    @PostMapping(value = "/playbook/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addPlaybook(@RequestBody @Valid OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        return new HttpResult<>(serverTaskFacade.addPlaybook(ansiblePlaybook));
    }

    @ApiOperation(value = "更新playbook")
    @PutMapping(value = "/playbook/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updastePlaybook(@RequestBody @Valid OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook) {
        return new HttpResult<>(serverTaskFacade.updatePlaybook(ansiblePlaybook));
    }

    @ApiOperation(value = "删除指定的playbook")
    @DeleteMapping(value = "/playbook/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deletePlaybookById(@Valid @RequestParam int id) {
        return new HttpResult<>(serverTaskFacade.deletePlaybookById(id));
    }

    @ApiOperation(value = "分页模糊查询script/列表")
    @PostMapping(value = "/script/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcAnsibleScriptVO.AnsibleScript>> queryScriptPage(@RequestBody @Valid AnsibleScriptParam.PageQuery pageQuery) {
        return new HttpResult<>(serverTaskFacade.queryScriptPage(pageQuery));
    }

    @ApiOperation(value = "新增script")
    @PostMapping(value = "/script/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addScript(@RequestBody @Valid OcAnsibleScriptVO.AnsibleScript ansibleScript) {
        return new HttpResult<>(serverTaskFacade.addScript(ansibleScript));
    }

    @ApiOperation(value = "更新script")
    @PutMapping(value = "/script/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updasteScript(@RequestBody @Valid OcAnsibleScriptVO.AnsibleScript ansibleScript) {
        return new HttpResult<>(serverTaskFacade.updateScript(ansibleScript));
    }

    @ApiOperation(value = "删除指定的script")
    @DeleteMapping(value = "/script/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteScriptById(@Valid @RequestParam int id) {
        return new HttpResult<>(serverTaskFacade.deleteScriptById(id));
    }


    @ApiOperation(value = "批量命令")
    @PostMapping(value = "/command/executor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessWrapper<Boolean>> executorCommand(@RequestBody @Valid ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor) {
        return new HttpResult(serverTaskFacade.executorCommand(serverTaskCommandExecutor));
    }

    @ApiOperation(value = "批量脚本")
    @PostMapping(value = "/script/executor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessWrapper<Boolean>> executorScript(@RequestBody @Valid ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor) {
        return new HttpResult(serverTaskFacade.executorScript(serverTaskScriptExecutor));
    }

    @ApiOperation(value = "执行playbook")
    @PostMapping(value = "/playbook/executor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessWrapper<Boolean>> executorPlaybook(@RequestBody @Valid ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor) {
        return new HttpResult(serverTaskFacade.executorPlaybook(serverTaskPlaybookExecutor));
    }

    @ApiOperation(value = "查询任务")
    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcServerTaskVO.ServerTask> queryServerTask(@Valid int taskId) {
        return new HttpResult<>(serverTaskFacade.queryServerTaskByTaskId(taskId));
    }

    @ApiOperation(value = "创建ansible主机配置文件")
    @GetMapping(value = "/ansible/hosts/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createAnsibleHosts() {
        return new HttpResult<>(serverTaskFacade.createAnsibleHosts());
    }

    @ApiOperation(value = "中止任务")
    @GetMapping(value = "/abort", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> abortServerTask(@Valid int taskId) {
        return new HttpResult<>(serverTaskFacade.abortServerTask(taskId));
    }

    @ApiOperation(value = "中止子任务")
    @GetMapping(value = "/member/abort", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> abortServerTaskMember(@Valid int memberId) {
        return new HttpResult<>(serverTaskFacade.abortServerTaskMember(memberId));
    }

    @ApiOperation(value = "查询Ansible版本")
    @GetMapping(value = "/ansible/version", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> queryAnsibleVersion() {
        return new HttpResult<>(serverTaskFacade.queryAnsibleVersion());
    }

    @ApiOperation(value = "预览Ansible主机配置文件")
    @GetMapping(value = "/ansible/hosts/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> previewAnsibleHosts() {
        return new HttpResult<>(serverTaskFacade.previewAnsibleHosts());
    }
}
