package com.baiyi.opscloud.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * @author baiyi
 */
@Configuration
@MapperScan(
        basePackages = {"com.baiyi.opscloud.mapper.opscloud"},
        sqlSessionTemplateRef = "opscloudSqlSessionTemplate"
)
public class DatasourceConfiguration {

    @Bean
    @Primary
    public SqlSessionTemplate opscloudSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(opscloudDataSourceSqlSessionFactory());
    }

    @Bean
    @Primary
    public DataSourceTransactionManager opscloudTransactionManager() {
        return new DataSourceTransactionManager(opscloudDataSource());
    }

    @Bean
    @Primary
    public SqlSessionFactory opscloudDataSourceSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(opscloudDataSource());
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/opscloud/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.druid.opscloud")
    public DataSource opscloudDataSource() {
        return DruidDataSourceBuilder.create()
                        .build();
    }
}
