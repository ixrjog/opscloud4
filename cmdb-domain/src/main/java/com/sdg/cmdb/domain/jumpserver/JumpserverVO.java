package com.sdg.cmdb.domain.jumpserver;

import lombok.Data;

import java.io.Serializable;

@Data
public class JumpserverVO implements Serializable {
    private static final long serialVersionUID = 7989376182485383420L;

    private String host;

    // TODO 当前系统账户
    private AssetsSystemuserDO assetsSystemuserDO;
    // TODO 当前资产管理账户
    private AssetsAdminuserDO assetsAdminuserDO;
    // 资产数量
    private int assetCnt ;
    // 用户数量
    private int userCnt ;
    // 授权数量
    private int permsCnt;

    private int usergroupCnt;

    private int nodeCnt;

}
