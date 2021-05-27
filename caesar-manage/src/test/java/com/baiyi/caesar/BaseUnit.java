package com.baiyi.caesar;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author baiyi
 * @Date 2020/1/6 8:05 下午
 * @Version 1.0
 */
@SpringBootTest(classes = ManageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// 单元测试读取dev配置文件
@ActiveProfiles(profiles = "dev")
@Retryable
public class BaseUnit {

}
