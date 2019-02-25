package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.jumpserver.AssetsAdminuserDO;
import com.sdg.cmdb.domain.jumpserver.AssetsSystemuserDO;
import com.sdg.cmdb.domain.jumpserver.JumpserverVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.util.List;

public interface JumpserverService {

    /**
     * 同步资产
     *
     * @return
     */
    BusinessWrapper<Boolean> syncAssets();

    /**
     * 同步服务到资产
     * @param serverDO
     */
    void addAssets(ServerDO serverDO);

    /**
     * 同步用户
     *
     * @return
     */
    BusinessWrapper<Boolean> syncUsers();

    /**
     * 查询系统用户
     *
     * @param name
     * @return
     */
    List<AssetsSystemuserDO> queryAssetsSystemuser(String name);

    /**
     * 查询管理员账户
     *
     * @param name
     * @return
     */
    List<AssetsAdminuserDO> queryAssetsAdminuser(String name);

    BusinessWrapper<Boolean> saveAssetsSystemuser(String id);

    BusinessWrapper<Boolean> saveAssetsAdminuser(String id);

    /**
     * 查询Jumpserver信息
     *
     * @return
     */
    JumpserverVO getJumpserver();

    BusinessWrapper<Boolean> authAdmin(long userId);

    /**
     * 用户绑定用户组
     *
     * @param userDO
     * @param serverGroupDO
     */
    void bindUserGroup(UserDO userDO, ServerGroupDO serverGroupDO);

    /**
     * 用户解除绑定用户组
     *
     * @param userDO
     * @param serverGroupDO
     */
    void unbindUserGroup(UserDO userDO, ServerGroupDO serverGroupDO);
}
