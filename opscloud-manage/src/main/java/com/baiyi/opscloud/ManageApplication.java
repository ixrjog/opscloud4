package com.baiyi.opscloud;


import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 代码已经完成，吾辈继续努力
 *
 * @Author baiyi
 * @Date 2019/12/25 4:00 下午
 * @Version 1.0
 */
@EnableTransactionManagement
@SpringBootApplication(exclude = { SecurityFilterAutoConfiguration.class})
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@EnableCaching
@EnableAsync
@EnableRetry
@ComponentScan(
        basePackages = "com.baiyi"
)
public class ManageApplication {

    private static final Logger log = LoggerFactory.getLogger(ManageApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
        log.info("Opscloud 4 <Spring Boot {}>", SpringBootVersion.getVersion());
        System.setProperty("druid.mysql.usePingMethod", "false");
        log.info("Swagger UI page https://server:port/swagger-ui.html");
    }

}