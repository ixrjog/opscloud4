package com.baiyi.opscloud.service;

import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author baiyi
 * @Date 2020/1/6 1:48 下午
 * @Version 1.0
 */
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "dev")
public class OcUserServiceTest {

    @Autowired
    private OcUserService ocUserService;
}
