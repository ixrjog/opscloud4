package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
import com.baiyi.opscloud.facade.ServerGroupFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/21 10:54 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/server/group")
@Api(tags = "服务器组配置")
public class ServerGroupController {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @ApiOperation(value = "分页查询serverGroup列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcServerGroupVO.ServerGroup>> queryServerGroupPage(@Valid ServerGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupPage(pageQuery));
    }

    @ApiOperation(value = "新增serverGroup")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerGroup(@RequestBody @Valid OcServerGroupVO.ServerGroup serverGroup) {
        return new HttpResult<>(serverGroupFacade.addServerGroup(serverGroup));
    }

    @ApiOperation(value = "更新serverGroup")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerGroup(@RequestBody @Valid OcServerGroupVO.ServerGroup serverGroup) {
        return new HttpResult<>(serverGroupFacade.updateServerGroup(serverGroup));
    }

    @ApiOperation(value = "删除指定的serverGroup")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerGroupById(@RequestParam int id) {
        return new HttpResult<>(serverGroupFacade.deleteServerGroupById(id));
    }

    // server group type
    @ApiOperation(value = "分页查询serverGroupType列表")
    @GetMapping(value = "/type/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcServerGroupTypeVO.ServerGroupType>> queryServerGroupTypePage(@Valid ServerGroupTypeParam.PageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupTypePage(pageQuery));
    }

    @ApiOperation(value = "新增serverGroupType")
    @PostMapping(value = "/type/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerGroupType(@RequestBody @Valid OcServerGroupTypeVO.ServerGroupType serverGroupType) {
        return new HttpResult<>(serverGroupFacade.addServerGroupType(serverGroupType));
    }

    @ApiOperation(value = "更新serverGroup")
    @PutMapping(value = "/type/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerGroupType(@RequestBody @Valid OcServerGroupTypeVO.ServerGroupType serverGroupType) {
        return new HttpResult<>(serverGroupFacade.updateServerGroupType(serverGroupType));
    }

    @ApiOperation(value = "删除指定的serverGroup")
    @DeleteMapping(value = "/type/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerGroupTypeById(@RequestParam int id) {
        return new HttpResult<>(serverGroupFacade.deleteServerGroupTypeById(id));
    }

}
