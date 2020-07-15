package com.baiyi.opscloud.server.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUsergroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverSettingsVO;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.builder.AssetsAssetBuilder;
import com.baiyi.opscloud.server.decorator.JumpserverSettingsDecorator;
import com.baiyi.opscloud.service.jumpserver.AssetsAssetService;
import com.baiyi.opscloud.service.jumpserver.AssetsNodeService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private AssetsAssetService assetsAssetService;

    @Resource
    private AssetsNodeService assetsNodeService;

    @Override
    public void sync() {
        Map<String, AssetsAsset> assetMap = getAssetMap();
        getServerGroupList().forEach(e -> {
            // 创建资产节点（服务器组）
            AssetsNode assetsNode = saveAssetsNode(e);
            if (assetsNode == null) {
                log.error("Jumpserver 同步节点（服务器组）{} Error !", e.getName());
            } else {
                // 同步资产并绑定 节点
                getGroupServerList(e.getId()).forEach(s -> {
                    try {
                        AssetsAsset assetsAsset = saveAssets(s, assetsNode);
                        assetMap.remove(assetsAsset.getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
        deleteAssetByMap(assetMap);
    }

    private void deleteAssetByMap(Map<String, AssetsAsset> assetMap) {
        assetMap.keySet().forEach(k -> deleteAssetsAsset(assetMap.get(k)));
    }

    private Map<String, AssetsAsset> getAssetMap() {
        List<AssetsAsset> assets = assetsAssetService.queryAll();
        if (CollectionUtils.isEmpty(assets)) return Maps.newHashMap();
        return assets.stream().collect(Collectors.toMap(AssetsAsset::getId, a -> a, (k1, k2) -> k1));
    }

    @Override
    public Boolean create(OcServer ocServer) {
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
    public Boolean disable(OcServer ocServer) {
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        if (assetsAsset == null) return true;
        assetsAsset.setIsActive(false);
        jumpserverCenter.updateAssetsAsset(assetsAsset);
        return true;
    }

    @Override
    public Boolean enable(OcServer ocServer) {
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        if (assetsAsset == null) return false;
        assetsAsset.setIsActive(true);
        jumpserverCenter.updateAssetsAsset(assetsAsset);
        return true;
    }

    @Override
    public Boolean remove(OcServer ocServer) {
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        if (assetsAsset == null) return true;
        // 删除资产的节点绑定关系
        return deleteAssetsAsset(assetsAsset);

    }

    private boolean deleteAssetsAsset(AssetsAsset assetsAsset) {
        // 删除资产的节点绑定关系
        return jumpserverCenter.delAssetsAsset(assetsAsset.getId());
    }

    @Override
    public Boolean update(OcServer ocServer) {
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
    private AssetsAsset saveAssets(OcServer ocServer, AssetsNode assetsNode) {
        AssetsAsset assetsAsset = createAssetsAsset(ocServer, "");
        // 绑定资产到节点（节点就是oc服务器组）
        jumpserverCenter.bindAssetsAssetNodes(assetsAsset, assetsNode);
        // 资产绑定系统账户
        jumpserverCenter.bindAvssetsSystemuserAssets(assetsAsset.getId());
        return assetsAsset;
    }

    /**
     * 创建资产（主机）
     * 需要解决一个问题 公网链接服务器删除内网服务器
     *
     * @param ocServer
     * @return
     */
    private AssetsAsset createAssetsAsset(OcServer ocServer, String comment) {
        String adminUserId = getAdminuserId();
        if (StringUtils.isEmpty(adminUserId))
            return null;
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        String pubIp = StringUtils.isEmpty(ocServer.getPublicIp()) ? null : "(pubIp:" + ocServer.getPublicIp() + ")";
        String assetComment = Joiner.on(" ").skipNulls().join(pubIp, comment, ocServer.getComment());
        // 更新服务器信息
        if (assetsAsset != null) {
            assetsAsset.setComment(assetComment);
            assetsAsset.setHostname(getHostname(ocServer));
            jumpserverCenter.updateAssetsAsset(assetsAsset);
        } else {
            String manageIp = getManageIp(ocServer);
            Integer port = getSSHPort(ocServer);
            assetsAsset = AssetsAssetBuilder.build(ocServer, manageIp, adminUserId, getHostname(ocServer),port, assetComment);
            if (!StringUtils.isEmpty(comment))
                assetsAsset.setComment(comment);
            jumpserverCenter.addAssetsAsset( assetsAsset,acqNodeId(ocServer));
        }
        return assetsAsset;
    }

    private String acqNodeId(OcServer ocServer) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocServer.getServerGroupId());
        AssetsNode assetsNode = assetsNodeService.queryAssetsNodeByValue(ocServerGroup.getName());
        if(assetsNode != null)
            return assetsNode.getId();
        return null;
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
        assetsAsset = jumpserverCenter.queryAssetsAssetByHostname(getHostname(ocServer));
        if (assetsAsset != null) {
            // 更新IP 此服务器可能是使用公网连接
            assetsAsset.setIp(manageIp);
            jumpserverCenter.updateAssetsAsset(assetsAsset);
            return assetsAsset;
        }
        return assetsAsset;
    }

}
