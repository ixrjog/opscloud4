package com.baiyi.caesar.controller.base;

import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.generator.caesar.UserToken;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.service.user.UserTokenService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/28 10:19 上午
 * @Version 1.0
 */
@Component
public class BaseWebSocketController {

    @Resource
    private UserTokenService userTokenService;

    @Resource
    private UserService userService;

    protected String authentication(String token) {
        UserToken userToken = userTokenService.getByVaildToken(token);
        if (userToken == null) return null;
        SessionUtil.setUsername(userToken.getUsername()); // 设置当前会话用户身份
        User user = userService.getByUsername(userToken.getUsername());
        SessionUtil.setUserId(user.getId());
        return userToken.getUsername();
    }
}
