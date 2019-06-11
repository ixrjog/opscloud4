package com.sdg.cmdb.service;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ApiServiceTest {

    private String ak = "87014bafb561dc893f656320a6e5f716";

    @Autowired
    private ApiService apiService;


    @Test
    public void test1() {
       System.err.println(JSON.toJSONString(apiService.getUser("22","baiyi111")));
    }

    @Test
    public void test2() {
        UserDO userDO = new UserDO();
        userDO.setUsername("ForTestEmplSys");
        //userDO.setMobile("00");
        System.err.println(JSON.toJSONString(apiService.getUser(ak,userDO)));
    }

    @Test
    public void test3() {
        UserVO userVO = new UserVO();
        userVO.setUsername("test003");
        userVO.setMail("test003@gegejia.com");
        userVO.setDisplayName("测试3");
        userVO.setMobile("13456768000");
        userVO.setUserpassword("121321412");
        userVO.setDev(true);
        System.err.println(JSON.toJSONString(apiService.createUser(ak,userVO)));
    }

    @Test
    public void test4() {

        // {"displayName":"鲈鱼","id":2428,"mail":"luyu@gegejia.com","mobile":"18658816215","username":"ludongxu","userpassword":"12345678"}

        UserVO userVO = new UserVO();
        userVO.setId(2428);
        userVO.setUsername("ludongxu");
        userVO.setMobile("18658816215");
        userVO.setUserpassword("12345678");
        //userVO.setUserpassword("11111111111111111111");
        System.err.println(JSON.toJSONString(apiService.updateUser(ak,userVO)));
    }


    @Test
    public void test5() {
        System.err.println(JSON.toJSONString(apiService.delUser(ak,"test003")));
    }



}
