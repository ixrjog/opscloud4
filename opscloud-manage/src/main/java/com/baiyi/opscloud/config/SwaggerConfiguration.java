package com.baiyi.opscloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@SuppressWarnings("ALL")
//@Configuration
//@EnableSwagger2
@Component
public class SwaggerConfiguration {

    @Value("${spring.application.version}")
    private String version;

    /**
     * https://doc.xiaominfo.com/knife4j/documentation/
     */

//    @Bean
//    public Docket openAPI() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .forCodeGeneration(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.baiyi.opscloud.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(generatepApiInfo());
//    }

//    private ApiInfo generatepApiInfo() {
//        return new ApiInfoBuilder()
//                .title(String.format("OPSCLOUD %s", version))
//                .description("OPSCLOUD OPEN-API接口文档")
//                .version(String.format("%s-RELEASE", version))
//                .license("APACHE LICENSE, VERSION 2.0")
//                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
//                .contact(new Contact("白衣", "https://github.com/ixrjog/opscloud4", "ixrjog@qq.com"))
//                .build();
//    }

}