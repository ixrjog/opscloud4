package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesApplicationInstanceParam;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesApplicationParam;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesApplicationVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesTemplateVO;
import com.baiyi.opscloud.facade.KubernetesApplicationFacade;
import com.baiyi.opscloud.facade.KubernetesFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:10 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/kubernetes/application")
@Api(tags = "Kubernetes管理")
public class KubernetesApplicationController {

    @Resource
    private KubernetesApplicationFacade kubernetesApplicationFacade;

    @Resource
    private KubernetesFacade kubernetesFacade;

    @ApiOperation(value = "分页我的应用配置")
    @PostMapping(value = "/my/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesApplicationVO.Application>> queryMyKubernetesApplicationPage(@RequestBody @Valid KubernetesApplicationParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesApplicationFacade.queryMyKubernetesApplicationPage(pageQuery));
    }

    @ApiOperation(value = "分页应用配置")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesApplicationVO.Application>> queryKubernetesApplicationPage(@RequestBody @Valid KubernetesApplicationParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesApplicationFacade.queryKubernetesApplicationPage(pageQuery));
    }

    @ApiOperation(value = "新增应用配置")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addKubernetesApplication(@RequestBody @Valid KubernetesApplicationVO.Application application) {
        return new HttpResult<>(kubernetesApplicationFacade.addKubernetesApplication(application));
    }

    @ApiOperation(value = "更新应用配置")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateKubernetesApplication(@RequestBody @Valid KubernetesApplicationVO.Application application) {
        return new HttpResult<>(kubernetesApplicationFacade.updateKubernetesApplication(application));
    }

    @ApiOperation(value = "删除应用配置")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesApplicationById(@RequestParam int id) {
        return new HttpResult<>(kubernetesApplicationFacade.deleteKubernetesApplicationById(id));
    }

    @ApiOperation(value = "名称查询应用配置")
    @GetMapping(value = "/name/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesApplicationVO.Application> queryKubernetesApplicationByName(@RequestParam String name) {
        return new HttpResult<>(kubernetesApplicationFacade.queryKubernetesApplicationByName(name));
    }

    @ApiOperation(value = "查询应用实例配置")
    @GetMapping(value = "/instance/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesApplicationVO.Instance> queryKubernetesApplicationInstanceById(@RequestParam @Valid int id) {
        return new HttpResult<>(kubernetesApplicationFacade.queryKubernetesApplicationInstanceById(id));
    }

    @ApiOperation(value = "分页查询应用实例配置")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesApplicationVO.Instance>> queryKubernetesApplicationInstancePage(@RequestBody @Valid KubernetesApplicationInstanceParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesApplicationFacade.queryKubernetesApplicationInstancePage(pageQuery));
    }

    @ApiOperation(value = "新增应用实例配置")
    @PostMapping(value = "/instance/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addKubernetesApplicationInstance(@RequestBody @Valid KubernetesApplicationVO.Instance instance) {
        return new HttpResult<>(kubernetesApplicationFacade.addKubernetesApplicationInstance(instance));
    }

    @ApiOperation(value = "创建应用实例无状态")
    @PostMapping(value = "/instance/deployment/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createKubernetesDeployment(@RequestBody @Valid KubernetesApplicationInstanceParam.CreateByTemplate createDeployment) {
        return new HttpResult<>(kubernetesApplicationFacade.createKubernetesDeployment(createDeployment));
    }

    @ApiOperation(value = "删除应用实例配置")
    @DeleteMapping(value = "/instance/deployment/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesDeployment(@RequestParam int id) {
        return new HttpResult<>(kubernetesApplicationFacade.deleteKubernetesDeploymentById(id));
    }

    @ApiOperation(value = "创建应用实例服务")
    @PostMapping(value = "/instance/service/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createKubernetesService(@RequestBody @Valid KubernetesApplicationInstanceParam.CreateByTemplate createService) {
        return new HttpResult<>(kubernetesApplicationFacade.createKubernetesService(createService));
    }

    @ApiOperation(value = "删除应用实例服务")
    @DeleteMapping(value = "/instance/service/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesService(@RequestParam int id) {
        return new HttpResult<>(kubernetesApplicationFacade.deleteKubernetesServiceById(id));
    }

    @ApiOperation(value = "更新应用实例配置")
    @PutMapping(value = "/instance/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateKubernetesApplicationInstance(@RequestBody @Valid KubernetesApplicationVO.Instance instance) {
        return new HttpResult<>(kubernetesApplicationFacade.updateKubernetesApplicationInstance(instance));
    }

    @ApiOperation(value = "删除应用实例配置")
    @DeleteMapping(value = "/instance/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesApplicationInstanceById(@RequestParam int id) {
        return new HttpResult<>(kubernetesApplicationFacade.deleteKubernetesApplicationInstanceById(id));
    }

    @ApiOperation(value = "分页应用配置")
    @GetMapping(value = "/instance/label/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> queryKubernetesApplicationInstanceLabel(@RequestParam @Valid int envType) {
        return new HttpResult<>(kubernetesApplicationFacade.queryKubernetesApplicationInstanceLabel(envType));
    }

    @ApiOperation(value = "分页查询实例模版配置")
    @PostMapping(value = "/instance/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesTemplateVO.Template>> queryKubernetesApplicationInstanceTemplatePage(@RequestBody @Valid KubernetesApplicationInstanceParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesTemplatePage(pageQuery));
    }
}
