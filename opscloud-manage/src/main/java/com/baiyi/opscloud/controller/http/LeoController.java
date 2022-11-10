package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.facade.leo.LeoBuildFacade;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.facade.leo.LeoTemplateFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2022/11/1 18:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/leo")
@Api(tags = "Leo管理")
@RequiredArgsConstructor
public class LeoController {

    private final LeoTemplateFacade leoTemplateFacade;

    private final LeoJobFacade leoJobFacade;

    private final LeoBuildFacade leoBuildFacade;

    // Leo Template

    @ApiOperation(value = "分页查询模板列表")
    @PostMapping(value = "/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoTemplateVO.Template>> queryLeoTemplatePage(@RequestBody @Valid LeoTemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(leoTemplateFacade.queryLeoTemplatePage(pageQuery));
    }

    @ApiOperation(value = "新增模板")
    @PostMapping(value = "/template/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoTemplate(@RequestBody @Valid LeoTemplateParam.AddTemplate addTemplate) {
        leoTemplateFacade.addLeoTemplate(addTemplate);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新模板")
    @PutMapping(value = "/template/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoTemplate(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        leoTemplateFacade.updateLeoTemplate(updateTemplate);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "从JenkinsJob更新模板内容")
    @PutMapping(value = "/template/content/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoTemplateVO.Template> updateLeoTemplateContent(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        return new HttpResult<>(leoTemplateFacade.updateLeoTemplateContent(updateTemplate));
    }

    // Leo Job

    @ApiOperation(value = "分页查询任务列表")
    @PostMapping(value = "/job/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoJobVO.Job>> queryLeoJobPage(@RequestBody @Valid LeoJobParam.JobPageQuery pageQuery) {
        return new HttpResult<>(leoJobFacade.queryLeoJobPage(pageQuery));
    }

    @ApiOperation(value = "新增任务")
    @PostMapping(value = "/job/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoJob(@RequestBody @Valid LeoJobParam.AddJob addJob) {
        leoJobFacade.addLeoJob(addJob);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新任务")
    @PutMapping(value = "/job/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoJob(@RequestBody @Valid LeoJobParam.UpdateJob updateJob) {
        leoJobFacade.updateLeoJob(updateJob);
        return HttpResult.SUCCESS;
    }

    // Leo Build

    @ApiOperation(value = "执行构建")
    @PostMapping(value = "/build/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> doLeoBuild(@RequestBody @Valid LeoBuildParam.DoBuild doBuild) {
        leoBuildFacade.doBuild(doBuild);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "构建分支选项")
    @PostMapping(value = "/build/branch/options/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.BranchOptions> getBuildBranchOptions(@RequestBody @Valid LeoBuildParam.GetBuildBranchOptions getOptions) {
        return new HttpResult<>(leoBuildFacade.getBuildBranchOptions(getOptions));
    }

}
