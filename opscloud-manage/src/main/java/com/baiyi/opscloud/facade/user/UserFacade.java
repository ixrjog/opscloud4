package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.user.AccessTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:38 上午
 * @Version 1.0
 */
public interface UserFacade {

    DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery);

    UserVO.User getUserDetails();

    void addUser(UserVO.User user);

    void updateUser(UserVO.User user);

    ServerTreeVO.ServerTree queryUserServerTree(ServerGroupParam.UserServerTreeQuery queryParam);

    DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    DataTable<ServerVO.Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery queryParam);


    /**
     * 授予用户AccessToken
     * @param accessToken
     * @return
     */
    AccessTokenVO.AccessToken grantUserAccessToken(AccessTokenVO.AccessToken accessToken);

    /**
     * 撤销用户AccessToken
     */
    void revokeUserAccessToken(String tokenId);
}
