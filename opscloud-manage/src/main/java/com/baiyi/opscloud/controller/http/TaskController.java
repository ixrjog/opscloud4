package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.facade.task.AnsiblePlaybookFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:05 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/task")
@Api(tags = "任务管理")
public class TaskController {

    @Resource
    private AnsiblePlaybookFacade ansiblePlaybookFacade;


    @ApiOperation(value = "分页查询剧本列表")
    @PostMapping(value = "/ansible/playbook/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AnsiblePlaybookVO.Playbook>> queryAnsiblePlaybookPage(@RequestBody @Valid AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery) {
        return new HttpResult<>(ansiblePlaybookFacade.queryAnsiblePlaybookPage(pageQuery));
    }

}
