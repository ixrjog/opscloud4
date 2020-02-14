package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.vo.auth.OcResourceVO;
import com.baiyi.opscloud.domain.vo.auth.OcRoleVO;
import com.baiyi.opscloud.facade.AuthFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:02 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "权限配置")
public class AuthController {
    @Resource
    private AuthFacade authFacade;

    @ApiOperation(value = "分页查询role列表")
    @GetMapping(value = "/role/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcRoleVO.OcRole>> queryRolePage(@Valid RoleParam.PageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRolePage(pageQuery));
    }

    @ApiOperation(value = "新增role")
    @PostMapping(value = "/role/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRole(@RequestBody @Valid OcRoleVO.OcRole ocRole) {
        authFacade.addRole(ocRole);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新role")
    @PutMapping(value = "/role/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRole(@RequestBody @Valid OcRoleVO.OcRole ocRole) {
        authFacade.updateRole(ocRole);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的role")
    @DeleteMapping(value = "/role/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleById(@RequestParam int id) {
        return new HttpResult<>(authFacade.deleteRoleById(id));
    }


    @ApiOperation(value = "分页查询resource列表")
    @GetMapping(value = "/resource/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcResourceVO.OcResource>> queryResourcePage(@Valid ResourceParam.PageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryResourcePage(pageQuery));
    }

    @ApiOperation(value = "新增resource")
    @PostMapping(value = "/resource/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addResource(@RequestBody @Valid OcResourceVO.OcResource ocResource) {
        authFacade.addResource(ocResource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新resource")
    @PutMapping(value = "/resource/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateResource(@RequestBody @Valid OcResourceVO.OcResource ocResource) {
        authFacade.updateResource(ocResource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的resource")
    @DeleteMapping(value = "/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteResourceById(@RequestParam int id) {
        return new HttpResult<>(authFacade.deleteResourceById(id));
    }


}
