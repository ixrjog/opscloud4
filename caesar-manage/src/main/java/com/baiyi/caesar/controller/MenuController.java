package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.generator.caesar.AuthRoleMenu;
import com.baiyi.caesar.domain.param.sys.MenuParam;
import com.baiyi.caesar.domain.vo.common.TreeVO;
import com.baiyi.caesar.domain.vo.sys.MenuVO;
import com.baiyi.caesar.facade.sys.MenuFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 1:17 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/sys/menu")
@Api(tags = "菜单管理")
public class MenuController {

    @Resource
    private MenuFacade menuFacade;

    @ApiOperation(value = "保存菜单")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveMenu(@RequestBody @Valid MenuParam.MenuSave param) {
        menuFacade.saveMenu(param);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "保存子菜单")
    @PostMapping(value = "/child/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveMenuChild(@RequestBody @Valid MenuParam.MenuChildSave param) {
        menuFacade.saveMenuChild(param);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delMenu(@RequestParam int id) {
        menuFacade.delMenuById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除子菜单")
    @DeleteMapping(value = "/child/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delMenuChild(@RequestParam int id) {
        menuFacade.delMenuChildById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询菜单")
    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Menu>> queryMenu() {
        return new HttpResult<>(menuFacade.queryMenu());
    }

    @ApiOperation(value = "查询子菜单")
    @GetMapping(value = "/child/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Child>> queryMenuChild(@RequestParam Integer id) {
        return new HttpResult<>(menuFacade.queryMenuChild(id));
    }

    @ApiOperation(value = "查询菜单树")
    @GetMapping(value = "/tree/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TreeVO.Tree>> queryMenuTree() {
        return new HttpResult<>(menuFacade.queryMenuTree());
    }

    @ApiOperation(value = "保存角色菜单")
    @PostMapping(value = "/role/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveRoleMenu(@RequestBody @Valid MenuParam.AuthRoleMenuSave param) {
        menuFacade.saveAuthRoleMenu(param);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询角色菜单")
    @GetMapping(value = "/role/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AuthRoleMenu>> queryAuthRoleMenu(Integer roleId) {
        return new HttpResult<>(menuFacade.queryAuthRoleMenu(roleId));
    }

    @ApiOperation(value = "查询角色菜单详情")
    @GetMapping(value = "/role/detail/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Menu>> queryAuthRoleMenuDetail(Integer roleId) {
        return new HttpResult<>(menuFacade.queryAuthRoleMenuDetail(roleId));
    }

    @ApiOperation(value = "查询我的菜单")
    @GetMapping(value = "/my/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Menu>> queryMyMenu() {
        return new HttpResult<>(menuFacade.queryMyMenu());
    }
}
