package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.PasswordUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.AccessToken;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.user.AccessTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import com.baiyi.opscloud.facade.user.UserFacade;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.packer.user.UserAccessTokenPacker;
import com.baiyi.opscloud.packer.user.UserPacker;
import com.baiyi.opscloud.service.user.AccessTokenService;
import com.baiyi.opscloud.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:38 上午
 * @Version 1.0
 */
@Service
public class UserFacadeImpl implements UserFacade {

    @Resource
    private UserService userService;

    @Resource
    private AccessTokenService accessTokenService;

    @Resource
    private UserAccessTokenPacker userAccessTokenPacker;

    @Resource
    private UserPacker userPacker;

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Resource
    private ServerFacade serverFacade;

    @Override
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<User> table = userService.queryPageByParam(pageQuery);
        return new DataTable<>(userPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public UserVO.User getUserDetails() {
        User user = userService.getByUsername(SessionUtil.getUsername());
        return userPacker.wrap(user);
    }

    @Override
    public void addUser(UserVO.User user) {
        User pre = userPacker.toDO(user);
        if (StringUtils.isEmpty(pre.getPassword()))
            throw new CommonRuntimeException("密码不能为空");
        userService.add(pre);
    }

    @Override
    public void updateUser(UserVO.User user) {
        User pre = userPacker.toDO(user);
        userService.updateBySelective(pre);
    }

    @Override
    public ServerTreeVO.ServerTree queryUserServerTree(ServerGroupParam.UserServerTreeQuery queryParam) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        queryParam.setUserId(user.getId());
        return serverGroupFacade.queryServerTree(queryParam, user);
    }

    @Override
    public DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        IUserBusinessPermissionPageQuery iQuery = UserBusinessPermissionFactory.getByBusinessType(pageQuery.getBusinessType());
        if (iQuery != null)
            return iQuery.queryUserBusinessPermissionPage(pageQuery);
        throw new CommonRuntimeException(ErrorEnum.USER_BUSINESS_TYPE_ERROR);
    }

    @Override
    public DataTable<ServerVO.Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery queryParam) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        queryParam.setUserId(user.getId());
        return serverFacade.queryUserRemoteServerPage(queryParam);
    }

    @Override
    public AccessTokenVO.AccessToken grantUserAccessToken(AccessTokenVO.AccessToken accessToken) {
        AccessToken at = AccessToken.builder()
                .username(SessionUtil.getUsername())
                .tokenId(IdUtil.buildUUID())
                .token(PasswordUtil.getRandomPW(32))
                .expiredTime(accessToken.getExpiredTime())
                .comment(accessToken.getComment())
                .build();
        accessTokenService.add(at);
        return userAccessTokenPacker.wrapToVO(at, false);
    }

    @Override
    public void revokeUserAccessToken(String tokenId) {
        AccessToken at = accessTokenService.getByTokenId(tokenId);
        if (at == null) return;
        at.setValid(false);
        accessTokenService.update(at);
    }

}
