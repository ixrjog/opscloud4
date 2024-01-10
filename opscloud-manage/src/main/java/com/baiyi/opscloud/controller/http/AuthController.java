package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.*;
import com.baiyi.opscloud.domain.vo.auth.AuthGroupVO;
import com.baiyi.opscloud.domain.vo.auth.AuthResourceVO;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleVO;
import com.baiyi.opscloud.facade.auth.AuthFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:02 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "权限配置")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @Operation(summary = "分页查询role列表")
    @PostMapping(value = "/role/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthRoleVO.Role>> queryRolePage(@RequestBody @Valid AuthRoleParam.AuthRolePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRolePage(pageQuery));
    }

    @Operation(summary = "新增role")
    @PostMapping(value = "/role/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRole(@RequestBody @Valid AuthRoleParam.Role role) {
        authFacade.addRole(role);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新role")
    @PutMapping(value = "/role/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRole(@RequestBody @Valid AuthRoleParam.Role role) {
        authFacade.updateRole(role);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的role")
    @DeleteMapping(value = "/role/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleById(@RequestParam @Valid int id) {
        authFacade.deleteRoleById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "分页查询资源组列表")
    @PostMapping(value = "/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthGroupVO.Group>> queryGroupPage(@RequestBody @Valid AuthGroupParam.AuthGroupPageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryGroupPage(pageQuery));
    }

    @Operation(summary = "新增资源组")
    @PostMapping(value = "/group/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGroup(@RequestBody @Valid AuthGroupParam.Group group) {
        authFacade.addGroup(group);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新资源组")
    @PutMapping(value = "/group/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateGroup(@RequestBody @Valid AuthGroupParam.Group group) {
        authFacade.updateGroup(group);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的资源组")
    @DeleteMapping(value = "/group/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteGroupById(@RequestParam @Valid int id) {
        authFacade.deleteGroupById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "分页查询角色绑定的资源列表")
    @PostMapping(value = "/role/resource/bind/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthResourceVO.Resource>> queryRoleBindResourcePage(@RequestBody @Valid AuthResourceParam.RoleBindResourcePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRoleBindResourcePage(pageQuery));
    }

    @Operation(summary = "角色绑定资源")
    @PostMapping(value = "/role/resource/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRoleResource(@RequestBody @Valid AuthRoleResourceParam.RoleResource roleResource) {
        authFacade.addRoleResource(roleResource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "角色解除绑定资源")
    @DeleteMapping(value = "/role/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleResourceById(@RequestParam @Valid int id) {
        authFacade.deleteRoleResourceById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "分页查询资源列表")
    @PostMapping(value = "/resource/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthResourceVO.Resource>> queryResourcePage(@RequestBody @Valid AuthResourceParam.AuthResourcePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryResourcePage(pageQuery));
    }

    @Operation(summary = "新增资源")
    @PostMapping(value = "/resource/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addResource(@RequestBody @Valid AuthResourceParam.Resource resource) {
        authFacade.addResource(resource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新资源")
    @PutMapping(value = "/resource/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateResource(@RequestBody @Valid AuthResourceParam.Resource resource) {
        authFacade.updateResource(resource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除资源")
    @DeleteMapping(value = "/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteResourceById(@RequestParam @Valid int id) {
        authFacade.deleteResourceById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新用户角色")
    @PutMapping(value = "/user/role/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUserRole(@RequestBody @Valid AuthUserRoleParam.UpdateUserRole updateUserRole) {
        authFacade.updateUserRole(updateUserRole);
        return HttpResult.SUCCESS;
    }

}