package com.baiyi.opscloud;

import com.baiyi.opscloud.common.util.JSONUtil;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author baiyi
 * @Date 2020/1/6 8:05 下午
 * @Version 1.0
 */
@SpringBootTest(classes = ManageApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"ssh.shell.port=2223"})
@AutoConfigureMockMvc
// 单元测试读取dev配置文件
@ActiveProfiles(profiles = "dev")
@Retryable
public class BaseUnit {

    public void print(Object t) {
        if (t instanceof String) {
            System.out.println((String) t);
        } else {
            System.out.println(JSONUtil.writeValueAsString(t));
        }
    }

    public void print(Exception e){
        System.out.println(e.getMessage());
    }

}