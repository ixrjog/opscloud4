package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;
import com.baiyi.opscloud.facade.project.ProjectFacade;
import com.baiyi.opscloud.util.OptionsUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2023/5/19 10:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/project")
@Tag(name = "项目管理")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectFacade projectFacade;

    @Operation(summary = "查询项目业务类型选项")
    @GetMapping(value = "/business/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getBusinessTypeOptions() {
        return new HttpResult<>(OptionsUtil.toProjectBusinessTypeOptions());
    }

    @Operation(summary = "分页查询资源关联的项目")
    @PostMapping(value = "/res/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProjectVO.Project>> queryResProjectPage(@RequestBody @Valid ProjectParam.ResProjectPageQuery pageQuery) {
        return new HttpResult<>(projectFacade.queryResProjectPage(pageQuery));
    }

    @Operation(summary = "分页查询项目列表")
    @PostMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProjectVO.Project>> queryProjectPage(@RequestBody @Valid ProjectParam.ProjectPageQuery pageQuery) {
        return new HttpResult<>(projectFacade.queryProjectPage(pageQuery));
    }

    @Operation(summary = "查询项目详情")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ProjectVO.Project> getProjectById(@Valid int id) {
        return new HttpResult<>(projectFacade.getProjectById(id));
    }

    @Operation(summary = "新增项目")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addProject(@RequestBody @Valid ProjectParam.AddProject project) {
        projectFacade.addProject(project);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除项目")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteProject(@RequestParam int id) {
        projectFacade.deleteProject(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新项目")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateProject(@RequestBody @Valid ProjectParam.UpdateProject project) {
        projectFacade.updateProject(project);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "预览项目资源")
    @PostMapping(value = "/res/preview/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProjectResourceVO.Resource>> previewProjectResourcePage(@RequestBody @Valid ProjectResourceParam.ResourcePageQuery pageQuery) {
        return new HttpResult<>(projectFacade.previewProjectResourcePage(pageQuery));
    }

    @Operation(summary = "项目资源绑定")
    @PutMapping(value = "/res/bind", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> bindResource(@RequestBody @Valid ProjectResourceParam.Resource resource) {
        projectFacade.bindResource(resource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "项目资源解除绑定")
    @DeleteMapping(value = "/res/unbind", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> unbindResource(@RequestParam int id) {
        projectFacade.unbindResource(id);
        return HttpResult.SUCCESS;
    }

}