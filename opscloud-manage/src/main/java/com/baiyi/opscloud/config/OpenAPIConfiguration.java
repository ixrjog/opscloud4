package com.baiyi.opscloud.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author baiyi
 * @Date 2023/4/6 13:47
 * @Version 1.0
 */
@Configuration
public class OpenAPIConfiguration {

    @Value("${spring.application.version}")
    private String version;

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Bean
    public OpenAPI restfulOpenAPI() {
        License license = new License().name("APACHE LICENSE, VERSION 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        return new OpenAPI()
                .info(new Info()
                        .title(String.format("Opscloud %s", version))
                        .description("Opscloud OpenAPI接口文档")
                        .version(version)
                        .license(license));
    }

}
