package com.baiyi.opscloud.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.user.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/28 10:19 上午
 * @Version 1.0
 */
@Component
public class BaseWebSocketController {

    protected static final String MESSAGE_STATE = "state";

    private static UserTokenService userTokenService;

    private static UserService userService;

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        BaseWebSocketController.userTokenService = userTokenService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        BaseWebSocketController.userService = userService;
    }

    protected String authentication(ILoginMessage loginMessage) {
        UserToken userToken = userTokenService.getByVaildToken(loginMessage.getToken());
        if (userToken == null) return null;
        SessionUtil.setUsername(userToken.getUsername()); // 设置当前会话用户身份
        User user = userService.getByUsername(userToken.getUsername());
        SessionUtil.setUserId(user.getId());
        return userToken.getUsername();
    }

    protected String getSatae(String message){
        JSONObject jsonObject = JSON.parseObject(message);
        return jsonObject.getString(MESSAGE_STATE);
    }
}
