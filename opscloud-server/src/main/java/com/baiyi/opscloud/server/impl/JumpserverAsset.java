package com.baiyi.opscloud.server.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.*;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverSettingsVO;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.builder.AssetsAssetBuilder;
import com.baiyi.opscloud.server.decorator.JumpserverSettingsDecorator;
import com.baiyi.opscloud.service.jumpserver.AssetsAssetNodesService;
import com.baiyi.opscloud.service.jumpserver.AssetsAssetService;
import com.baiyi.opscloud.service.jumpserver.AssetsSystemuserAssetsService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/1 4:12 下午
 * @Version 1.0
 */
@Slf4j
@Component("JumpserverAsset")
public class JumpserverAsset extends BaseServer implements IServer {

    @Resource
    private JumpserverCenter jumpserverCenter;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private JumpserverSettingsDecorator jumpserverSettingsDecorator;

    @Resource
    private AssetsAssetNodesService assetsAssetNodesService;

    @Resource
    private AssetsSystemuserAssetsService assetsSystemuserAssetsService;

    @Resource
    private AssetsAssetService assetsAssetService;

    @Override
    public boolean sync() {
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
            getGroupServerList(ocServerGroup.getId()).forEach(e -> saveAssets(e, assetsNode));
        }
        return true;
    }


    @Override
    public boolean create(OcServer ocServer) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocServer.getServerGroupId());
        AssetsNode assetsNode = saveAssetsNode(ocServerGroup);
        if (assetsNode == null) {
            log.error("Jumpserver 同步节点（服务器组）{} Error !", ocServerGroup.getName());
            return false;
        }
        saveAssets(ocServer, assetsNode);
        return true;
    }

    @Override
    public boolean disable(OcServer ocServer) {
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        if (assetsAsset == null) return true;
        assetsAsset.setIsActive(false);
        jumpserverCenter.updateAssetsAsset(assetsAsset);
        return true;
    }

    @Override
    public boolean enable(OcServer ocServer) {
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        if (assetsAsset == null) return false;
        assetsAsset.setIsActive(true);
        jumpserverCenter.updateAssetsAsset(assetsAsset);
        return true;
    }

    @Override
    public boolean remove(OcServer ocServer) {
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        if (assetsAsset == null) return true;
        // 删除资产的节点绑定关系
        AssetsAssetNodes assetsAssetNodes = assetsAssetNodesService.queryAssetsAssetNodesByAssetId(assetsAsset.getId());
        if (assetsAssetNodes != null)
            assetsAssetNodesService.delAssetsAssetNodes(assetsAssetNodes.getId());
        // 删除资产的系统账户绑定关系(批量删除)
        assetsSystemuserAssetsService.deleteAssetsSystemuserAssetsByAssetId(assetsAsset.getId());
        // 删除资产
        assetsAssetService.deleteAssetsAssetById(assetsAsset.getId());
        return true;
    }

    @Override
    public boolean update(OcServer ocServer) {
        AssetsAsset assetsAsset = createAssetsAsset(ocServer, null);
        return assetsAsset != null;
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
        jumpserverCenter.createPermsAssetpermission(ocServerGroup, assetsNode, usersUsergroup);
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
                if (!StringUtils.isEmpty(ocServer.getComment()))
                    assetsAsset.setComment(ocServer.getComment());
            }
            if (!StringUtils.isEmpty(ocServer.getPublicIp()))
                assetsAsset.setComment(Joiner.on("").join(assetsAsset.getComment(), "(pubIp:", ocServer.getPublicIp(), ")"));
            assetsAsset.setHostname(getHostName(ocServer));
            //assetsAssetDO.setCreated_by("oc auto");
            jumpserverCenter.updateAssetsAsset(assetsAsset);
        } else {
            String manageIp = getManageIp(ocServer);
            assetsAsset = AssetsAssetBuilder.build(ocServer, manageIp, adminUserId, getHostName(ocServer));
            if (!StringUtils.isEmpty(comment))
                assetsAsset.setComment(comment);
            jumpserverCenter.addAssetsAsset(assetsAsset);
        }
        return assetsAsset;
    }

    private String getAdminuserId() {
        JumpserverSettingsVO.Settings settings = querySettings();
        return settings.getAssetsAdminuserId();
    }

    private JumpserverSettingsVO.Settings querySettings() {
        JumpserverSettingsVO.Settings settings = new JumpserverSettingsVO.Settings();
        return jumpserverSettingsDecorator.decorator(settings);
    }

    private AssetsAsset getAssetsAsset(OcServer ocServer) {
        String manageIp = getManageIp(ocServer);
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

}
