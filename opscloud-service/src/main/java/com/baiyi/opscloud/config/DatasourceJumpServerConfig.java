package com.baiyi.opscloud.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * @author feixue
 */
@Configuration
@MapperScan(
        basePackages  = {"com.baiyi.opscloud.mapper.jumpserver"},
        sqlSessionTemplateRef = "jumpServerSqlSessionTemplate"
)
public class DatasourceJumpServerConfig {

    @Bean(name="jumpServerSqlSessionTemplate")
    public SqlSessionTemplate jumpServerSqlSessionTemplate(SqlSessionFactory jumpServerDataSourceSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(jumpServerDataSourceSqlSessionFactory);
    }

    @Bean(name="jumpServerTransactionManager")
    public DataSourceTransactionManager jumpServerTransactionManager(DataSource jumpServerDataSource) {
        return new DataSourceTransactionManager(jumpServerDataSource);
    }

    @Bean(name="jumpServerDataSourceSqlSessionFactory")
    public SqlSessionFactory jumpServerDataSourceSqlSessionFactory(DataSource jumpServerDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(jumpServerDataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/jumpserver/*.xml")); // 2. xml 所在路径
        return factoryBean.getObject();
    }

    @Bean(name="jumpServerDataSourceProperties")
    @ConfigurationProperties("app.datasource.jumpserver")
    public DataSourceProperties jumpServerDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name="jumpserver")
    @ConfigurationProperties("app.datasource.jumpserver.configuration")
    public DataSource jumpServerDataSource(DataSourceProperties jumpServerDataSourceProperties) {
        return jumpServerDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class) // 3. 可以显示指定连接池，也可以不显示指定；即此行代码可以注释掉
                .build();
    }

}
