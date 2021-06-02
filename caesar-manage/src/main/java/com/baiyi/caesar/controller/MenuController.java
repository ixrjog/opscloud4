package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.param.sys.MenuParam;
import com.baiyi.caesar.facade.sys.MenuFacade;
import com.baiyi.caesar.domain.vo.sys.MenuVO;
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
        menuFacade.delMenu(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除子菜单")
    @DeleteMapping(value = "/child/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delMenuChild(@RequestParam int id) {
        menuFacade.delMenuChild(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询菜单")
    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.Menu>> queryMenu() {
        return new HttpResult<>(menuFacade.queryMenu());
    }

    @ApiOperation(value = "查询子菜单")
    @GetMapping(value = "/child/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO.MenuChild>> queryMenuChild(@RequestParam Integer id) {
        return new HttpResult<>(menuFacade.queryMenuChild(id));
    }
}
