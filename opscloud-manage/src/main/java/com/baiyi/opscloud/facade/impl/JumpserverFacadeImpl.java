package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.decorator.UsersUserDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import com.baiyi.opscloud.domain.generator.jumpserver.Terminal;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;
import com.baiyi.opscloud.domain.vo.jumpserver.*;
import com.baiyi.opscloud.facade.JumpserverFacade;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.decorator.JumpserverSettingsDecorator;
import com.baiyi.opscloud.server.decorator.JumpserverTerminalDecorator;
import com.baiyi.opscloud.server.factory.ServerFactory;
import com.baiyi.opscloud.service.jumpserver.AssetsAssetService;
import com.baiyi.opscloud.service.jumpserver.AssetsNodeService;
import com.baiyi.opscloud.service.jumpserver.TerminalService;
import com.baiyi.opscloud.service.jumpserver.UsersUserService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:18 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class JumpserverFacadeImpl implements JumpserverFacade {


    @Resource
    private OcUserService ocUserService;

    @Resource
    private JumpserverCenter jumpserverCenter;


    @Resource
    private UsersUserService usersUserService;

    @Resource
    private AssetsAssetService assetsAssetService;

    @Resource
    private UsersUserDecorator usersUserDecorator;

    @Resource
    private AssetsNodeService assetsNodeService;

    @Resource
    private JumpserverSettingsDecorator jumpserverSettingsDecorator;

    @Resource
    private JumpserverTerminalDecorator jumpserverTerminalDecorator;

    @Resource
    private TerminalService terminalService;

    @Resource
    private RedisUtil redisUtil;
    // 授权规则前缀
    public final String PERMS_PREFIX = "perms_";


    @Override
    public BusinessWrapper<Boolean> syncUsers() {
        getIAccount().sync();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncUserById(String id) {
        UsersUser usersUser = usersUserService.queryUsersUserById(id);
        OcUser ocUser = ocUserService.queryOcUserByUsername(usersUser.getUsername());
        if (ocUser == null) return new BusinessWrapper(ErrorEnum.USER_NOT_EXIST);
        getIAccount().sync(ocUser);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> setUserActive(String id) {
        return jumpserverCenter.setUserActive(id);
    }

    @Override
    public BusinessWrapper<Boolean> delUserByUsername(String username) {
        jumpserverCenter.delUsersUser(username);
        return BusinessWrapper.SUCCESS;
    }


    /**
     * 同步资产 包括（主机，服务器组，用户组，授权等）
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> syncAssets() {
        getIServer().sync();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delAssetById(String assetId) {
        jumpserverCenter.delAssetsAsset(assetId);
        return BusinessWrapper.SUCCESS;
    }

    private IAccount getIAccount() {
        return AccountFactory.getAccountByKey("JumpserverAccount");
    }


    private IServer getIServer() {
        return ServerFactory.getIServerByKey("JumpserverAsset");
    }

    @Override
    public DataTable<JumpserverUsersUserVO.UsersUser> fuzzyQueryUserPage(UsersUserPageParam.PageQuery pageQuery) {
        DataTable<UsersUser> table = usersUserService.fuzzyQueryUsersUserPage(pageQuery);
        List<JumpserverUsersUserVO.UsersUser> page = BeanCopierUtils.copyListProperties(table.getData(), JumpserverUsersUserVO.UsersUser.class);
        DataTable<JumpserverUsersUserVO.UsersUser> dataTable
                = new DataTable<>(page.stream().map(e -> usersUserDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<JumpserverUsersUserVO.UsersUser> fuzzyQueryAdminUserPage(UsersUserPageParam.PageQuery pageQuery) {
        DataTable<UsersUser> table = usersUserService.fuzzyQueryAdminUsersUserPage(pageQuery);
        List<JumpserverUsersUserVO.UsersUser> page = BeanCopierUtils.copyListProperties(table.getData(), JumpserverUsersUserVO.UsersUser.class);
        DataTable<JumpserverUsersUserVO.UsersUser> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<JumpserverAssetsAssetVO.AssetsAsset> fuzzyQueryAssetPage(AssetsAssetPageParam.PageQuery pageQuery) {
        DataTable<AssetsAsset> table = assetsAssetService.fuzzyQueryAssetsAssetPage(pageQuery);
        List<JumpserverAssetsAssetVO.AssetsAsset> page = BeanCopierUtils.copyListProperties(table.getData(), JumpserverAssetsAssetVO.AssetsAsset.class);
        DataTable<JumpserverAssetsAssetVO.AssetsAsset> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<JumpserverAssetsNodeVO.AssetsNode> queryAssetsNodePage(AssetsNodePageParam.PageQuery pageQuery) {
        DataTable<AssetsNode> table = assetsNodeService.queryAssetsNodePage(pageQuery);
        List<JumpserverAssetsNodeVO.AssetsNode> page = BeanCopierUtils.copyListProperties(table.getData(), JumpserverAssetsNodeVO.AssetsNode.class);
        DataTable<JumpserverAssetsNodeVO.AssetsNode> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public JumpserverSettingsVO.Settings querySettings() {
        JumpserverSettingsVO.Settings settings = new JumpserverSettingsVO.Settings();
        return jumpserverSettingsDecorator.decorator(settings);
    }

    @Override
    public BusinessWrapper<Boolean> saveSettings(JumpserverSettingsVO.Settings settings) {
        Map<String, String> settingsMap = Maps.newHashMap();
        settingsMap.put(Global.JUMPSERVER_ASSETS_ADMINUSER_ID_KEY, settings.getAssetsAdminuserId());
        settingsMap.put(Global.JUMPSERVER_ASSETS_SYSTEMUSER_ID_KEY, settings.getAssetsSystemuserId());
        settingsMap.put(Global.JUMPSERVER_ASSETS_ADMIN_SYSTEMUSER_ID_KEY, settings.getAssetsAdminSystemuserId());
        redisUtil.set(Global.JUMPSERVER_SETTINGS_KEY, settingsMap, TimeUtils.dayTime * 1000);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<JumpserverTerminalVO.Terminal> queryTerminal() {
        List<Terminal> terminalList = terminalService.queryTerminal();
        return terminalList.stream().map(e ->
                jumpserverTerminalDecorator.decorator(BeanCopierUtils.copyProperties(e, JumpserverTerminalVO.Terminal.class))).collect(Collectors.toList());
    }

    @Override
    public BusinessWrapper<Boolean> authAdmin(String usersUserId) {
        return jumpserverCenter.authAdmin(usersUserId);
    }

    @Override
    public BusinessWrapper<Boolean> revokeAdmin(String usersUserId) {
        return jumpserverCenter.revokeAdmin(usersUserId);
    }


}
