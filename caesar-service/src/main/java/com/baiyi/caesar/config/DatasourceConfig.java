package com.baiyi.caesar.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
        basePackages  = {"com.baiyi.caesar.mapper.caesar"},
        sqlSessionTemplateRef = "caesarSqlSessionTemplate"
)
public class DatasourceConfig {

    @Bean
    @Primary
    public SqlSessionTemplate caesarSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(caesarDataSourceSqlSessionFactory());
    }

    @Bean
    @Primary
    public DataSourceTransactionManager caesarTransactionManager() {
        return new DataSourceTransactionManager(caesarDataSource());
    }

    @Bean
    @Primary
    public SqlSessionFactory caesarDataSourceSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(caesarDataSource());
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/caesar/*.xml")); // 2. xml 所在路径
        return factoryBean.getObject();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.caesar")
    public DataSourceProperties caesarDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.caesar.configuration")
    public DataSource caesarDataSource() {
        return caesarDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class) // 3. 可以显示指定连接池，也可以不显示指定；即此行代码可以注释掉
                .build();
    }

}
