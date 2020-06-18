package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;
import com.baiyi.opscloud.domain.vo.jumpserver.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:18 下午
 * @Version 1.0
 */
public interface JumpserverFacade {

    /**
     * 同步用户
     * @return
     */
    BusinessWrapper<Boolean> syncUsers();

    /**
     * 同步用户
     * @return
     */
    BusinessWrapper<Boolean> syncUserById(String id);


    BusinessWrapper<Boolean> setUserActive(String id);

    BusinessWrapper<Boolean> delUserByUsername(String username);

    /**
     * 同步资产
     *
     * @return
     */
    BusinessWrapper<Boolean> syncAssets();

    BusinessWrapper<Boolean> delAssetById(String assetId);

    DataTable<JumpserverUsersUserVO.UsersUser> fuzzyQueryUserPage(UsersUserPageParam.PageQuery pageQuery);

    DataTable<JumpserverUsersUserVO.UsersUser> fuzzyQueryAdminUserPage(UsersUserPageParam.PageQuery pageQuery);

    DataTable<JumpserverAssetsAssetVO.AssetsAsset> fuzzyQueryAssetPage(AssetsAssetPageParam.PageQuery pageQuery);

    DataTable<JumpserverAssetsNodeVO.AssetsNode> queryAssetsNodePage(AssetsNodePageParam.PageQuery pageQuery);

    JumpserverSettingsVO.Settings querySettings();

    BusinessWrapper<Boolean> saveSettings(JumpserverSettingsVO.Settings settings);

    List<JumpserverTerminalVO.Terminal> queryTerminal();

    BusinessWrapper<Boolean> authAdmin(String usersUserId);

    BusinessWrapper<Boolean> revokeAdmin(String usersUserId);

}
