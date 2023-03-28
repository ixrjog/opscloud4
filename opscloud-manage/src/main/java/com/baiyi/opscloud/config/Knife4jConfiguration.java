package com.baiyi.opscloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @Author baiyi
 * @Date 2023/3/28 15:46
 * @Version 1.0
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Value("${spring.application.version}")
    private String version;

    private ApiInfo generatepApiInfo() {
        return new ApiInfoBuilder()
                .title(String.format("OPSCLOUD %s", version))
                .description("OPSCLOUD OPEN-API接口文档")
                .version(String.format("%s-RELEASE", version))
                .license("APACHE LICENSE, VERSION 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(new Contact("白衣", "https://github.com/ixrjog/opscloud4", "ixrjog@qq.com"))
                .build();
    }


    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(generatepApiInfo())
                //分组名称
                //.groupName("用户服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.baiyi.opscloud.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
