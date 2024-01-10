package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.facade.task.AnsiblePlaybookFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:05 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/task")
@Tag(name = "任务管理")
@RequiredArgsConstructor
public class TaskController {

    private final AnsiblePlaybookFacade ansiblePlaybookFacade;

    @Operation(summary = "分页查询剧本列表")
    @PostMapping(value = "/ansible/playbook/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AnsiblePlaybookVO.Playbook>> queryAnsiblePlaybookPage(@RequestBody @Valid AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery) {
        return new HttpResult<>(ansiblePlaybookFacade.queryAnsiblePlaybookPage(pageQuery));
    }

    @Operation(summary = "新增剧本配置")
    @PostMapping(value = "/ansible/playbook/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAnsiblePlaybook(@RequestBody @Valid AnsiblePlaybookParam.Playbook playbook) {
        ansiblePlaybookFacade.addAnsiblePlaybook(playbook);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新剧本配置")
    @PutMapping(value = "/ansible/playbook/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAnsiblePlaybook(@RequestBody @Valid AnsiblePlaybookParam.Playbook playbook) {
        ansiblePlaybookFacade.updateAnsiblePlaybook(playbook);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的剧本配置")
    @DeleteMapping(value = "/ansible/playbook/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerById(@RequestParam int id) {
        ansiblePlaybookFacade.deleteAnsiblePlaybookById(id);
        return HttpResult.SUCCESS;
    }

}