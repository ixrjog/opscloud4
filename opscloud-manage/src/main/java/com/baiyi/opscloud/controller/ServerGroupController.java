package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.domain.param.server.SeverGroupPropertyParam;
import com.baiyi.opscloud.domain.vo.server.ServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupPropertyVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.facade.ServerGroupFacade;
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
 * @Date 2020/2/21 10:54 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/server/group")
@Api(tags = "服务器组管理")
public class ServerGroupController {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @ApiOperation(value = "分页查询serverGroup列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerGroupVO.ServerGroup>> queryServerGroupPage(@Valid ServerGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupPage(pageQuery));
    }

    @ApiOperation(value = "按id查询serverGroup详情")
    @GetMapping(value = "/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ServerGroupVO.ServerGroup> queryServerGroupById(@Valid int id) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupById(id));
    }

    @ApiOperation(value = "新增serverGroup")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerGroup(@RequestBody @Valid ServerGroupVO.ServerGroup serverGroup) {
        return new HttpResult<>(serverGroupFacade.addServerGroup(serverGroup));
    }

    @ApiOperation(value = "更新serverGroup")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerGroup(@RequestBody @Valid ServerGroupVO.ServerGroup serverGroup) {
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
    public HttpResult<DataTable<ServerGroupTypeVO.ServerGroupType>> queryServerGroupTypePage(@Valid ServerGroupTypeParam.PageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupTypePage(pageQuery));
    }

    @ApiOperation(value = "新增serverGroupType")
    @PostMapping(value = "/type/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerGroupType(@RequestBody @Valid ServerGroupTypeVO.ServerGroupType serverGroupType) {
        return new HttpResult<>(serverGroupFacade.addServerGroupType(serverGroupType));
    }

    @ApiOperation(value = "更新serverGroup")
    @PutMapping(value = "/type/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerGroupType(@RequestBody @Valid ServerGroupTypeVO.ServerGroupType serverGroupType) {
        return new HttpResult<>(serverGroupFacade.updateServerGroupType(serverGroupType));
    }

    @ApiOperation(value = "删除指定的serverGroup")
    @DeleteMapping(value = "/type/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerGroupTypeById(@RequestParam int id) {
        return new HttpResult<>(serverGroupFacade.deleteServerGroupTypeById(id));
    }

    @ApiOperation(value = "分页查询user授权的服务器组列表")
    @GetMapping(value = "/user/include/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerGroupVO.ServerGroup>> queryUserIncludeUserGroupPage(@Valid ServerGroupParam.UserServerGroupPageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryUserIncludeServerGroupPage(pageQuery));
    }

    @ApiOperation(value = "分页查询user未授权的服务器组列表")
    @GetMapping(value = "/user/exclude/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerGroupVO.ServerGroup>> queryUserExcludeUserGroupPage(@Valid ServerGroupParam.UserServerGroupPageQuery pageQuery) {
        return new HttpResult<>(serverGroupFacade.queryUserExcludeServerGroupPage(pageQuery));
    }

    // user group
    @ApiOperation(value = "用户组授权给用户")
    @GetMapping(value = "/grant", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserServerGroup(@Valid ServerGroupParam.UserServerGroupPermission userServerGroupPermission) {
        return new HttpResult<>(serverGroupFacade.grantUserServerGroup(userServerGroupPermission));
    }

    @ApiOperation(value = "用户解除用户组授权")
    @DeleteMapping(value = "/revoke", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserServerGroup(@Valid ServerGroupParam.UserServerGroupPermission userServerGroupPermission) {
        return new HttpResult<>(serverGroupFacade.revokeUserServerGroup(userServerGroupPermission));
    }

    // attribute
    @ApiOperation(value = "查询服务器组属性")
    @GetMapping(value = "/attribute/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ServerAttributeVO.ServerAttribute>> queryServerGroupAttribute(@RequestParam int id) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupAttribute(id));
    }

    @ApiOperation(value = "保存服务器组属性")
    @PutMapping(value = "/attribute/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveServerGroupAttribute(@RequestBody @Valid ServerAttributeVO.ServerAttribute serverAttribute) {
        return new HttpResult<>(serverGroupFacade.saveServerGroupAttribute(serverAttribute));
    }

    @ApiOperation(value = "查询服务器组扩展属性")
    @GetMapping(value = "/property/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<Integer, Map<String, String>>> queryServerGroupProperty(@RequestParam int id) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupPropertyMap(id));
    }

    @ApiOperation(value = "查询服务器组扩展属性")
    @PostMapping(value = "/property/set/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<Integer, Map<String, String>>> queryServerGroupPropertyBySet(@RequestBody @Valid SeverGroupPropertyParam.PropertyParam propertyParam) {
        return new HttpResult<>(serverGroupFacade.queryServerGroupPropertyMap(propertyParam));
    }

    @ApiOperation(value = "保存服务器组扩展属性")
    @PutMapping(value = "/property/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveServerGroupProperty(@RequestBody @Valid ServerGroupPropertyVO.ServerGroupProperty serverGroupProperty) {
        return new HttpResult<>(serverGroupFacade.saveServerGroupProperty(serverGroupProperty));
    }

    @ApiOperation(value = "删除服务器组扩展属性")
    @DeleteMapping(value = "/property/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delServerGroupProperty(int id) {
        return new HttpResult<>(serverGroupFacade.delServerGroupPropertyById(id));
    }


}
