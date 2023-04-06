package com.baiyi.opscloud.user;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.user.UserService;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/18 14:55
 * @Version 1.0
 */
public class UserTest extends BaseUnit {

    @Resource
    private UserService userService;


    @Test
    void userUpdateTest2() {
        User user = userService.getByUsername("baiyi");
        User pre = new User();
        pre.setId(user.getId());
        pre.setUsername("baiyi");
        pre.setEmail("jian.liang@palmpay-inc.com");
        userService.updateBySelective(pre);
    }

    @Test
    void userUpdateTest() {
        List<User> users = userService.listActive();
        print("user size =" + users.size());
        users.forEach(u->{
           if(u.getEmail().endsWith("@transsnet.com")){
               String email = u.getEmail().replace("@transsnet.com","@palmpay-inc.com");
               User pre = new User();
               pre.setId(u.getId());
               pre.setUsername(u.getUsername());
               pre.setEmail(email);
               userService.updateBySelective(pre);
               print(pre);
           }
        });


        // userService.updateBySelective();

    }

}
