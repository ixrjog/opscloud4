package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/24 10:30 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/server/group")
@Api(tags = "系统管理")
public class ServerGroupController {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @ApiOperation(value = "分页查询服务器组列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerGroupVO.ServerGroup>> queryServerGroupPage(@RequestBody @Valid ServerGroupParam.ServerGroupPageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupPage(pageQuery));
    }

    @ApiOperation(value = "新增服务器组")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerGroup(@RequestBody @Valid ServerGroupVO.ServerGroup serverGroup) {
        serverGroupFacade.addServerGroup(serverGroup);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新服务器组")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerGroup(@RequestBody @Valid ServerGroupVO.ServerGroup serverGroup) {
        serverGroupFacade.updateServerGroup(serverGroup);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的服务器组")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerGroupById(@RequestParam int id) {
        serverGroupFacade.deleteServerGroupById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询服务器组类型列表")
    @PostMapping(value = "/type/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerGroupTypeVO.ServerGroupType>> queryServerGroupTypePage(@RequestBody @Valid ServerGroupTypeParam.ServerGroupTypePageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupTypePage(pageQuery));
    }

    @ApiOperation(value = "新增服务器组类型")
    @PostMapping(value = "/type/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerGroupType(@RequestBody @Valid ServerGroupTypeVO.ServerGroupType serverGroupType) {
        serverGroupFacade.addServerGroupType(serverGroupType);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新服务器组类型")
    @PutMapping(value = "/type/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerGroupType(@RequestBody @Valid ServerGroupTypeVO.ServerGroupType serverGroupType) {
        serverGroupFacade.updateServerGroupType(serverGroupType);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的服务器组类型")
    @DeleteMapping(value = "/type/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerGroupTypeById(@RequestParam int id) {
        serverGroupFacade.deleteServerGroupTypeById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询服务器组环境分组信息(发布平台专用)")
    @PostMapping(value = "/env/pattern/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, List<Server>>> queryServerGroupHostPatternByEnv(@RequestBody @Valid ServerGroupParam.ServerGroupEnvHostPatternQuery query)  {
        return new HttpResult<>(serverGroupFacade.queryServerGroupHostPatternByEnv(query));
    }

//    @ApiOperation(value = "分页查询用户许可的服务器组列表")
//    @PostMapping(value = "/user/permission/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public HttpResult<DataTable<ServerGroupVO.ServerGroup>> queryUserPermissionServerGroupPage(@RequestBody @Valid ServerGroupParam.UserPermissionServerGroupPageQuery pageQuery) {
//        return new HttpResult<>(serverGroupFacade.queryUserPermissionServerGroupPage(pageQuery));
//    }


}
