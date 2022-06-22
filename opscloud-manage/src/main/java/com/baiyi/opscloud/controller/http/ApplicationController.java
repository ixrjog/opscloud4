package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.util.OptionsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/7/13 3:37 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/application")
@Api(tags = "应用管理")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationFacade applicationFacade;

    @ApiOperation(value = "查询应用业务类型选项")
    @GetMapping(value = "/business/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getBusinessTypeOptions() {
        return new HttpResult<>(OptionsUtil.toApplicationBusinessTypeOptions());
    }

    @ApiOperation(value = "分页查询应用列表")
    @PostMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationVO.Application>> queryApplicationPage(@RequestBody @Valid ApplicationParam.ApplicationPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryApplicationPage(pageQuery));
    }

    @ApiOperation(value = "分页查询容器应用列表")
    @PostMapping(value = "/kubernetes/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationVO.Application>> queryApplicationKubernetesPage(@RequestBody @Valid UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryApplicationKubernetesPage(pageQuery));
    }

    @ApiOperation(value = "查询应用详情")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ApplicationVO.Application> getApplicationById(@Valid Integer id) {
        return new HttpResult<>(applicationFacade.getApplicationById(id));
    }

    @ApiOperation(value = "新增应用")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplication(@RequestBody @Valid ApplicationVO.Application application) {
        applicationFacade.addApplication(application);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除应用")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteApplication(@RequestParam Integer id) {
        applicationFacade.deleteApplication(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新应用")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateApplication(@RequestBody @Valid ApplicationVO.Application application) {
        applicationFacade.updateApplication(application);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "预览应用资源")
    @PostMapping(value = "/res/preview/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationResourceVO.Resource>> previewApplicationResourcePage(@RequestBody @Valid ApplicationResourceParam.ResourcePageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.previewApplicationResourcePage(pageQuery));
    }

    @ApiOperation(value = "应用资源绑定")
    @PutMapping(value = "/res/bind", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> bindApplicationResource(@RequestBody @Valid ApplicationResourceVO.Resource resource) {
        applicationFacade.bindApplicationResource(resource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "应用资源解除绑定")
    @DeleteMapping(value = "/res/unbind", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> unbindApplicationResource(@RequestParam Integer id) {
        applicationFacade.unbindApplicationResource(id);
        return HttpResult.SUCCESS;
    }
}
