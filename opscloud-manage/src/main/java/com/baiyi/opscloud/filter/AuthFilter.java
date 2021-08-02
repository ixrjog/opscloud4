package com.baiyi.opscloud.filter;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.config.WhiteConfig;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
public class AuthFilter extends OncePerRequestFilter {

    @Resource
    private UserAuthFacade userAuthFacade;

    @Resource
    private WhiteConfig whiteConfig;

    /**
     * 前端框架 token 名称
     */
    public static final String AUTHORIZATION = "Authorization";

    // public static final String GITLAB_TOKEN = "X-Gitlab-Token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        // response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (!"options".equalsIgnoreCase(request.getMethod())) {
            String resourceName = request.getServletPath();
            // 单页不拦截页面,只拦截协议请求
            if ("/dashboard".equalsIgnoreCase(resourceName)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 跳过白名单
            if (checkWhitelist(resourceName)) {
                filterChain.doFilter(request, response);
                return;
            }
            //静态资源不拦截
            if (resourceName.endsWith(".js")
                    || resourceName.endsWith(".md")
                    || resourceName.endsWith(".css")
                    || resourceName.endsWith(".woff")
                    || resourceName.endsWith(".otf")
                    || resourceName.endsWith(".eot")
                    || resourceName.endsWith(".ttf")
                    || resourceName.endsWith(".svg")
                    || resourceName.endsWith(".jpg")
                    || resourceName.endsWith(".png")
                    || resourceName.endsWith(".html")) {
                filterChain.doFilter(request, response);
                return;
            }
            final String token = request.getHeader(AUTHORIZATION);
            try {
                userAuthFacade.tryUserHasResourceAuthorize(token, resourceName);
                filterChain.doFilter(request, response);
            } catch (AuthRuntimeException ex) {
                response.setContentType(APPLICATION_JSON_UTF8_VALUE);
                setHeaders(request, response);
                HttpResult result = new HttpResult(ex);
                response.getWriter().println(result);
            }
        } else {
            setHeaders(request, response);
            filterChain.doFilter(request, response);
        }
    }

    private boolean checkWhitelist(String resourceName) {
        for (String resource : whiteConfig.getUrls())
            if (resourceName.indexOf(resource) == 0)
                return true;
        return false;
    }

    public void setHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.addHeader("Access-Control-Allow-Origin", origin);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "*");
        //30 min
        response.addHeader("Access-Control-Max-Age", "1800");
    }
}
