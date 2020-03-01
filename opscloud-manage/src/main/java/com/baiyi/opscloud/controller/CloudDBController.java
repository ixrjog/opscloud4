package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.facade.CloudServerFacade;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/1 11:15 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/db")
@Api(tags = "云数据库")
public class CloudDBController {


    @Resource
    private CloudServerFacade cloudserverFacade;

//    @ApiOperation(value = "分页查询云数据库列表")
//    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
//    public HttpResult<DataTable<OcCloudDBVO>> queryCloudDBPage(@Valid CloudServerParam.PageQuery pageQuery) {
//        return new HttpResult<>(cloudserverFacade.queryCloudserverPage(pageQuery));
//    }
//
//    @ApiOperation(value = "删除指定的云主机")
//    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
//    public HttpResult<Boolean> deleteCloudserverById(@RequestParam int id) {
//        return new HttpResult<>(cloudserverFacade.deleteCloudserverById(id));
//    }
//
//    @ApiOperation(value = "同步指定的云主机")
//    @GetMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
//    public HttpResult<Boolean> syncCloudserverByKey(@RequestParam String key) {
//        return new HttpResult<>(cloudserverFacade.syncCloudserverByKey(key));
//    }
}
