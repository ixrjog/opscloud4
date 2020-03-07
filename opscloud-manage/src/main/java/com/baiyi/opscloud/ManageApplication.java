package com.baiyi.opscloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author baiyi
 * @Date 2019/12/25 4:00 下午
 * @Version 1.0
 */

@EnableTransactionManagement
@MapperScan(basePackages = "com.baiyi.opscloud.mapper")
//=======
////@MapperScan(basePackages = "com.baiyi.opscloud.mapper")
//>>>>>>> 4b52d713737030f5a65980e2fc9211aa76377f25
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@EnableCaching
@ComponentScan(
        basePackages = "com.baiyi"
)
public class ManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }
}
