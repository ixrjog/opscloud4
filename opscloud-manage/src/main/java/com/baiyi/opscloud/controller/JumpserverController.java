package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;
import com.baiyi.opscloud.domain.vo.jumpserver.*;
import com.baiyi.opscloud.facade.JumpserverFacade;
import com.baiyi.opscloud.jumpserver.facade.TerminalSessionFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/12 10:05 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/jump/jumpserver")
@Api(tags = "Jumpserver管理")
public class JumpserverController {

    @Resource
    private JumpserverFacade jumpserverFacade;

    @Resource
    private TerminalSessionFacade terminalSessionFacade;

    @ApiOperation(value = "分页查询jumpserver用户列表")
    @PostMapping(value = "/user/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<JumpserverUsersUserVO.UsersUser>> fuzzyQueryUserPage(@RequestBody @Valid UsersUserPageParam.PageQuery pageQuery) {
        return new HttpResult<>(jumpserverFacade.fuzzyQueryUserPage(pageQuery));
    }

    @ApiOperation(value = "分页查询jumpserver管理员列表")
    @PostMapping(value = "/user/admin/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<JumpserverUsersUserVO.UsersUser>> fuzzyQueryAdminUserPage(@RequestBody @Valid UsersUserPageParam.PageQuery pageQuery) {
        return new HttpResult<>(jumpserverFacade.fuzzyQueryAdminUserPage(pageQuery));
    }

    @ApiOperation(value = "同步用户")
    @GetMapping(value = "/user/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUsers() {
        return new HttpResult<>(jumpserverFacade.syncUsers());
    }

    @ApiOperation(value = "同步用户")
    @GetMapping(value = "/user/id/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUser(@Valid String id) {
        return new HttpResult<>(jumpserverFacade.syncUserById(id));
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/user/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteUserByUsername(@RequestParam @Valid String username) {
        return new HttpResult<>(jumpserverFacade.delUserByUsername(username));
    }

    @ApiOperation(value = "设置用户是否有效")
    @GetMapping(value = "/user/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setUserActive(@RequestParam String id) {
        return new HttpResult<>(jumpserverFacade.setUserActive(id));
    }

    @ApiOperation(value = "分页查询jumpserver资产列表")
    @PostMapping(value = "/asset/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<JumpserverAssetsAssetVO.AssetsAsset>> fuzzyQueryAssetPage(@RequestBody @Valid AssetsAssetPageParam.PageQuery pageQuery) {
        return new HttpResult<>(jumpserverFacade.fuzzyQueryAssetPage(pageQuery));
    }

    @ApiOperation(value = "同步资产")
    @GetMapping(value = "/asset/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncAsset() {
        return new HttpResult<>(jumpserverFacade.syncAssets());
    }

    @ApiOperation(value = "删除资产")
    @DeleteMapping(value = "/asset/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteAssetById(@RequestParam @Valid String assetId) {
        return new HttpResult<>(jumpserverFacade.delAssetById(assetId));
    }

    @ApiOperation(value = "分页查询资产节点列表")
    @GetMapping(value = "/assetsNode/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<JumpserverAssetsNodeVO.AssetsNode>> queryAssetsNodePage(@Valid AssetsNodePageParam.PageQuery pageQuery) {
        return new HttpResult<>(jumpserverFacade.queryAssetsNodePage(pageQuery));
    }

    @ApiOperation(value = "查询全局配置")
    @GetMapping(value = "/settings/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<JumpserverSettingsVO.Settings> querySettings() {
        return new HttpResult<>(jumpserverFacade.querySettings());
    }

    @ApiOperation(value = "保存全局配置")
    @PostMapping(value = "/settings/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveSettings(@RequestBody @Valid JumpserverSettingsVO.Settings settings) {
        return new HttpResult<>(jumpserverFacade.saveSettings(settings));
    }

    @ApiOperation(value = "查询终端(koko)信息")
    @GetMapping(value = "/terminal/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<JumpserverTerminalVO.Terminal>> queryTerminal() {
        return new HttpResult<>(jumpserverFacade.queryTerminal());
    }

    @ApiOperation(value = "管理员授权")
    @GetMapping(value = "/user/admin/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> authAdmin(@RequestParam String id) {
        return new HttpResult<>(jumpserverFacade.authAdmin(id));
    }

    @ApiOperation(value = "撤销管理员授权")
    @GetMapping(value = "/user/admin/revoke", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeAdmin(@RequestParam String id) {
        return new HttpResult<>(jumpserverFacade.revokeAdmin(id));
    }

    @ApiOperation(value = "分页查询jumpserver活动会话")
    @PostMapping(value = "/terminal/session/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<JumpserverTerminalSessionVO.TerminalSession>> queryTerminalSessionPage(@RequestBody @Valid PageParam pageQuery) {
        return new HttpResult<>(terminalSessionFacade.queryTerminalSessionPage(pageQuery));
    }
}
