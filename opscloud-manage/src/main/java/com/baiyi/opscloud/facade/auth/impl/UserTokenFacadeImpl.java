package com.baiyi.opscloud.facade.auth.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import com.baiyi.opscloud.facade.auth.UserTokenFacade;
import com.baiyi.opscloud.service.user.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2021/5/15 9:59 上午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenFacadeImpl implements UserTokenFacade {

    private final UserTokenService userTokenService;

    @Override
    public LogVO.Login userLogin(User user) {
        revokeUserToken(user.getUsername());
        UserToken userToken = grantUserToken(user.getUsername());
        return LogVO.Login.builder()
                .name(user.getDisplayName())
                .uuid(user.getUuid())
                .token(userToken.getToken())
                .build();
    }

    @Override
    public void revokeUserToken(String username) {
        userTokenService.queryByValidTokenByUsername(username).forEach(e -> {
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
