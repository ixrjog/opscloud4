package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloudserver.CloudserverParam;
import com.baiyi.opscloud.domain.vo.cloudserver.OcCloudserverVO;
import com.baiyi.opscloud.facade.CloudserverFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:30 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloudserver")
@Api(tags = "权限配置")
public class CloudserverController {

    @Resource
    private CloudserverFacade cloudserverFacade;

    @ApiOperation(value = "分页查询云主机列表")
    @GetMapping(value = "/server/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcCloudserverVO.OcCloudserver>> queryCloudserverPage(@Valid CloudserverParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudserverFacade.queryCloudserverPage(pageQuery));
    }

    @ApiOperation(value = "删除指定的云主机")
    @DeleteMapping(value = "/server/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudserverById(@RequestParam int id) {
        return new HttpResult<>(cloudserverFacade.deleteCloudserverById(id));
    }

    @ApiOperation(value = "删除指定的云主机")
    @GetMapping(value = "/server/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudserverByKey(@RequestParam String key) {
        return new HttpResult<>(cloudserverFacade.syncCloudserverByKey(key));
    }
}
