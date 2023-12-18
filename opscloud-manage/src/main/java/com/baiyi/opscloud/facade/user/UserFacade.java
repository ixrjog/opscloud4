package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.param.user.AccessTokenParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.user.AccessManagementVO;
import com.baiyi.opscloud.domain.vo.user.AccessTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:38 上午
 * @Version 1.0
 */
public interface UserFacade {

    DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery);

    default UserVO.User getUserDetails() {
        return getUserDetailsByUsername(StringUtils.EMPTY);
    }

    UserVO.User getUserDetailsByUsername(String username);

    /**
     * 从数据源实例中同步用户与用户组关系
     *
     * @return
     */
    void syncUserPermissionGroupForAsset();

    UserVO.User addUser(UserParam.CreateUser createUser);

    void updateUser(UserParam.UpdateUser updateUser);

    void setUserActive(String username);

    void deleteUser(Integer id);

    ServerTreeVO.ServerTree queryUserServerTree(ServerGroupParam.UserServerTreeQuery queryParam);

    DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    DataTable<ServerVO.Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery queryParam);

    /**
     * 授予用户AccessToken
     *
     * @param accessToken
     * @return
     */
    AccessTokenVO.AccessToken grantUserAccessToken(AccessTokenParam.ApplicationAccessToken applicationAccessToken);

    /**
     * 撤销用户AccessToken
     */
    void revokeUserAccessToken(String tokenId);

    DataTable<UserVO.User> queryBusinessPermissionUserPage(UserBusinessPermissionParam.BusinessPermissionUserPageQuery pageQuery);

    List<AccessManagementVO.XAccessManagement> queryAmsUser(String username, String amType);

    /**
     * 用户查询MFA
     *
     * @return
     */
    UserVO.UserMFA getUserMFA();

    /**
     * 用户重置MFA
     *
     * @return
     */
    UserVO.UserMFA resetUserMFA();

    UserVO.UserMFA bindUserMFA(String otp);

    UserVO.UserIAMMFA getUserIAMMFA();

}
