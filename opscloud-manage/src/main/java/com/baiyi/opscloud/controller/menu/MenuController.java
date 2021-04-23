package com.baiyi.opscloud.controller.menu;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcMenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcRoleMenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcSubmenu;
import com.baiyi.opscloud.domain.param.menu.MenuParam;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.menu.MenuFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 5:29 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单平台")
public class MenuController {

    @Resource
    private MenuFacade menuFacade;

    @ApiOperation(value = "保存菜单列表")
    @PostMapping(value = "/list/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveMenuList(@RequestBody @Valid MenuParam.MenuSave param) {
        return new HttpResult<>(menuFacade.saveMenuList(param));
    }

    @ApiOperation(value = "保存子菜单列表")
    @PostMapping(value = "/sub/list/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveSubmenuList(@RequestBody @Valid MenuParam.SubmenuSave param) {
        return new HttpResult<>(menuFacade.saveSubmenuList(param));
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delMenu(@RequestParam int id) {
        return new HttpResult<>(menuFacade.delMenu(id));
    }

    @ApiOperation(value = "删除子菜单")
    @DeleteMapping(value = "/sub/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delSubmenu(@RequestParam int id) {
        return new HttpResult<>(menuFacade.delSubmenu(id));
    }

    @ApiOperation(value = "查询所有菜单列表")
    @GetMapping(value = "/all/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcMenu>> queryMenuAll() {
        return new HttpResult<>(menuFacade.queryMenuAll());
    }

    @ApiOperation(value = "查询子菜单列表")
    @GetMapping(value = "/sub/list/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcSubmenu>> querySubmenuByMenuId(@RequestParam Integer menuId) {
        return new HttpResult<>(menuFacade.querySubmenuByMenuId(menuId));
    }

    @ApiOperation(value = "查询菜单树")
    @GetMapping(value = "/list/tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TreeVO.Tree>> queryMenuListTree() {
        return new HttpResult<>(menuFacade.queryMenuListTree());
    }

    @ApiOperation(value = "保存角色菜单")
    @PostMapping(value = "/role/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveRoleMenu(@RequestBody @Valid MenuParam.RoleMenuSave param) {
        return new HttpResult<>(menuFacade.saveRoleMenu(param));
    }

    @ApiOperation(value = "查询角色菜单")
    @GetMapping(value = "/role/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcRoleMenu>> queryRoleMenu(@RequestParam Integer roleId) {
        return new HttpResult<>(menuFacade.queryRoleMenu(roleId));
    }

    @ApiOperation(value = "查询角色菜单模板")
    @GetMapping(value = "/role/temp/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> queryRoleMenuTemp(@RequestParam Integer roleId) {
        return new HttpResult<>(menuFacade.queryRoleMenuTemp(roleId));
    }

}
