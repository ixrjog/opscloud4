package com.sdg.cmdb.domain.jumpserver;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JumpserverVO implements Serializable {
    private static final long serialVersionUID = 7989376182485383420L;

    private String host;

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

    /**
     * 管理员
     */
    private List<UsersUserDO> administrators;

    /**
     * 终端列表 coco
     */
    private List<TerminalDO> terminals;

    private List<TerminalSessionDO> terminalSessions;


}
