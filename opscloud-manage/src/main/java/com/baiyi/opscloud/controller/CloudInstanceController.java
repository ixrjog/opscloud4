package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTypeParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTaskVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTypeVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVSwitchVO;
import com.baiyi.opscloud.facade.CloudInstanceFacade;
import com.baiyi.opscloud.facade.CloudInstanceTaskFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:37 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/instance")
@Api(tags = "云实例管理")
public class CloudInstanceController {

    @Resource
    private CloudInstanceFacade cloudInstanceFacade;

    @Resource
    private CloudInstanceTaskFacade cloudInstanceTaskFacade;

    @ApiOperation(value = "分页模糊查询云实例模版列表")
    @PostMapping(value = "/template/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate>> queryCloudInstanceTemplatePage(@RequestBody @Valid CloudInstanceTemplateParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudInstanceFacade.fuzzyQueryCloudInstanceTemplatePage(pageQuery));
    }

    @ApiOperation(value = "保存模版")
    @PostMapping(value = "/template/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveCloudInstanceTemplate(@RequestBody @Valid OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        return new HttpResult<>(cloudInstanceFacade.saveCloudInstanceTemplate(cloudInstanceTemplate));
    }

    @ApiOperation(value = "删除指定的云实例模版")
    @DeleteMapping(value = "/template/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudInstanceTemplate(@RequestParam int id) {
        return new HttpResult<>(cloudInstanceFacade.deleteCloudInstanceTemplateById(id));
    }

    @ApiOperation(value = "保存模版YAML")
    @PostMapping(value = "/template/yaml/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveCloudInstanceTemplateYAML(@RequestBody @Valid OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        return new HttpResult<>(cloudInstanceFacade.saveCloudInstanceTemplateYAML(cloudInstanceTemplate));
    }

    @ApiOperation(value = "分页模糊查询云实例类型列表")
    @PostMapping(value = "/type/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcCloudInstanceTypeVO.CloudInstanceType>> queryCloudInstanceTypePage(@RequestBody @Valid CloudInstanceTypeParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudInstanceFacade.fuzzyQueryCloudInstanceTypePage(pageQuery));
    }

    @ApiOperation(value = "同步云实例类型")
    @GetMapping(value = "/type/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudInstanceType(@RequestParam int cloudType) {
        return new HttpResult<>(cloudInstanceFacade.syncInstanceType(cloudType));
    }

    @ApiOperation(value = "查询地区")
    @GetMapping(value = "/region/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> queryCloudRegionList(@RequestParam int cloudType) {
        return new HttpResult<>(cloudInstanceFacade.queryCloudRegionList(cloudType));
    }

    @ApiOperation(value = "查询cpu规格")
    @GetMapping(value = "/cpu/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<Integer>> queryCloudCpuCoreList(@RequestParam int cloudType) {
        return new HttpResult<>(cloudInstanceFacade.queryCpuCoreList(cloudType));
    }

    @ApiOperation(value = "查询模版可用区中虚拟交换机详情")
    @GetMapping(value = "/template/vswitch/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcCloudVSwitchVO.VSwitch>> queryCloudInstanceTemplateVSwitch(@RequestParam int templateId, String zoneId) {
        return new HttpResult<>(cloudInstanceFacade.queryCloudInstanceTemplateVSwitch(templateId, zoneId));
    }

    @ApiOperation(value = "创建ECS实例")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createCloudInstance(@RequestBody @Valid CloudInstanceTemplateParam.CreateCloudInstance createCloudInstance) {
        return new HttpResult<>(cloudInstanceFacade.createCloudInstance(createCloudInstance));
    }

    @ApiOperation(value = "查询创建实例任务详情")
    @GetMapping(value = "/task/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcCloudInstanceTaskVO.CloudInstanceTask> queryCloudInstanceTask(@RequestParam int id) {
        return new HttpResult<>(cloudInstanceTaskFacade.queryCloudInstanceTask(id));
    }

    @ApiOperation(value = "查询创建实例任务详情（最后一个任务）")
    @GetMapping(value = "/task/last/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcCloudInstanceTaskVO.CloudInstanceTask> queryLastCloudInstanceTask(@RequestParam int templateId) {
        return new HttpResult<>(cloudInstanceTaskFacade.queryLastCloudInstanceTask(templateId));
    }
}
