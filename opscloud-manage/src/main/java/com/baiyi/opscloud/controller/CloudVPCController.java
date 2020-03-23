package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVPCVO;
import com.baiyi.opscloud.facade.CloudVPCFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/3/19 2:03 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/vpc")
@Api(tags = "云专有网络")
public class CloudVPCController {

    @Resource
    private CloudVPCFacade cloudVPCFacade;

    @ApiOperation(value = "分页模糊查询云VPC列表")
    @PostMapping(value = "/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcCloudVPCVO.CloudVpc>> queryCloudVPCPage(@RequestBody @Valid CloudVPCParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudVPCFacade.fuzzyQueryCloudVPCPage(pageQuery));
    }

    @ApiOperation(value = "同步指定的VPC")
    @GetMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudVPCByKey(@RequestParam String key) {
        return new HttpResult<>(cloudVPCFacade.syncCloudVPCByKey(key));
    }

    @ApiOperation(value = "删除指定的VPC")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudVPCById(@RequestParam int id) {
        return new HttpResult<>(cloudVPCFacade.deleteCloudVPCById(id));
    }

    @ApiOperation(value = "设置VPC是否有效")
    @PutMapping(value = "/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCloudImageActive(@RequestParam int id) {
        return new HttpResult<>(cloudVPCFacade.setCloudVPCActive(id));
    }
}
