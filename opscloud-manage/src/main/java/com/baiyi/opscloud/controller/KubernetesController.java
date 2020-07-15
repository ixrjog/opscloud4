package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.kubernetes.*;
import com.baiyi.opscloud.domain.vo.kubernetes.*;
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
 * @Date 2020/6/24 4:40 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/kubernetes")
@Api(tags = "Kubernetes管理")
public class KubernetesController {

    @Resource
    private KubernetesFacade kubernetesFacade;

    @ApiOperation(value = "查询容器组配置")
    @PostMapping(value = "/pod/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<KubernetesPodVO.Pod>> queryKubernetesClusterPage(@RequestBody @Valid KubernetesPodParam.QueryParam queryParam) {
        return new HttpResult<>(kubernetesFacade.queryMyKubernetesPod(queryParam));
    }

    @ApiOperation(value = "分页查询集群配置")
    @PostMapping(value = "/cluster/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesClusterVO.Cluster>> queryKubernetesClusterPage(@RequestBody @Valid KubernetesClusterParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesClusterPage(pageQuery));
    }

    @ApiOperation(value = "新增集群配置")
    @PostMapping(value = "/cluster/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addKubernetesCluster(@RequestBody @Valid KubernetesClusterVO.Cluster cluster) {
        return new HttpResult<>(kubernetesFacade.addKubernetesCluster(cluster));
    }

    @ApiOperation(value = "更新集群配置")
    @PutMapping(value = "/cluster/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateKubernetesCluster(@RequestBody @Valid KubernetesClusterVO.Cluster cluster) {
        return new HttpResult<>(kubernetesFacade.updateKubernetesCluster(cluster));
    }

    @ApiOperation(value = "删除集群配置")
    @DeleteMapping(value = "/cluster/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesClusterById(@RequestParam int id) {
        return new HttpResult<>(kubernetesFacade.deleteKubernetesClusterById(id));
    }

    @ApiOperation(value = "分页查询集群命名空间配置")
    @PostMapping(value = "/cluster/namespace/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesClusterNamespaceVO.Namespace>> queryKubernetesClusterNamespacePage(@RequestBody @Valid KubernetesClusterNamespaceParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesClusterNamespacePage(pageQuery));
    }

    @ApiOperation(value = "新增集群命名空间配置")
    @PostMapping(value = "/cluster/namespace/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addKubernetesClusterNamespace(@RequestBody @Valid KubernetesClusterNamespaceVO.Namespace namespace) {
        return new HttpResult<>(kubernetesFacade.addKubernetesClusterNamespace(namespace));
    }

    @ApiOperation(value = "更新集群命名空间配置")
    @PutMapping(value = "/cluster/namespace/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateKubernetesClusterNamespace(@RequestBody @Valid KubernetesClusterNamespaceVO.Namespace namespace) {
        return new HttpResult<>(kubernetesFacade.updateKubernetesClusterNamespace(namespace));
    }

    @ApiOperation(value = "删除集群命名空间配置")
    @DeleteMapping(value = "/cluster/namespace/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesClusterNamespaceById(@RequestParam int id) {
        return new HttpResult<>(kubernetesFacade.deleteKubernetesClusterNamespaceById(id));
    }

    @ApiOperation(value = "查询未配置的集群命名空间")
    @PostMapping(value = "/cluster/namespace/exclude/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<KubernetesClusterNamespaceVO.Namespace>> queryKubernetesExcludeNamespace(@RequestBody @Valid KubernetesClusterNamespaceParam.ExcludeQuery excludeQuery) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesExcludeNamespace(excludeQuery));
    }

    @ApiOperation(value = "更新无状态配置")
    @PutMapping(value = "/deployment/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncKubernetesDeployment(@RequestParam @Valid int namespaceId) {
        kubernetesFacade.syncKubernetesDeployment(namespaceId);
        return new HttpResult<>(BusinessWrapper.SUCCESS);
    }


    @ApiOperation(value = "分页查询无状态配置")
    @PostMapping(value = "/deployment/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesDeploymentVO.Deployment>> queryKubernetesDeploymentPage(@RequestBody @Valid KubernetesDeploymentParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesDeploymentPage(pageQuery));
    }

    @ApiOperation(value = "分页查询模版配置")
    @PostMapping(value = "/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesTemplateVO.Template>> queryKubernetesTemplatePage(@RequestBody @Valid KubernetesTemplateParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesTemplatePage(pageQuery));
    }


    @ApiOperation(value = "新增模版配置")
    @PostMapping(value = "/template/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addKubernetesTemplate(@RequestBody @Valid KubernetesTemplateVO.Template template) {
        return new HttpResult<>(kubernetesFacade.addKubernetesTemplate(template));
    }

    @ApiOperation(value = "更新模版配置")
    @PutMapping(value = "/template/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateKubernetesTemplate(@RequestBody @Valid KubernetesTemplateVO.Template template) {
        return new HttpResult<>(kubernetesFacade.updateKubernetesTemplate(template));
    }

    @ApiOperation(value = "删除模版配置")
    @DeleteMapping(value = "/template/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesTemplateById(@RequestParam int id) {
        return new HttpResult<>(kubernetesFacade.deleteKubernetesTemplateById(id));
    }

    @ApiOperation(value = "分页查询服务配置")
    @PostMapping(value = "/service/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KubernetesServiceVO.Service>> queryKubernetesServicePage(@RequestBody @Valid KubernetesServiceParam.PageQuery pageQuery) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesServicePage(pageQuery));
    }

    @ApiOperation(value = "查询服务配置")
    @PostMapping(value = "/service/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesServiceVO.Service> queryKubernetesServicePage(@RequestBody @Valid KubernetesServiceParam.QueryParam queryParam ) {
        return new HttpResult<>(kubernetesFacade.queryKubernetesServiceByParam(queryParam));
    }

    @ApiOperation(value = "删除服务配置")
    @DeleteMapping(value = "/service/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKubernetesServiceById(@RequestParam int id) {
        return new HttpResult<>(kubernetesFacade.deleteKubernetesServiceById(id));
    }

    @ApiOperation(value = "更新服务配置")
    @PutMapping(value = "/service/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncKubernetesService(@RequestParam @Valid int namespaceId) {
        kubernetesFacade.syncKubernetesService(namespaceId);
        return new HttpResult<>(BusinessWrapper.SUCCESS);
    }

}
