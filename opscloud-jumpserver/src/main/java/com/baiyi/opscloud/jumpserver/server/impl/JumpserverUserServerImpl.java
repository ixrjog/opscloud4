package com.baiyi.opscloud.jumpserver.server.impl;

import com.baiyi.opscloud.jumpserver.config.JumpserverConfig;
import com.baiyi.opscloud.jumpserver.server.JumpserverUserServer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/9 11:39 上午
 * @Version 1.0
 */
@Component("JumpserverAccountServer")
public class JumpserverUserServerImpl implements JumpserverUserServer {

    @Resource
    private JumpserverConfig jumpserverConfig;


//    public void getUser(String username){
//        try {
//            UserApi userApi = new UserApi(jumpserverConfig);
//
//
//
//            User user = new User();
//            String uuid = UUIDUtils.convertUUID(usersUserDO.getId());
//            user.setId(uuid);
//            user.setPublicKey(userDO.getRsaKey());
//            jmsUsersService.userPubkeyReset(user);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



}
