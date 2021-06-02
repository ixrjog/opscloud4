package com.baiyi.caesar.facade.auth.impl;

import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.generator.caesar.UserToken;
import com.baiyi.caesar.domain.vo.auth.LogVO;
import com.baiyi.caesar.facade.auth.UserTokenFacade;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.service.user.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/5/15 9:59 上午
 * @Version 1.0
 */
@Service
@Slf4j
public class UserTokenFacadeImpl implements UserTokenFacade {

    @Resource
    private UserTokenService userTokenService;

    @Resource
    private UserService userService;

    @Override
    public LogVO.Login userLogin(User user) {
        revokeUserToken(user.getUsername());
        UserToken userToken = grantUserToken(user.getUsername());
        user.setLastLogin(new Date());
        userService.update(user); // 更新用户登录事件
        return LogVO.Login.builder()
                .name(user.getDisplayName())
                .uuid(user.getUuid())
                .token(userToken.getToken())
                .build();
    }

    @Override
    public void revokeUserToken(String username) {
        userTokenService.queryByVaildTokenByUsername(username).forEach(e -> {
            e.setValid(false);
            userTokenService.update(e);
        });
    }

    @Override
    public UserToken grantUserToken(String username) {
        UserToken userToken = UserToken.builder()
                .valid(true)
                .username(username)
                .token(IdUtil.buildUUID())
                .build();
        userTokenService.add(userToken);
        return userToken;
    }
}
