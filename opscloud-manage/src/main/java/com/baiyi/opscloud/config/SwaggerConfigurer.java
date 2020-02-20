package com.baiyi.opscloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigurer {

    @Bean
    public Docket openAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.baiyi.opscloud.controller"))
                .build()
                .apiInfo(apiInfo());
    }

    //api接口作者相关信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("opscloud")
                .description("接口文档")
                .version("0.0.1-SNAPSHOT")
                .build();
    }
}