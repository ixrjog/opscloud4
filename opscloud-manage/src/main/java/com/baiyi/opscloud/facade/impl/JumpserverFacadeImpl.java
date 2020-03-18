package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.AssetsAssetBuilder;
import com.baiyi.opscloud.common.base.CredentialType;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.decorator.JumpserverSettingsDecorator;
import com.baiyi.opscloud.decorator.JumpserverTerminalDecorator;
import com.baiyi.opscloud.decorator.UsersUserDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.*;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.vo.jumpserver.*;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.facade.JumpserverFacade;
import com.baiyi.opscloud.facade.ServerAttributeFacade;
import com.baiyi.opscloud.facade.ServerFacade;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.service.jumpserver.AssetsAssetService;
import com.baiyi.opscloud.service.jumpserver.AssetsNodeService;
import com.baiyi.opscloud.service.jumpserver.TerminalService;
import com.baiyi.opscloud.service.jumpserver.UsersUserService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private OcServerService ocServerService;

    @Resource
    private ServerFacade serverFacade;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private JumpserverCenter jumpserverCenter;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

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

    // 过期时间
    //public final String DATE_EXPIRED = "2089-01-01 00:00:00";



    @Override
    public BusinessWrapper<Boolean> syncUsers() {
        List<OcUser> userList = ocUserService.queryOcUserActive();
        for (OcUser ocUser : userList)
            syncUsersUser(ocUser);
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public BusinessWrapper<Boolean> setUserActive(String id) {
        return jumpserverCenter.setUserActive(id);
    }

    private void syncUsersUser(OcUser ocUser) {
        // 只同步有服务器组授权的用户
        List<OcServerGroup> serverGroupList = ocServerGroupService.queryUserPermissionOcServerGroupByUserId(ocUser.getId());
        if (serverGroupList.isEmpty()) return;
        UsersUser usersUser = jumpserverCenter.saveUsersUser(ocUser);
        bindUserGroups(usersUser, serverGroupList);
    }

    /**
     * 绑定用户-用户组
     *
     * @param usersUser
     * @param serverGroupList
     */
    private void bindUserGroups(UsersUser usersUser, List<OcServerGroup> serverGroupList) {
        for (OcServerGroup ocServerGroup : serverGroupList)
            jumpserverCenter.bindUserGroups(usersUser, ocServerGroup);
    }

    /**
     * 同步资产 包括（主机，服务器组，用户组，授权等）
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> syncAssets() {
        ServerGroupParam.PageQuery pageQuery = new ServerGroupParam.PageQuery();
        pageQuery.setPage(0);
        pageQuery.setLength(2000);
        DataTable<OcServerGroup> table = ocServerGroupService.queryOcServerGroupByParam(pageQuery);
        List<OcServerGroup> serverGroupList = table.getData();
        for (OcServerGroup ocServerGroup : serverGroupList) {
            // 创建资产节点（服务器组）
            AssetsNode assetsNode = saveAssetsNode(ocServerGroup);
            if (assetsNode == null) {
                log.error("Jumpserver 同步节点（服务器组）{} Error !", ocServerGroup.getName());
                continue;
            }
            // 同步资产并绑定 节点
            ocServerService.queryOcServerByServerGroupId(ocServerGroup.getId()).forEach(e -> saveAssets(e, assetsNode));

        }
        return new BusinessWrapper<Boolean>(true);
    }

    private AssetsNode saveAssetsNode(OcServerGroup ocServerGroup) {
        // 创建资产节点（服务器组）
        AssetsNode assetsNode = jumpserverCenter.createAssetsNode(ocServerGroup);
        if (assetsNode == null) {
            log.error("Jumpserver 同步节点（服务器组）{} Error !", ocServerGroup.getName());
            return null;
        }
        // 创建用户组
        UsersUsergroup usersUsergroup = jumpserverCenter.saveUsersUsergroup(ocServerGroup);
        // 创建授权并绑定 节点，用户组，系统账户
        PermsAssetpermission permsAssetpermission = jumpserverCenter.createPermsAssetpermission(ocServerGroup, assetsNode, usersUsergroup);
        return assetsNode;
    }

    /**
     * 保存资产，并执行系统账户授权
     *
     * @param ocServer
     * @param assetsNode
     */
    private void saveAssets(OcServer ocServer, AssetsNode assetsNode) {
        AssetsAsset assetsAsset = createAssetsAsset(ocServer, "");
        // 绑定资产到节点（节点就是oc服务器组）
        jumpserverCenter.bindAssetsAssetNodes(assetsAsset, assetsNode);
        // 资产绑定系统账户
        jumpserverCenter.bindAvssetsSystemuserAssets(assetsAsset.getId());
    }


    /**
     * 新增资产
     *
     * @param ocServer
     */
    @Override
    public void addAssets(OcServer ocServer) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocServer.getServerGroupId());
        AssetsNode assetsNode = saveAssetsNode(ocServerGroup);
        if (assetsNode == null) {
            log.error("Jumpserver 同步节点（服务器组）{} Error !", ocServerGroup.getName());
            return;
        }
        saveAssets(ocServer, assetsNode);
    }

    /**
     * 创建资产（主机）
     * 需要解决一个问题 公网链接服务器删除内网服务器
     *
     * @param ocServer
     * @return
     */
    private AssetsAsset createAssetsAsset(OcServer ocServer, String comment) {
        if (StringUtils.isEmpty(comment))
            comment = "";
        String adminUserId = getAdminuserId();
        if (StringUtils.isEmpty(adminUserId))
            return null;
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        // 更新服务器信息
        if (assetsAsset != null) {
            if (!StringUtils.isEmpty(comment)) {
                assetsAsset.setComment(comment);
            } else {
                assetsAsset.setComment(ocServer.getComment());
            }
            if (!StringUtils.isEmpty(ocServer.getPublicIp()))
                assetsAsset.setComment(Joiner.on("").join(assetsAsset.getComment(), "(pubIp:", ocServer.getPublicIp(), ")"));
            assetsAsset.setHostname(getHostName(ocServer));
            //assetsAssetDO.setCreated_by("oc auto");
            jumpserverCenter.updateAssetsAsset(assetsAsset);
        } else {
            String manageIp = serverAttributeFacade.getManageIp(ocServer);
            assetsAsset = AssetsAssetBuilder.build(ocServer, manageIp, adminUserId, getHostName(ocServer));
            if (!StringUtils.isEmpty(comment))
                assetsAsset.setComment(comment);
            jumpserverCenter.addAssetsAsset(assetsAsset);
        }
        return assetsAsset;
    }


    private AssetsAsset getAssetsAsset(OcServer ocServer) {
        String manageIp = serverAttributeFacade.getManageIp(ocServer);
        AssetsAsset assetsAsset = jumpserverCenter.queryAssetsAssetByIp(manageIp);
        if (assetsAsset != null) return assetsAsset;

        // 主机名查询服务器
        assetsAsset = jumpserverCenter.queryAssetsAssetByHostname(getHostName(ocServer));
        if (assetsAsset != null) {
            // 更新IP 此服务器可能是使用公网连接
            assetsAsset.setIp(manageIp);
            jumpserverCenter.updateAssetsAsset(assetsAsset);
            return assetsAsset;
        }
        return assetsAsset;
    }

    /**
     * 取主机名称 序号填充对齐
     * 例如 当前主机数量为80台, 第一台序号为 01
     *
     * @param ocServer
     * @return
     */
    private String getHostName(OcServer ocServer) {
        int serverCount = ocServerService.countByServerGroupId(ocServer.getServerGroupId());
        String format = Joiner.on("").join("%0", String.valueOf(serverCount).length(), "d");
        String sn = String.format(format, ocServer.getSerialNumber());
        return Joiner.on("-").join(serverFacade.acqHostname(ocServer), sn);
    }


    @Override
    public boolean activeUsersUser(String username, boolean active) {
        return jumpserverCenter.activeUsersUser(username, active);
    }

    @Override
    public boolean delUsersUser(String username) {
        return jumpserverCenter.delUsersUser(username);
    }

    @Override
    public boolean updateUsersUser(OcUser ocUser) {
        return jumpserverCenter.updateUsersUser(ocUser);
    }

    @Override
    public boolean pushKey(OcUserVO.User user) {
        OcUser ocUser = BeanCopierUtils.copyProperties(user, OcUser.class);
        String key = CredentialType.SSH_PUB_KEY.getName();
        if (!user.getCredentialMap().containsKey(key))
            return false;
        OcUserCredentialVO.UserCredential credential = user.getCredentialMap().get(key);
        return jumpserverCenter.pushKey(ocUser, credential);
    }

    @Override
    public DataTable<JumpserverUsersUserVO.UsersUser> fuzzyQueryUserPage(UsersUserPageParam.PageQuery pageQuery) {
        DataTable<UsersUser> table = usersUserService.fuzzyQueryUsersUserPage(pageQuery);
        List<JumpserverUsersUserVO.UsersUser> page = BeanCopierUtils.copyListProperties(table.getData(), JumpserverUsersUserVO.UsersUser.class);
        //DataTable<JumpserverUsersUserVO.UsersUser> dataTable = new DataTable<>(page, table.getTotalNum());
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
    public  BusinessWrapper<Boolean> authAdmin(String usersUserId){
        return jumpserverCenter.authAdmin(usersUserId);
    }

    @Override
    public   BusinessWrapper<Boolean> revokeAdmin(String usersUserId){
        return jumpserverCenter.revokeAdmin(usersUserId);
    }

    private String getAdminuserId() {
        JumpserverSettingsVO.Settings settings = querySettings();
        return settings.getAssetsAdminuserId();
    }

}
