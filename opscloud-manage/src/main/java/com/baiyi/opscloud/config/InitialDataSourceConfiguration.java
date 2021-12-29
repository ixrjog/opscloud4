package com.baiyi.opscloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * @Author 修远
 * @Date 2021/7/2 2:38 下午
 * @Since 1.0
 */

@Slf4j
@Configuration
public class InitialDataSourceConfiguration implements ApplicationContextAware {

    @Resource
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            DataSource dataSource = applicationContext.getBean("opscloudDataSource", DataSource.class);
            dataSource.getConnection().close();
            log.info("校验DataSource[Mysql]连接成功!");
        } catch (Exception e) {
            log.error("校验DataSource[Mysql]连接失败: {}", e.getMessage());
            // 当检测数据库连接失败时, 停止项目启动
            configurableApplicationContext.close();
        }
    }
}
