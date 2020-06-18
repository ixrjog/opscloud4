package com.baiyi.opscloud.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
/**
 * @Author baiyi
 * @Date 2020/2/13 5:10 下午
 * @Version 1.0
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

//    @Resource
//    private AuthInterceptor authInterceptor;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

//    @Bean
//    public ServletRegistrationBean apiV1ServletBean(WebApplicationContext wac) {
//        DispatcherServlet ds = new DispatcherServlet(wac);
//        ServletRegistrationBean bean = new ServletRegistrationBean(ds, "/oc3/*");
//        bean.setName("opscloud-api");
//        return bean;
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/")
                .resourceChain(false);
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
//                .resourceChain(false);
//        registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
//        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
//        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
//        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
//        registry.addResourceHandler("/icon.ico").addResourceLocations("classpath:/static/");

    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOrigins("*"). //允许跨域的域名，可以用*表示允许任何域名使用
                allowedMethods("*"). //允许任何方法（post、get等）
                allowedHeaders("*"). //允许任何请求头
                allowCredentials(true). //带上cookie信息
                exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L); //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //允许上传的文件最大值
        factory.setMaxFileSize(DataSize.ofMegabytes(10 * 1024 * 1024));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(10 * 1024 * 1024));
        return factory.createMultipartConfig();
    }
}
