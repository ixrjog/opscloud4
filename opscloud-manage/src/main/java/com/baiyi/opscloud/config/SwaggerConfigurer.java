package com.baiyi.opscloud.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
//@EnableSwagger2

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfigurer {

//    @Bean
//    public Docket openAPI() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .forCodeGeneration(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.baiyi.opscloud.controller"))
//                .build()
//                .apiInfo(apiInfo());
//    }


    //api接口作者相关信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("opscloud")
                .description("接口文档")
                .version("0.0.1-SNAPSHOT")
                .build();
    }
}