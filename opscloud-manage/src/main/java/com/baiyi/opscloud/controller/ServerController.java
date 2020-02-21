package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.OcServerVO;
import com.baiyi.opscloud.facade.ServerFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:45 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/server")
@Api(tags = "服务器管理")
public class ServerController {

    @Resource
    private ServerFacade serverFacade;

    @ApiOperation(value = "分页查询server列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcServerVO.Server>> queryServerPage(@Valid ServerParam.PageQuery pageQuery) {
        return new HttpResult<>(serverFacade.queryServerPage(pageQuery));
    }

    @ApiOperation(value = "新增server")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServer(@RequestBody @Valid OcServerVO.Server server) {
        return new HttpResult<>(serverFacade.addServer(server));
    }

    @ApiOperation(value = "更新server")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServer(@RequestBody @Valid OcServerVO.Server server) {
        return new HttpResult<>(serverFacade.updateServer(server));
    }

    @ApiOperation(value = "删除指定的server")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerById(@RequestParam int id) {
        return new HttpResult<>(serverFacade.deleteServerById(id));
    }
}
