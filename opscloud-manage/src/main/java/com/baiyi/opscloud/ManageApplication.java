package com.baiyi.opscloud;


import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.time.Instant;

/**
 *
 * 欢迎进入白衣的运维世界，从这里开始你将踏入成神之路
 *
 *
 * @Author baiyi
 * @Date 2019/12/25 4:00 下午
 * @Version 1.0
 */

@EnableTransactionManagement
@SpringBootApplication
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
        Instant inst = Instant.now();
        SpringApplication.run(ManageApplication.class, args);
        log.info("OPSCLOUD 4 IaC 基础架构即代码 <Spring Boot {}>", SpringBootVersion.getVersion());
        log.info("启动成功! 耗时:{}/s", Duration.between(inst, Instant.now()).getSeconds());
        System.setProperty("druid.mysql.usePingMethod","false");
        log.info("修改参数: druid.mysql.usePingMethod=false");
    }

}
