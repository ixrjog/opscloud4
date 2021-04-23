package com.baiyi.opscloud.controller.gitlab;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabGroupVO;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.opscloud.facade.gitlab.GitlabFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/23 3:03 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/gitlab")
@Api(tags = "gitlab管理")
public class GitlabController {

    @Resource
    private GitlabFacade gitlabFacade;

    @ApiOperation(value = "分页查询实例")
    @GetMapping(value = "/instance/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<GitlabInstanceVO.Instance>> queryGitlabInstance(@RequestParam String queryName) {
        return new HttpResult<>(gitlabFacade.queryGitlabInstance(queryName));
    }

    @ApiOperation(value = "分页查询群组")
    @PostMapping(value = "/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<GitlabGroupVO.Group>> queryGitlabGroupPage(@RequestBody @Valid GitlabGroupParam.GitlabGroupPageQuery query) {
        return new HttpResult<>(gitlabFacade.queryGitlabGroupPage(query));
    }
}
