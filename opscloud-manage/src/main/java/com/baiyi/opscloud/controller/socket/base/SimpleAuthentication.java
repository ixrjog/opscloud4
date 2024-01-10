package com.baiyi.opscloud.controller.socket.base;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.domain.model.message.SimpleState;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.user.UserTokenService;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/28 10:19 上午
 * @Version 1.0
 */
@Component
public class SimpleAuthentication {

    protected static final String MESSAGE_STATE = "state";

    private static UserTokenService userTokenService;

    private static UserService userService;

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        SimpleAuthentication.userTokenService = userTokenService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        SimpleAuthentication.userService = userService;
    }

    public String hasLogin(ILoginMessage loginMessage) {
        if (StringUtils.isBlank(loginMessage.getToken())) {
            throw new AuthenticationException("鉴权失败: Token为Null!");
        }
        UserToken userToken = userTokenService.getByValidToken(loginMessage.getToken());
        if (userToken == null) {
            throw new AuthenticationException("鉴权失败: 无效的Token!");
        }
        // 设置当前会话用户身份
        SessionHolder.setUsername(userToken.getUsername());
        User user = userService.getByUsername(userToken.getUsername());
        if (user == null) {
            throw new AuthenticationException("鉴权失败: 无效的用户!");
        }
        SessionHolder.setUserId(user.getId());
        return userToken.getUsername();
    }

    protected String getState(String message) {
        SimpleState ss = new GsonBuilder().create().fromJson(message, SimpleState.class);
        return ss.getState();
    }

}
