package com.baiyi.opscloud.configuration;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.*;

import jakarta.servlet.MultipartConfigElement;

/**
 * @Author baiyi
 * @Date 2020/2/13 5:10 下午
 * @Version 1.0
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    private static final String HOME = "index.html";

    /**
     * 首页
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName(HOME);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
                .resourceChain(false);
        // registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // .resourceChain(false);
        // registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
        // .resourceChain(false);
//        registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
//        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
//        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
//        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
//        registry.addResourceHandler("/icon.ico").addResourceLocations("classpath:/static/");

    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 允许上传的文件最大值
        factory.setMaxFileSize(DataSize.ofMegabytes(10 * 1024 * 1024));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(10 * 1024 * 1024));
        return factory.createMultipartConfig();
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
        return (factory) -> factory.setRegisterDefaultServlet(true);
    }

    /**
     * SpringBoot 2.4+ 修改
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                // 启用后会导致websocket跨域配置失效
                // .allowCredentials(true)
                .maxAge(3600L);
    }

}
