package com.baiyi.opscloud.config;

/**
 * https://doc.xiaominfo.com/docs/quick-start
 * @Author baiyi
 * @Date 2023/3/28 15:46
 * @Version 1.0
 */
//@Configuration
//@EnableSwagger2WebMvc
public class Knife4jConfiguration {
//
//    @Value("${spring.application.version}")
//    private String version;
//
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
//
//
//    @Bean(value = "dockerBean")
//    public Docket dockerBean() {
//        // 指定使用Swagger2规范
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(generatepApiInfo())
//                //分组名称
//                //.groupName("用户服务")
//                .select()
//                //这里指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage("com.baiyi.opscloud.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
}
