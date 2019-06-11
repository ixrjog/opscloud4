package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JumpserverVO implements Serializable {
    private static final long serialVersionUID = 7989376182485383420L;

    private String host;
    // 去除协议
    private String hostname;

    // SSH
    private String cocoHost;

    // TODO 当前系统账户
    private AssetsSystemuserDO assetsSystemuserDO;
    // TODO 当前资产管理账户
    private AssetsAdminuserDO assetsAdminuserDO;

    // 用户
    private int localUsersTotal;
    private int jumpserverUsersTotal;

    // 服务器
    private int localServersTotal;
    private int jumpserverAssetsTotal;

    // 授权数量
    private int permsTotal;

    public void setHost(String host) {
        this.host = host;
        try {
            String s[] = host.split("\\/");
            this.hostname = s[s.length - 1];
        } catch (Exception e) {

        }
    }

    /**
     * 管理员
     */
    private List<UsersUserDO> administrators;

    /**
     * 闲置用户
     */
    private List<UsersUserDO> inactiveUsers;

    /**
     * 闲置资产
     */
    private List<AssetsAssetDO> inactiveAssets;

    /**
     * 终端列表 coco
     */
    private List<TerminalDO> terminals;

    private List<TerminalSessionDO> terminalSessions;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
