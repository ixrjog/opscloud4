package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;
import com.baiyi.opscloud.domain.param.sys.MenuParam;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.sys.MenuVO;
import com.baiyi.opscloud.facade.sys.MenuFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/2 1:17 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/api/sys/menu")
@Tag(name = "菜单管理")
@RequiredArgsConstructor
public class MenuController {

    private final MenuFacade menuFacade;

    @Operation(summary = "保存菜单")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveMenu(@RequestBody @Valid MenuParam.MenuSave param) {
        menuFacade.saveMenu(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "保存子菜单")
    @PostMapping(value = "/child/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveMenuChild(@RequestBody @Valid MenuParam.MenuChildSave param) {
        menuFacade.saveMenuChild(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delMenu(@RequestParam int id) {
        menuFacade.delMenuById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除子菜单")
    @DeleteMapping(value = "/child/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delMenuChild(@RequestParam int id) {
        menuFacade.delMenuChildById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询菜单")
    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Menu>> queryMenu() {
        return new HttpResult<>(menuFacade.queryMenu());
    }

    @Operation(summary = "查询子菜单")
    @GetMapping(value = "/child/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Child>> queryMenuChild(@RequestParam int id) {
        return new HttpResult<>(menuFacade.queryMenuChild(id));
    }

    @Operation(summary = "查询菜单树")
    @GetMapping(value = "/tree/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TreeVO.Tree>> queryMenuTree() {
        return new HttpResult<>(menuFacade.queryMenuTree());
    }

    @Operation(summary = "保存角色菜单")
    @PostMapping(value = "/role/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveRoleMenu(@RequestBody @Valid MenuParam.AuthRoleMenuSave param) {
        menuFacade.saveAuthRoleMenu(param);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询角色菜单")
    @GetMapping(value = "/role/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AuthRoleMenu>> queryAuthRoleMenu(@RequestParam int roleId) {
        return new HttpResult<>(menuFacade.queryAuthRoleMenu(roleId));
    }

    @Operation(summary = "查询角色菜单详情")
    @GetMapping(value = "/role/detail/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Menu>> queryAuthRoleMenuDetail(@RequestParam int roleId) {
        return new HttpResult<>(menuFacade.queryAuthRoleMenuDetail(roleId));
    }

    @Operation(summary = "查询我的菜单")
    @GetMapping(value = "/my/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Menu>> queryMyMenu() {
        return new HttpResult<>(menuFacade.queryMyMenu());
    }

}