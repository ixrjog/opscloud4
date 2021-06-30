package com.baiyi.caesar.filter;

import com.baiyi.caesar.common.exception.auth.AuthRuntimeException;
import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.config.WhiteConfig;
import com.baiyi.caesar.facade.auth.UserAuthFacade;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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

    // 改成配置文件
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars",
            "/ws/guacamole/tunnel", // RDP,VNC协议转换通道
            "/ws/terminal" // WebTerminal
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (!request.getMethod().equalsIgnoreCase("options")) {
            String resourceName = request.getServletPath();
            // 单页不拦截页面,只拦截协议请求
            if (resourceName.equalsIgnoreCase("/dashboard")) {
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
                HttpResult result = new HttpResult(ex);
                setHeaders(request, response);
                // response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(result);

            }
        } else {
            setHeaders(request, response);
            // response.setContentType("application/json;charset=UTF-8");
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
        response.addHeader("Access-Control-Max-Age", "1800");//30 min
    }
}
