package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;
import com.baiyi.opscloud.domain.vo.jumpserver.*;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:18 下午
 * @Version 1.0
 */
public interface JumpserverFacade {

    BusinessWrapper<Boolean> syncUsers();

    BusinessWrapper<Boolean> setUserActive(String id);

    /**
     * 同步资产
     *
     * @return
     */
    BusinessWrapper<Boolean> syncAssets();

    /**
     * 同步服务器到资产
     *
     * @param ocServer
     */
    void addAssets(OcServer ocServer);




    boolean activeUsersUser(String username, boolean active);

    boolean delUsersUser(String username);

    boolean updateUsersUser(OcUser ocUser);

    /**
     * 通过API推送用户公钥
     *
     * @param user
     * @return
     */
    boolean pushKey(OcUserVO.User user);


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
