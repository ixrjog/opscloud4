package com.baiyi.opscloud.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

import javax.sql.DataSource;


/**
 * @Author 修远
 * @Date 2021/7/2 2:38 下午
 * @Since 1.0
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Slf4j
@Configuration
public class InitialDataSourceConfiguration implements ApplicationContextAware {

    @Resource
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        try {
            DataSource dataSource = applicationContext.getBean("dataSource", DataSource.class);
            dataSource.getConnection().close();
            log.info("Start verification MySQL connection succeeded");
        } catch (Exception e) {
            log.error("Start verification MySQL unable to connect: {}", e.getMessage());
            /*
             * 停止项目启动
             */
            configurableApplicationContext.close();
        }
    }

}
