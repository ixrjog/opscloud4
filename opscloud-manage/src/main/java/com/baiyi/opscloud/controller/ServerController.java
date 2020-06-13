package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.ServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.ServerFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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
    public HttpResult<DataTable<ServerVO.Server>> queryServerPage(@Valid ServerParam.PageQuery pageQuery) {
        return new HttpResult<>(serverFacade.queryServerPage(pageQuery));
    }

    @ApiOperation(value = "查询server详情")
    @GetMapping(value = "/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ServerVO.Server> queryServerById(int id) {
        return new HttpResult<>(serverFacade.queryServerById(id));
    }

    @ApiOperation(value = "批量查询server列表")
    @PostMapping(value = "/ids/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ServerVO.Server>> queryServerByIds(@RequestBody ServerParam.QueryByServerIds queryByServerByIds) {
        return new HttpResult<>(serverFacade.queryServerByIds(queryByServerByIds));
    }

    @ApiOperation(value = "按服务器组查询服务器列表")
    @PostMapping(value = "/query/by/group", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ServerVO.Server>> queryServerByServerGroup(@RequestBody ServerParam.QueryByServerGroup queryByServerGroup) {
        return new HttpResult<>(serverFacade.queryServerByServerGroup(queryByServerGroup));
    }

    @ApiOperation(value = "分页模糊查询server列表")
    @PostMapping(value = "/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerVO.Server>> fuzzyQueryServerPage(@RequestBody @Valid ServerParam.PageQuery pageQuery) {
        return new HttpResult<>(serverFacade.fuzzyQueryServerPage(pageQuery));
    }

    // attribute
    @ApiOperation(value = "查询服务器属性")
    @GetMapping(value = "/attribute/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ServerAttributeVO.ServerAttribute>> queryServerAttribute(@RequestParam int id) {
        return new HttpResult<>(serverFacade.queryServerAttribute(id));
    }

    @ApiOperation(value = "保存服务器组属性")
    @PutMapping(value = "/attribute/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveServerAttribute(@RequestBody @Valid ServerAttributeVO.ServerAttribute serverAttribute) {
        return new HttpResult<>(serverFacade.saveServerAttribute(serverAttribute));
    }

    @ApiOperation(value = "新增server")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServer(@RequestBody @Valid ServerVO.Server server) {
        return new HttpResult<>(serverFacade.addServer(server));
    }

    @ApiOperation(value = "更新server")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServer(@RequestBody @Valid ServerVO.Server server) {
        return new HttpResult<>(serverFacade.updateServer(server));
    }

    @ApiOperation(value = "删除指定的server")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerById(@Valid @RequestParam int id) {
        return new HttpResult<>(serverFacade.deleteServerById(id));
    }


}
